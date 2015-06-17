package org.bitstrings.maven.plugins.portallocator.util;

import static org.assertj.core.api.Assertions.*;
import static org.bitstrings.maven.plugins.portallocator.util.MavenUtils.*;
import static org.junit.Assert.*;

import org.apache.maven.project.MavenProject;
import org.assertj.core.api.Condition;

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
        assertThat(
                Integer.valueOf( getPropertyValue( project, propertyName ) ) )
            .isBetween( from, to );
    }

    public static void assertPropertyNotBetween(
            final int from, final int to, String propertyName, MavenProject project )
    {
        assertThat(
                Integer.valueOf( getPropertyValue( project, propertyName ) ) )
            .is(
                new Condition<Integer>( "WHAT" )
                {
                    @Override
                    public boolean matches( Integer value )
                    {
                        return ( value < from || value > to );
                    }
                }
            );
    }
}
