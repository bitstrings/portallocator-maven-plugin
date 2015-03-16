package org.bitstrings.maven.plugins.portallocator;

import static com.google.common.base.MoreObjects.*;
import static java.util.Collections.*;
import static org.apache.maven.plugins.annotations.LifecyclePhase.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Splitter;

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
    private List<PortAllocator> portAllocators;

    @Parameter
    private Ports ports;

    @Parameter( defaultValue = PORT_NAME_SUFFIX_DEFAULT )
    private String portNameSuffix;

    @Parameter( defaultValue = OFFSET_NAME_SUFFIX_DEFAULT )
    private String offsetNameSuffix;

    @Parameter( defaultValue = NAME_SEPARATOR_DEFAULT )
    private String nameSeparator;

    private static final String PREFERRED_PORTS_DEFAULT = "8090";
    private static final String PORT_NAME_SUFFIX_DEFAULT = "port";
    private static final String OFFSET_NAME_SUFFIX_DEFAULT = "port-offset";
    private static final String NAME_SEPARATOR_DEFAULT = ".";
    private static final String PORT_ALLOCATOR_DEFAULT_ID = "default";

    private static final Set<Integer> allocatedPorts = synchronizedSet( new HashSet<Integer>() );
    private static final Map<String, PortAllocatorService>
                portAllocatorServiceMap =
                    synchronizedMap( new HashMap<String, PortAllocatorService>() );

    static
    {
        portAllocatorServiceMap.put(
                PORT_ALLOCATOR_DEFAULT_ID,
                new PortAllocatorService.Builder()
                        .overflowPermitted()
                        .port( Integer.valueOf( PREFERRED_PORTS_DEFAULT ) )
                        .build() );
    }

    @Override
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            for ( PortAllocator portAllocator : portAllocators )
            {
                final PortAllocatorService pas = createPortAllocatorService( portAllocator );

                portAllocatorServiceMap.put( portAllocator.getId(), pas );
            }

            if ( ports != null )
            {
                PortAllocatorService pas =
                    ports.getPortAllocator() == null
                            ? createPortAllocatorService( ports.getPortAllocator() )
                            : portAllocatorServiceMap.get(
                                    firstNonNull( ports.getPortAllocatorRef(), PORT_ALLOCATOR_DEFAULT_ID ) );

                for ( Port port : ports.getPorts() )
                {
                    allocatePort( pas, port );
                }
            }
        }
        catch ( Exception e )
        {
            throw new MojoFailureException( e.getLocalizedMessage(), e );
        }
    }

    protected PortAllocatorService createPortAllocatorService( PortAllocator portAllocator )
    {
        initPortAllocator( portAllocator );

        final PortAllocatorService.Builder pasBuilder = new PortAllocatorService.Builder();

        if ( portAllocator.getDepletionAction() == PortAllocator.DepletionAction.CONTINUE )
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
                    return !allocatedPorts.contains( potentialPort );
                }

                @Override
                public void afterAllocation( int port )
                {
                    allocatedPorts.add( port );
                }
            }
        );

        return pasBuilder.build();
    }

    protected void initPortAllocator( PortAllocator portAllocator )
    {
        if ( portAllocator.getDepletionAction() == null )
        {
            portAllocator.setDepletionAction( PortAllocator.DepletionAction.CONTINUE );
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

    protected void addPortRanges( PortAllocatorService.Builder builder, String portsCsv )
    {
        for ( String portRange : Splitter.on( ',' ).trimResults().omitEmptyStrings().split( portsCsv ) )
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

    protected String getPropertyName( String separator, String... names )
    {
        return StringUtils.join( names, separator );
    }

    protected void allocatePort( PortAllocatorService pas, Port portConfig )
        throws IOException
    {
        final String portNamePrefix = portConfig.getName();
        final String portPropertyName = getPropertyName( nameSeparator, portNamePrefix, portNameSuffix );

        final int allocatedPort = pas.nextAvailablePort();

        mavenProject.getProperties().put( portPropertyName, String.valueOf( allocatedPort ) );

        if ( !quiet && getLog().isInfoEnabled() )
        {
            getLog().info( "Assigning port [" + allocatedPort + "] to property [" +  portPropertyName + "]" );
        }

        if ( portConfig.getOffsetBasePort() != null )
        {
            final String offsetPropertyName = getPropertyName( nameSeparator, portNamePrefix, offsetNameSuffix );
            final String offset = String.valueOf( allocatedPort - portConfig.getOffsetBasePort() );

            mavenProject.getProperties().put( offsetPropertyName, offset );

            if ( !quiet && getLog().isInfoEnabled() )
            {
                getLog().info(
                        "Assigning offset [" + offset + "] "
                            + "from base port [" + portConfig.getOffsetBasePort() + "] "
                            + "to property [" +  offsetPropertyName + "]" );
            }
        }
    }
}
