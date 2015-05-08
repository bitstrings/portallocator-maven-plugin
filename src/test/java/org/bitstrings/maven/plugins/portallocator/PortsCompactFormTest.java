package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.MavenUtils.*;
import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class PortsCompactFormTest
    extends AbstractPortAllocatorTest
{
    @Test
    public void portsCompactForm()
        throws Exception
    {
        new PortAllocatorServiceMock();

        MavenProject project = createBaseMavenProject();

        rule.executeMojo( project, "allocate", domFromString( "<ports>compact</ports>" ) );

        assertPropertyEquals( "8090", "compact.port", project );
    }
}
