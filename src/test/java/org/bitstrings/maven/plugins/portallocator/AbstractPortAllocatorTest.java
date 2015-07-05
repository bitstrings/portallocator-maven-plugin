package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Rule;

public class AbstractPortAllocatorTest
{
    @Rule
    public MojoRule rule = new MojoRule();

    @Rule
    public TestResources resources = new TestResources();

    protected PortAllocatorServiceMock portAllocatorServiceMock = new PortAllocatorServiceMock();

    public MavenProject createNewMavenProject()
        throws Exception
    {
        return rule.readMavenProject( resources.getBasedir( "base" ) );
    }

    public MavenProject executeMojo( String... params )
        throws Exception
    {
        return executeMojo( createNewMavenProject(), params );
    }

    public MavenProject executeMojo( MavenProject project, String... params )
        throws Exception
    {
        Xpp3Dom[] doms = new Xpp3Dom[ params.length ];

        for ( int i = 0; i < params.length; i++ )
        {
            doms[ i ] = domFromString( params[ i ] );
        }

        rule.executeMojo( project, "allocate", doms );

        return project;
    }
}
