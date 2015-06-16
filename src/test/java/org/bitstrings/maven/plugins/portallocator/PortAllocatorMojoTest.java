package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.MavenUtils.*;
import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import org.apache.maven.project.MavenProject;
import org.bitstrings.maven.plugins.portallocator.PortAllocatorService.PortRange;
import org.bitstrings.test.junit.runner.ClassLoaderPerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( ClassLoaderPerTestRunner.class )
public class PortAllocatorMojoTest
    extends AbstractPortAllocatorTest
{
    @Test
    public void should_assignPortDefaultPort_when_usingCompactFormAndNoPort()
        throws Exception
    {
        MavenProject project = createNewMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>compact</ports>" ) );

        assertPropertyEquals( "8090", "compact.port", project );
    }

    @Test
    public void should_assignPort_when_usingCompactFormAndPreferredPort()
        throws Exception
    {
        MavenProject project = createNewMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>compact:8095</ports>" ) );

        assertPropertyEquals( "8095", "compact.port", project );
    }

    @Test
    public void should_assignMultiPort_when_usingCompactFormAndPreferredPort()
        throws Exception
    {
        MavenProject project = createNewMavenProject();

        rule.executeMojo(
                project,
                "allocate",
                domFromString( "<ports>compact1:8095,compact2:9999,  compact3:8888 </ports>" ) );

        assertPropertyEquals( "8095", "compact1.port", project );
        assertPropertyEquals( "9999", "compact2.port", project );
        assertPropertyEquals( "8888", "compact3.port", project );
    }

    @Test
    public void should_assignPort_when_usingFullFormAndPreferredPort()
        throws Exception
    {
        MavenProject project = createNewMavenProject();

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

        assertPropertyEquals( "8090", "full.port", project );
        assertPropertyEquals( "9191", "full-preferred.port", project );
    }

    @Test
    public void should_assignNextAvailablePort_when_usingPortUnavailable()
        throws Exception
    {
        portAllocatorServiceMock.addPorts( new PortRange( 8090, 8099 ) );

        MavenProject project = createNewMavenProject();

        rule.executeMojo(
                project,
                "allocate",
                domFromString( "<ports>next:8095</ports>" ) );

        assertPropertyNotBetween( 8090, 8099, "next.port", project );
    }
}
