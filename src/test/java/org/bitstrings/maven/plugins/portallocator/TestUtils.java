package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.MavenUtils.*;

import org.apache.maven.project.MavenProject;
import org.junit.Assert;

public final class TestUtils
{
    private TestUtils() {}

    public static void assertEquals( String expected, String propertyName, MavenProject project )
    {
        Assert.assertEquals( expected, getPropertyValue( project, propertyName ) );
    }
}
