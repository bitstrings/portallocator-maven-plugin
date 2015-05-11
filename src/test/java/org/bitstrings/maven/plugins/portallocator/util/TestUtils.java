package org.bitstrings.maven.plugins.portallocator.util;

import static org.bitstrings.maven.plugins.portallocator.util.MavenUtils.*;

import org.apache.maven.project.MavenProject;
import org.junit.Assert;

public final class TestUtils
{
    private TestUtils() {}

    public static void assertPropertyEquals( String expected, String propertyName, MavenProject project )
    {
        Assert.assertEquals( expected, getPropertyValue( project, propertyName ) );
    }

    public static void assertPropertyNotEquals( String expected, String propertyName, MavenProject project )
    {
        Assert.assertNotEquals( expected, getPropertyValue( project, propertyName ) );
    }
}
