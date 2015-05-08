package org.bitstrings.maven.plugins.portallocator;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.myfaces.test.runners.TestPerClassLoaderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( TestPerClassLoaderRunner.class )
public class PortAllocatorMojoTest
    extends AbstractMojoTestCase
{
//    @Rule
//    public static TestResources resources = new TestResources();
//
//    @Rule
//    public static MojoRule rule = new MojoRule();

/*
    @Test
    public void portsSimpleCompactForm()
        throws Exception
    {
        new PortAllocatorServiceMock();

        MavenProject project = createBaseMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>compact</ports>" ) );

        assertEquals( "8090", "compact.port", project );
    }

    @Test
    public void portsCDForm()
        throws Exception
    {
        new PortAllocatorServiceMock();

        MavenProject project = createBaseMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>cd:8190</ports>" ) );

        assertEquals( "8190", "cd.port", project );
    }

    @Test
    public void portsFullForm()
        throws Exception
    {
        new PortAllocatorServiceMock();

        MavenProject project = createBaseMavenProject();

        rule.executeMojo(
                project,
                "allocate",
                domFromString(
                    "<ports>"
                        + "<port>"
                        + "<name>full</name>"
                        + "</port>"
                        + "<port>"
                        + "<name>full-preferred</name>"
                        + "<preferredPort>9191</preferredPort>"
                        + "</port>"
                        + "</ports>"
                )
        );

        //assertEquals( "8090", "full.port", project );
        //assertEquals( "9191", "full-preferred.port", project );
    }

    public MavenProject createBaseMavenProject()
        throws Exception
    {
        return rule.readMavenProject( resources.getBasedir( "base" ) );
    }
    */

    @Test
    public void createBaseMavenProject()
        throws Exception
    {
        System.out.println( lookupMojo( "allocate", new File( getBasedir(), "src/test/projects/base/pom.xml" ) ).getClass() );
    }
}
