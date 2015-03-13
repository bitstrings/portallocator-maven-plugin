package org.bitstrings.maven.plugins.portallocator;

import static org.apache.maven.plugins.annotations.LifecyclePhase.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bitstrings.maven.plugins.portallocator.PortAllocation.DepletionAction;

import com.google.common.base.Splitter;

@Mojo( name = "allocate", defaultPhase = VALIDATE, threadSafe = true, requiresOnline = false )
public class PortAllocatorMojo
    extends AbstractMojo
{
    @Component
    private MavenProject mavenProject;

    @Component
    private MavenSession mavenSession;

    @Parameter( defaultValue = "false" )
    private boolean verbose;

    @Parameter
    private List<PortAllocation> portAllocations;

    private static final String PREFERRED_PORTS_DEFAULT = "8090";
    private static final String PORT_NAME_SUFFIX_DEFAULT = "port";
    private static final String OFFSET_NAME_SUFFIX_DEFAULT = "offset";
    private static final String NAME_LEVEL_SEPARATOR_DEFAULT = ".";

    private static final Set<Integer> allocatedPorts = new HashSet<>();

    public static final PortAllocator.Listener PORT_ALLOCATOR_LISTENER =
        new PortAllocator.Listener()
        {
            @Override
            public boolean beforeAllocation( int potentialPort )
            {
                synchronized ( allocatedPorts )
                {
                    return !allocatedPorts.contains( potentialPort );
                }
            }

            @Override
            public void afterAllocation( int port )
            {
                synchronized ( allocatedPorts )
                {
                    allocatedPorts.add( port );
                }
            }
        };

    @Override
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        for ( PortAllocation portAllocation : portAllocations )
        {
            initPortAllocation( portAllocation );

            final PortAllocator.Builder portAllocatorBuilder = new PortAllocator.Builder();

            if ( portAllocation.getDepletionAction() == PortAllocation.DepletionAction.CONTINUE )
            {
                portAllocatorBuilder.overflowPermitted();
            }

            for ( String portsCsv : portAllocation.getPreferredPorts().getPortsList() )
            {
                addPortRanges( portAllocatorBuilder, portsCsv );
            }

            portAllocatorBuilder.listener( PORT_ALLOCATOR_LISTENER );

            try
            {
                mavenProject.getProperties().put(
                        portAllocation.getName()
                            + portAllocation.getNameLevelSeparator()
                            + portAllocation.getPortNameSuffix(),
                        String.valueOf( portAllocatorBuilder.build().nextAvailablePort() ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    protected void initPortAllocation( PortAllocation portAllocation )
    {
        if ( portAllocation.getDepletionAction() == null )
        {
            portAllocation.setDepletionAction( DepletionAction.CONTINUE );
        }

        if ( portAllocation.getPreferredPorts() == null )
        {
            portAllocation.setPreferredPorts( new PortAllocation.PreferredPorts() );
        }

        if ( portAllocation.getPreferredPorts().getPortsList().isEmpty() )
        {
            portAllocation.getPreferredPorts().addPorts( PREFERRED_PORTS_DEFAULT );
        }

        if ( portAllocation.getPortNameSuffix() == null )
        {
            portAllocation.setPortNameSuffix( PORT_NAME_SUFFIX_DEFAULT );
        }

        if ( portAllocation.getOffsetNameSuffix() == null )
        {
            portAllocation.setOffsetNameSuffix( OFFSET_NAME_SUFFIX_DEFAULT );
        }

        if ( portAllocation.getNameLevelSeparator() == null )
        {
            portAllocation.setNameLevelSeparator( NAME_LEVEL_SEPARATOR_DEFAULT );
        }
    }

    protected void addPortRanges( PortAllocator.Builder builder, String portsCsv )
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
                        Integer.parseInt( portRange.substring( rangeSepIndex ).trim() ) );
                }
            }
        }
    }
}
