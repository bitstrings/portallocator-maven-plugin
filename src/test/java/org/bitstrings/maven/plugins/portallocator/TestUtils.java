package org.bitstrings.maven.plugins.portallocator;

import org.apache.maven.project.MavenProject;
import org.junit.Assert;

public final class TestUtils
{
    private TestUtils() {}

    public static String getPropertyValue( MavenProject project, String name )
    {
        return project.getProperties().getProperty( name );
    }

    public static void assertEquals( String expected, String propertyName, MavenProject project )
    {
        Assert.assertEquals( expected, getPropertyValue( project, propertyName ) );
    }
}
