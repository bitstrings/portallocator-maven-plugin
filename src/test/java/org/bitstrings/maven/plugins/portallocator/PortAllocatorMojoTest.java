package org.bitstrings.maven.plugins.portallocator;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Rule;
import org.junit.Test;

public class PortAllocatorMojoTest
{
    @Rule
    public MojoRule rule = new MojoRule();

    @Rule
    public TestResources resources = new TestResources();

    @Test
    public void simplest()
        throws Exception
    {
        File pom = getTestFile( "src/test/resources/unit/project-to-test/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        PortAllocatorMojo myMojo = (PortAllocatorMojo) rule.lookupMojo( "allocate", pom );
        assertNotNull( myMojo );
        myMojo.execute();
    }

    private File getTestFile( String testProjectPath )
        throws Exception
    {
        resources.getBasedir( "test--" );

        return null;
    }
}
