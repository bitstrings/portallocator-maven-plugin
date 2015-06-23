/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitstrings.maven.plugins.portallocator;

import static com.google.common.base.MoreObjects.*;
import static java.util.Collections.*;
import static org.apache.commons.lang3.BooleanUtils.*;
import static org.apache.maven.plugins.annotations.LifecyclePhase.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bitstrings.maven.plugins.portallocator.util.Helpers;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

@Mojo( name = "allocate", defaultPhase = VALIDATE, threadSafe = true, requiresProject = true, requiresOnline = false )
public class PortAllocatorMojo
    extends AbstractMojo
{
    @Parameter( defaultValue = "${project}", readonly = true )
    private MavenProject mavenProject;

    @Parameter( defaultValue = "${session}", readonly = true )
    private MavenSession mavenSession;

    @Parameter( defaultValue = "false" )
    private boolean quiet;

    @Parameter
    private PortAllocators portAllocators;

    @Parameter
    private Ports ports;

    @Parameter( defaultValue = PORT_NAME_SUFFIX_DEFAULT )
    private String portNameSuffix;

    @Parameter( defaultValue = OFFSET_NAME_SUFFIX_DEFAULT )
    private String offsetNameSuffix;

    @Parameter( defaultValue = NAME_SEPARATOR_DEFAULT )
    private String nameSeparator;

    @Parameter
    private File writePropertiesFile;

    private final Map<String, Integer> executionPortMap = new HashMap<>();

    private static final String PREFERRED_PORTS_DEFAULT = "8090";
    private static final String PORT_NAME_SUFFIX_DEFAULT = "port";
    private static final String OFFSET_NAME_SUFFIX_DEFAULT = "port-offset";
    private static final String NAME_SEPARATOR_DEFAULT = ".";
    private static final String PORT_ALLOCATOR_DEFAULT_ID = "default";

    private static final Set<Integer> ALLOCATED_PORTS = synchronizedSet( new HashSet<Integer>() );
    private static final Map<String, PortAllocatorService>
                PORT_ALLOCATOR_SERVICE_MAP =
                    synchronizedMap( new HashMap<String, PortAllocatorService>() );

    private static final ReentrantLock ALLOCATION_LOCK = new ReentrantLock();

    private static final PortAllocatorService PORT_ALLOCATOR_SERVICE_DEFAULT;

    static
    {
        PORT_ALLOCATOR_SERVICE_MAP.put(
                PORT_ALLOCATOR_DEFAULT_ID,
                PORT_ALLOCATOR_SERVICE_DEFAULT = createPortAllocatorService( new PortAllocator() ) );
    }

    @Override
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            if ( portAllocators != null )
            {
                for ( PortAllocator portAllocator : portAllocators )
                {
                    final PortAllocatorService pas = createPortAllocatorService( portAllocator );

                    final PortAllocatorService existingPas = PORT_ALLOCATOR_SERVICE_MAP.get( portAllocator.getId() );

                    if ( portAllocator.isPermitOverride()
                            || ( existingPas == null )
                            || ( portAllocator.getId().equals( PORT_ALLOCATOR_DEFAULT_ID )
                                    && ( existingPas == PORT_ALLOCATOR_SERVICE_DEFAULT ) ) )
                    {
                        PORT_ALLOCATOR_SERVICE_MAP.put( portAllocator.getId(), pas );

                        if ( !quiet && getLog().isInfoEnabled() )
                        {
                            getLog().info( "Registering port allocator [" + portAllocator.getId() + "]");
                        }
                    }
                }
            }

            if ( ports != null )
            {
                if ( ( ports.getPortAllocatorRef() != null ) && ( ports.getPortAllocator() != null ) )
                {
                    throw new MojoExecutionException(
                        "Either use a port allocator reference or define an inner allocator but you can use both." );
                }

                PortAllocatorService pas =
                    ports.getPortAllocator() == null
                            ? PORT_ALLOCATOR_SERVICE_MAP.get(
                                    firstNonNull( ports.getPortAllocatorRef(), PORT_ALLOCATOR_DEFAULT_ID ) )
                            : createPortAllocatorService( ports.getPortAllocator() );

                if ( pas == null )
                {
                    throw new MojoExecutionException(
                        "Cannot find port allocator [" + ports.getPortAllocatorRef() + "]" );
                }

                // assign
                final LinkedListMultimap<String, Port> portGroupMap = LinkedListMultimap.create();

                for ( Port port : ports )
                {
                    final String offsetFrom = port.getOffsetFrom();
                    final String portGroupName = findGroupRoot( port, portGroupMap );

                    portGroupMap.put( portGroupName, port );

                    if ( offsetFrom != null )
                    {
                        Integer fromParent = executionPortMap.get( getPortName( portGroupName ) );
                        if ( fromParent == null )
                        {
                            throw new MojoExecutionException(
                                "Port [" + port.getName() + "] using offset from undefined [" + offsetFrom + "]." );
                        }

                        portGroupMap.put( offsetFrom, port );
                    }

                    Iterator<Port> portIterator = Iterators.singletonIterator( port );

                    while ( portIterator.hasNext() )
                    {
                        final Port portToAllocate = portIterator.next();

                        ALLOCATION_LOCK.lock();

                        ALLOCATED_PORTS.remove( executionPortMap.remove( getPortName( portToAllocate.getName() ) ) );
                        ALLOCATED_PORTS.remove( executionPortMap.remove( getOffsetName( portToAllocate.getName() ) ) );

                        ALLOCATION_LOCK.unlock();

                        if ( !allocatePort( pas, portToAllocate ) )
                        {
                            if ( offsetFrom != null )
                            {
                                portIterator = portGroupMap.get( portGroupName ).listIterator();
                            }
                        }
                    }
                }

                // log ports
                for ( Port port : ports )
                {
                    if ( !quiet && getLog().isInfoEnabled() )
                    {
                        String name = getPortName( port.getName() );
                        Integer value = executionPortMap.get( name );

                        if ( value != null )
                        {
                            getLog().info( "Assigning port [" + value + "] to property [" +  name + "]" );
                        }

                        name = getOffsetName( port.getName() );
                        value = executionPortMap.get( name );

                        if ( value != null )
                        {
                            getLog().info(
                                    "Assigning offset [" + value + "] "
                                        + "using preferred port [" + port.getPreferredPort() + "] "
                                        + "to property [" +  name + "]" );
                        }
                    }
                }
            }

            if ( writePropertiesFile != null )
            {
                final File parent = writePropertiesFile.getParentFile();

                if ( ( parent != null ) && !parent.exists() )
                {
                    parent.mkdirs();
                }

                try ( final Writer out = new BufferedWriter( new FileWriter( writePropertiesFile ) ) )
                {
                    if ( !quiet && getLog().isInfoEnabled() )
                    {
                        getLog().info( "Writing ports file [" + writePropertiesFile + "]" );
                    }

                    final Properties outProps = new Properties();
                    outProps.putAll(
                        Maps.transformValues(
                            executionPortMap,
                            new Function<Integer, String>()
                            {
                                @Override
                                public String apply( Integer input )
                                {
                                    return input.toString();
                                }
                            }
                        )
                    );
                    outProps.store( out, null );
                }
                catch ( Exception e )
                {
                    throw new MojoExecutionException( "Problem writing ports file [" + writePropertiesFile + "]", e );
                }
            }
        }
        catch ( MojoExecutionException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( e.getLocalizedMessage(), e );
        }
    }

    protected static PortAllocatorService createPortAllocatorService( PortAllocator portAllocator )
    {
        initPortAllocator( portAllocator );

        final PortAllocatorService.Builder pasBuilder = new PortAllocatorService.Builder();

        if ( portAllocator.getDepletedAction() == PortAllocator.DepletedAction.CONTINUE )
        {
            pasBuilder.overflowPermitted();
        }

        for ( String portsCsv : portAllocator.getPreferredPorts().getPortsList() )
        {
            addPortRanges( pasBuilder, portsCsv );
        }

        pasBuilder.listener(
            new PortAllocatorService.Listener()
            {
                @Override
                public boolean beforeAllocation( int potentialPort )
                {
                    ALLOCATION_LOCK.lock();

                    return !ALLOCATED_PORTS.contains( potentialPort );
                }

                @Override
                public void afterAllocation( int port )
                {
                    ALLOCATED_PORTS.add( port );

                    ALLOCATION_LOCK.unlock();
                }
            }
        );

        return pasBuilder.build();
    }

    protected static void initPortAllocator( PortAllocator portAllocator )
    {
        if ( portAllocator.getDepletedAction() == null )
        {
            portAllocator.setDepletedAction( PortAllocator.DepletedAction.CONTINUE );
        }

        if ( portAllocator.getPreferredPorts() == null )
        {
            portAllocator.setPreferredPorts( new PortAllocator.PreferredPorts() );
        }

        if ( portAllocator.getPreferredPorts().getPortsList().isEmpty() )
        {
            portAllocator.getPreferredPorts().addPorts( PREFERRED_PORTS_DEFAULT );
        }

        if ( portAllocator.getId() == null )
        {
            portAllocator.setId( PORT_ALLOCATOR_DEFAULT_ID );
        }
    }

    protected static void addPortRanges( PortAllocatorService.Builder builder, String portsCsv )
    {
        for ( String portRange : Helpers.iterateOnCsv( portsCsv ) )
        {
            final int rangeSepIndex = portRange.indexOf( '-' );

            if ( rangeSepIndex == -1 )
            {
                builder.port( Integer.parseInt( portRange.trim() ) );
            }
            else
            {
                final int lowest = Integer.parseInt( portRange.substring( 0 , rangeSepIndex ).trim() );

                if ( rangeSepIndex == portRange.length() )
                {
                    builder.portFrom( lowest );
                }
                else
                {
                    builder.portRange(
                        lowest,
                        Integer.parseInt( portRange.substring( rangeSepIndex + 1 ).trim() ) );
                }
            }
        }
    }

    protected boolean allocatePort( PortAllocatorService pas, Port portConfig )
        throws MojoExecutionException, IOException
    {
        final String portNamePrefix = portConfig.getName();
        final String portPropertyName = getPortName( portNamePrefix );

        final int allocatedPort;

        if ( portConfig.getOffsetFrom() != null )
        {
            if ( portConfig.getPreferredPort() == null )
            {
                throw new MojoExecutionException( "'preferredPort' must be set when using 'fromOffset'." );
            }

            final String offsetFromName = getOffsetName( portConfig.getOffsetFrom() );
            final Integer fromOffset = executionPortMap.get( offsetFromName );

            if ( fromOffset == null )
            {
                throw new MojoExecutionException(
                        "Port [" + portPropertyName + "] references an unknown port offset [" + offsetFromName + "]" );
            }

            allocatedPort = ( portConfig.getPreferredPort() + fromOffset );

            if ( !pas.isPortAvailable( allocatedPort ) )
            {
                return false;
            }
        }
        else
        {
            allocatedPort =
                ( ( portConfig.getPreferredPort() != null ) && pas.isPortAvailable( portConfig.getPreferredPort() ) )
                        ? portConfig.getPreferredPort()
                        : pas.nextAvailablePort();

            if ( allocatedPort == PortAllocatorService.PORT_NA )
            {
                throw new MojoExecutionException( "Unable to allocate a port for [" + portPropertyName + "]" );
            }
        }

        mavenProject.getProperties().put( portPropertyName, String.valueOf( allocatedPort ) );
        executionPortMap.put( portPropertyName, allocatedPort );

        if ( isTrue( portConfig.getSetOffsetProperty() != null ) )
        {
            if ( portConfig.getPreferredPort() == null )
            {
                throw new MojoExecutionException( "'preferredPort' must be set when 'setOffsetProperty = true'." );
            }

            final String offsetPropertyName = getOffsetName( portNamePrefix );
            final int offset = ( allocatedPort - portConfig.getPreferredPort() );

            mavenProject.getProperties().put( offsetPropertyName, String.valueOf( offset ) );
            executionPortMap.put( offsetPropertyName, offset );
        }

        return true;
    }

    protected String findGroupRoot( Port port, ListMultimap<String, Port> portGroupMap )
    {
        while (
            ( port != null )
                && ( port.getOffsetFrom() != null )
                && portGroupMap.containsKey( port.getOffsetFrom() ) )
        {
            port = portGroupMap.get( port.getOffsetFrom() ).get( 0 );
        }

        return port.getName();
    }

    protected String getPortName( String prefix )
    {
        return buildName( prefix, portNameSuffix );
    }

    protected String getOffsetName( String prefix )
    {
        return buildName( prefix, offsetNameSuffix );
    }

    protected String buildName( String... parts )
    {
        return StringUtils.join( parts, nameSeparator );
    }
}
