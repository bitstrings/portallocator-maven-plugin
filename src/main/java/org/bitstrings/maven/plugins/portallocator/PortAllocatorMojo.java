package org.bitstrings.maven.plugins.portallocator;

import static com.google.common.base.MoreObjects.*;
import static java.util.Collections.*;
import static org.apache.maven.plugins.annotations.LifecyclePhase.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    @Parameter
    private File writePropertiesFile;

    private final Properties execPorts = new Properties();

    private static final String PREFERRED_PORTS_DEFAULT = "8090";
    private static final String PORT_NAME_SUFFIX_DEFAULT = "port";
    private static final String OFFSET_NAME_SUFFIX_DEFAULT = "port-offset";
    private static final String NAME_SEPARATOR_DEFAULT = ".";
    private static final String PORT_ALLOCATOR_DEFAULT_ID = "default";

    private static final Set<Integer> allocatedPorts = synchronizedSet( new HashSet<Integer>() );
    private static final Map<String, PortAllocatorService>
                portAllocatorServiceMap =
                    synchronizedMap( new HashMap<String, PortAllocatorService>() );

    private static final ReentrantLock ALLOCATION_LOCK = new ReentrantLock();

    private static final PortAllocatorService PORT_ALLOCATOR_SERVICE_DEFAULT;

    static
    {
        portAllocatorServiceMap.put(
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

                    ALLOCATION_LOCK.lock();

                    final PortAllocatorService existingPas = portAllocatorServiceMap.get( portAllocator.getId() );

                    if ( portAllocator.isPermitOverride()
                            || ( existingPas == null )
                            || ( portAllocator.getId().equals( PORT_ALLOCATOR_DEFAULT_ID )
                                    && ( existingPas == PORT_ALLOCATOR_SERVICE_DEFAULT ) ) )
                    {
                        portAllocatorServiceMap.put( portAllocator.getId(), pas );

                        if ( !quiet && getLog().isInfoEnabled() )
                        {
                            getLog().info( "Registering port allocator [" + portAllocator.getId() + "]");
                        }
                    }

                    ALLOCATION_LOCK.unlock();
                }
            }

            if ( ports != null )
            {
                if ( ( ports.getPortAllocatorRef() != null ) && ( ports.getPortAllocator() != null ) )
                {
                    throw new MojoFailureException(
                        "Either use a port allocator reference or define an inner allocator but you can use both." );
                }

                PortAllocatorService pas =
                    ports.getPortAllocator() == null
                            ? portAllocatorServiceMap.get(
                                    firstNonNull( ports.getPortAllocatorRef(), PORT_ALLOCATOR_DEFAULT_ID ) )
                            : createPortAllocatorService( ports.getPortAllocator() );

                if ( pas == null )
                {
                    throw new MojoFailureException(
                        "Cannot find port allocator [" + ports.getPortAllocatorRef() + "]" );
                }

                for ( Port port : ports.getPorts() )
                {
                    ALLOCATION_LOCK.lock();

                    allocatePort( pas, port );

                    ALLOCATION_LOCK.unlock();
                }
            }

            ALLOCATION_LOCK.lock();

            if ( writePropertiesFile != null )
            {
                final File parent = writePropertiesFile.getParentFile();

                if ( ( parent != null ) && !parent.exists() )
                {
                    parent.mkdirs();
                }

                try (final Writer out = new BufferedWriter( new FileWriter( writePropertiesFile ) ) )
                {
                    if ( !quiet && getLog().isInfoEnabled() )
                    {
                        getLog().info( "Writing ports file [" + writePropertiesFile + "]");
                    }

                    execPorts.store( out, "" );
                }
                catch ( Exception e )
                {
                    throw new MojoExecutionException( "Problem writing ports file [" + writePropertiesFile + "]", e );
                }
            }

            ALLOCATION_LOCK.unlock();
        }
        catch ( Exception e )
        {
            throw new MojoFailureException( e.getLocalizedMessage(), e );
        }
    }

    protected static PortAllocatorService createPortAllocatorService( PortAllocator portAllocator )
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

    protected static void initPortAllocator( PortAllocator portAllocator )
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

    protected static void addPortRanges( PortAllocatorService.Builder builder, String portsCsv )
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

    protected static String getPropertyName( String separator, String... names )
    {
        return StringUtils.join( names, separator );
    }

    protected void allocatePort( PortAllocatorService pas, Port portConfig )
        throws Exception
    {
        final String portNamePrefix = portConfig.getName();
        final String portPropertyName = getPropertyName( nameSeparator, portNamePrefix, portNameSuffix );

        final int allocatedPort = pas.nextAvailablePort();

        mavenProject.getProperties().put( portPropertyName, String.valueOf( allocatedPort ) );
        execPorts.setProperty( portPropertyName, String.valueOf( allocatedPort ) );

        if ( !quiet && getLog().isInfoEnabled() )
        {
            getLog().info( "Assigning port [" + allocatedPort + "] to property [" +  portPropertyName + "]" );
        }

        if ( portConfig.getOffsetBasePort() != null )
        {
            final String offsetPropertyName = getPropertyName( nameSeparator, portNamePrefix, offsetNameSuffix );
            final String offset = String.valueOf( allocatedPort - portConfig.getOffsetBasePort() );

            mavenProject.getProperties().put( offsetPropertyName, offset );
            execPorts.setProperty( offsetPropertyName, offset );

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
