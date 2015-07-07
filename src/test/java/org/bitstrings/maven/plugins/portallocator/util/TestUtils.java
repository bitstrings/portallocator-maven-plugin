/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitstrings.maven.plugins.portallocator.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public final class TestUtils
{
    private TestUtils() {}

    public static String getPropertyValue( MavenProject project, String name )
    {
        return project.getProperties().getProperty( name );
    }

    public static Xpp3Dom domFromString( String str )
        throws XmlPullParserException, IOException
    {
        return Xpp3DomBuilder.build( new StringReader( str ) );
    }

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

    public static void assertPropertiesContainsEntries( Map<String, String> expected, MavenProject project )
    {
        assertPropertiesContainsEntries( expected, project.getProperties() );
    }

    public static void assertPropertiesContainsEntries( Map<?, ?> expected, Map<?, ?> actual )
    {
        for ( Map.Entry<?, ?> expectedEntry : expected.entrySet() )
        {
            assertTrue(
                "Expected property <" + expectedEntry.getKey() + "> not found.",
                actual.containsKey( expectedEntry.getKey() ) );

            assertEquals( expectedEntry.getValue(), actual.get( expectedEntry.getKey() ) );
        }
    }
}
