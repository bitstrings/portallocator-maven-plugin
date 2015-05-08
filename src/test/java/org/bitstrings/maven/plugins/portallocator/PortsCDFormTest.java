package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.MavenUtils.*;
import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class PortsCDFormTest
    extends AbstractPortAllocatorTest
{
    @Test
    public void portsCDForm()
        throws Exception
    {
        new PortAllocatorServiceMock();

        MavenProject project = createBaseMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>cd:8190</ports>" ) );

        assertPropertyEquals( "8190", "cd.port", project );
    }
}
