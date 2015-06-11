package org.bitstrings.maven.plugins.portallocator;

import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;

public class AbstractPortAllocatorTest
{
    @Rule
    public MojoRule rule = new MojoRule();

    @Rule
    public TestResources resources = new TestResources();

    public MavenProject createNewMavenProject()
        throws Exception
    {
        return rule.readMavenProject( resources.getBasedir( "base" ) );
    }

    @Before
    public void setUp()
        throws Exception
    {
        // yeah I know
        ( (Set<?>) FieldUtils.readStaticField(
                PortAllocatorMojo.class,
                "ALLOCATED_PORTS",
                true )
        ).clear();

        //new ServerSocketMock();
        //new PortAllocatorServiceMock();
    }
}
