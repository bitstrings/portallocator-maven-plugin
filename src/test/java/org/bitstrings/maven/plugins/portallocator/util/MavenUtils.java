package org.bitstrings.maven.plugins.portallocator.util;

import java.io.IOException;
import java.io.StringReader;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public final class MavenUtils
{
    private MavenUtils() {}

    public static String getPropertyValue( MavenProject project, String name )
    {
        return project.getProperties().getProperty( name );
    }

    public static Xpp3Dom domFromString( String str )
        throws XmlPullParserException, IOException
    {
        return Xpp3DomBuilder.build( new StringReader( str ) );
    }
}
