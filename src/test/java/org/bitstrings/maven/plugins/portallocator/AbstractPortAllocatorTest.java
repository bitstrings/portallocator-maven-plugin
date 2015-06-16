package org.bitstrings.maven.plugins.portallocator;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
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
}
