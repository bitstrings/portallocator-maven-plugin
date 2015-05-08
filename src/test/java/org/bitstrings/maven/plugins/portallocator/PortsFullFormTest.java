package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.MavenUtils.*;
import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class PortsFullFormTest
    extends AbstractPortAllocatorTest
{
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

        assertPropertyEquals( "8090", "full.port", project );
        assertPropertyEquals( "9191", "full-preferred.port", project );
    }
}
