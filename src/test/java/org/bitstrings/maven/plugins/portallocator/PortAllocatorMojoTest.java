package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.MavenUtils.*;
import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class PortAllocatorMojoTest
    extends AbstractPortAllocatorTest
{
    @Test
    public void should_assignPort_when_usingCompactForm()
        throws Exception
    {
        MavenProject project = createNewMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>compact</ports>" ) );

        assertPropertyEquals( "8090", "compact.port", project );
    }

    @Test
    public void should_assignPort_when_usingFullForm()
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
}
