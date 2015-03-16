package org.bitstrings.maven.plugins.portallocator;

import static org.apache.maven.plugins.annotations.LifecyclePhase.*;

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
import org.bitstrings.maven.plugins.portallocator.PortAllocation.RelativePort;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

@Mojo( name = "allocate", defaultPhase = VALIDATE, threadSafe = true, requiresOnline = false )
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

    private static final String PREFERRED_PORTS_DEFAULT = "8090";
    private static final String PORT_NAME_SUFFIX_DEFAULT = "port";
    private static final String OFFSET_NAME_SUFFIX_DEFAULT = "port-offset";
    private static final String PORT_ALLOCATOR_DEFAULT_ID = "default";

    private static final Set<Integer> allocatedPorts = new HashSet<>();
    private static final Map<String, PortAllocatorService> portAllocatorServiceMap = new HashMap<>();

    @Override
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        for ( PortAllocator portAllocator : portAllocators )
        {
            final PortAllocatorService pas = buildPortAllocatorService( portAllocator );

            portAllocatorServiceMap.put( portAllocator.getId(), pas );
        }

        if ( ports != null )
        {
            PortAllocatorService pas =
                ports.getPortAllocator() == null
                        ? buildPortAllocatorService( ports.getPortAllocator() )
                        : portAllocatorServiceMap.get( ports.getPortAllocatorRef() );

            for ( Port port : ports.getPorts() )
            {
            }
        }

        for ( final PortAllocation portAllocation : portAllocations )
        {
            try
            {
                final String portName = portAllocation.getName();

                portAllocatorBuilder.listener(
                    new PortAllocatorService.Listener()
                    {
                        private final Map<String, Integer> rProps = new HashMap<>();

                        @Override
                        public boolean beforeAllocation( int potentialPort )
                        {
                            synchronized ( allocatedPorts )
                            {
                                if ( allocatedPorts.contains( potentialPort ) )
                                {
                                    return false;
                                }


                                for ( RelativePort relativePort : portAllocation.getRelativePorts() )
                                {
                                    final String rPortName = relativePort.getName();
                                    final int rOffset = relativePort.getOffset();
                                    final int rPort = ( potentialPort + rOffset );

                                    try
                                    {
                                        if ( new PortAllocatorService.Builder()
                                                    .port( rPort ).build()
                                                    .nextAvailablePort() == PortAllocatorService.PORT_NA )
                                        {
                                            return false;
                                        }
                                    }
                                    catch ( Exception e ) {}

                                    if ( !Strings.isNullOrEmpty( rPortName ) )
                                    {
                                        rProps.put( rPortName, rPort );
                                    }
                                }

                                return true;
                            }
                        }

                        @Override
                        public void afterAllocation( int port )
                        {
                            synchronized ( allocatedPorts )
                            {
                                allocatedPorts.add( port );

                                mavenProject.getProperties().putAll( rProps );

                                for ( Map.Entry<String, Integer> rEntry : rProps.entrySet() )
                                {
                                    allocatedPorts.add( rEntry.getValue() );

                                    setAllocationProperty(
                                            portAllocation,
                                            rEntry.getValue(), portName,
                                            rEntry.getKey() );
                                }
                            }
                        }
                    }
                );

                setAllocationProperty( portAllocation, portAllocatorBuilder.build().nextAvailablePort(), portName );
            }
            catch ( Exception e )
            {
                throw new MojoFailureException( e.getLocalizedMessage(), e );
            }
        }
    }

    protected PortAllocatorService buildPortAllocatorService( PortAllocator portAllocator )
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

    protected String getPortPropertyName( PortAllocation portAllocation, String... levelNames )
    {
        return StringUtils.join( levelNames, portAllocation.getNameLevelSeparator() );
    }

    protected void setAllocationProperty( PortAllocation portAllocation, int port, String... levelNames )
    {
        final String portNamePrefix =
                ( getPortPropertyName( portAllocation, levelNames ) + portAllocation.getNameLevelSeparator() );

        final String portPropertyName = portNamePrefix + portAllocation.getPortNameSuffix();

        mavenProject.getProperties().put( portPropertyName, String.valueOf( port ) );

        if ( !quiet && getLog().isInfoEnabled() )
        {
            getLog().info( "Assigning port [" + port + "] to property [" +  portPropertyName + "]" );
        }

        if ( portAllocation.getOffsetBasePort() != null )
        {
            final String offsetPropertyName = portNamePrefix + portAllocation.getOffsetNameSuffix();
            final String offset = String.valueOf( port - portAllocation.getOffsetBasePort() );

            mavenProject.getProperties().put( offsetPropertyName, offset );

            if ( !quiet && getLog().isInfoEnabled() )
            {
                getLog().info(
                        "Assigning offset [" + offset + "] "
                            + "from base port [" + portAllocation.getOffsetBasePort() + "] "
                            + "to property [" +  offsetPropertyName + "]" );
            }
        }
    }
}
