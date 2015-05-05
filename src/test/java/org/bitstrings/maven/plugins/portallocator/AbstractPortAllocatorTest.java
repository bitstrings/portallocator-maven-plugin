package org.bitstrings.maven.plugins.portallocator;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.junit.Rule;

public class AbstractPortAllocatorTest
{
    @Rule
    protected TestResources resources = new TestResources();

    @Rule
    protected MojoRule rule = new MojoRule();

    public MavenProject createBaseMavenProject()
        throws Exception
    {
        return rule.readMavenProject( resources.getBasedir( "base" ) );
    }
}
