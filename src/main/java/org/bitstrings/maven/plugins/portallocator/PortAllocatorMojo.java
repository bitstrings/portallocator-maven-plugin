package org.bitstrings.maven.plugins.portallocator;

import static org.apache.maven.plugins.annotations.LifecyclePhase.*;

import java.util.LinkedList;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

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
    private List<PortPool> portPools = new LinkedList<>();

    @Parameter
    private List<Port> ports = new LinkedList<>();

    @Override
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
    }


    public static void main( String[] args )
        throws Exception
    {
    }
}
