package org.bitstrings.maven.plugins.portallocator;

import static org.junit.Assert.*;

import java.io.File;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.io.Files;

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
        pom = Files.toString( getPomFile( "base" ), Charset.forName( "UTF-8" ) );
        pomDom = Xpp3DomBuilder.build( new StringReader( pom ) );
        pluginConfiguration = rule.extractPluginConfiguration( "portallocator-maven-plugin", pomDom );
    }

    @Test
    public void simplest()
        throws Exception
    {
        MavenProject project = rule.readMavenProject( getPomFile( "base" ).getParentFile() );

        rule.executeMojo( project, "allocate", Xpp3DomBuilder.build( new StringReader( "<ports>compact</ports>" ) ) );

        System.out.println( project.getProperties() );
    }

    private File getPomFile( String testProjectPath )
        throws Exception
    {
        File pom = new File( resources.getBasedir( testProjectPath ), "pom.xml" );

        assertNotNull( pom );
        assertTrue( pom.exists() );

        return pom;
    }
}
