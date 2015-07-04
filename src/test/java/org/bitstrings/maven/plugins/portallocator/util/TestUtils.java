package org.bitstrings.maven.plugins.portallocator.util;

import static org.bitstrings.maven.plugins.portallocator.util.MavenUtils.*;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Properties;

import org.apache.maven.project.MavenProject;

public final class TestUtils
{
    private TestUtils() {}

    public static void assertPropertyEquals( String expected, String propertyName, MavenProject project )
    {
        assertEquals( expected, getPropertyValue( project, propertyName ) );
    }

    public static void assertPropertyEquals( int expected, String propertyName, MavenProject project )
    {
        assertPropertyEquals( String.valueOf( expected ), propertyName, project );
    }

    public static void assertPropertyNotEquals( String expected, String propertyName, MavenProject project )
    {
        assertNotEquals( expected, getPropertyValue( project, propertyName ) );
    }

    public static void assertPropertyNotEquals( int expected, String propertyName, MavenProject project )
    {
        assertPropertyNotEquals( String.valueOf( expected ), propertyName, project );
    }

    public static void assertPropertyBetween(
            int from, int to, String propertyName, MavenProject project )
    {
        final int actual = Integer.valueOf( getPropertyValue( project, propertyName ) );

        assertTrue(
                "Actual <" + actual + "> must be between <" + from + "> and <" + to + ">.",
                ( actual >= from ) && ( actual <= to ) );
    }

    public static void assertPropertyNotBetween(
            final int from, final int to, String propertyName, MavenProject project )
    {
        final int actual = Integer.valueOf( getPropertyValue( project, propertyName ) );

        assertTrue(
                "Actual <" + actual + "> must not be between <" + from + "> and <" + to + ">.",
                ( actual < from ) || ( actual > to ) );
    }

    public static void assertPropertiesEquals( Map<String, String> expected, MavenProject project )
    {
        final Properties actual = project.getProperties();

        for ( Map.Entry<String, String> expectedEntry : expected.entrySet() )
        {
            assertTrue(
                "Expected property <" + expectedEntry.getKey() + "> not found.",
                actual.containsKey( expectedEntry.getKey() ) );

            assertEquals( expectedEntry.getValue(), actual.getProperty( expectedEntry.getKey() ) );
        }
    }
}
