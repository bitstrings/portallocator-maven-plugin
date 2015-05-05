package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.MavenUtils.*;
import static org.bitstrings.maven.plugins.portallocator.TestUtils.*;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class PortAllocatorMojoTest
    extends AbstractPortAllocatorTest
{
    @Test
    public void portsSimpleCompactForm()
        throws Exception
    {
        new PortAllocatorServiceMock( 8090 );

        MavenProject project = createBaseMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>compact</ports>" ) );

        assertEquals( "8090", "compact.port", project );
    }

    @Test
    public void portsCDForm()
        throws Exception
    {
        new PortAllocatorServiceMock( 8095, 8190 );

        MavenProject project = createBaseMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>cd:8190</ports>" ) );

        assertEquals( "8190", "cd.port", project );
    }

    @Test
    public void portsFullForm()
        throws Exception
    {
        new PortAllocatorServiceMock( 8090, 9090, 9191 );

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
                        + "</port>"
                        + "</ports>"
                )
        );

        assertEquals( "8090", "full.port", project );
        assertEquals( "9191", "full-preferred.port", project );
    }
}
