package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.TestUtils.*;

import java.io.StringReader;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PortAllocatorMojoTest
{
    private String pom;

    private Xpp3Dom pomDom;

    private PlexusConfiguration pluginConfiguration;

    @Rule
    public TestResources resources = new TestResources();

    @Rule
    public MojoRule rule = new MojoRule();

    @Before
    public void setUp()
        throws Exception
    {
//        pomDom = Xpp3DomBuilder.build( new StringReader( pom ) );
//        pluginConfiguration = rule.extractPluginConfiguration( "portallocator-maven-plugin", pomDom );
    }

    @Test
    public void compactPorts()
        throws Exception
    {
        new PortAllocatorServiceMock( 8090 );

        MavenProject project = rule.readMavenProject( resources.getBasedir( "base" ) );

        rule.executeMojo( project, "allocate", Xpp3DomBuilder.build( new StringReader( "<ports>compact</ports>" ) ) );

        assertEquals( "8090", "compact.port", project );
    }
}
