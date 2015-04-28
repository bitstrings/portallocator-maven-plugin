package org.bitstrings.maven.plugins.portallocator.util;

import com.google.common.base.Splitter;

public final class Helpers
{
    private Helpers() {}

    public static Iterable<String> iterateOnCsv( String csv )
    {
        return iterateOnSplit( csv, "," );
    }

    public static Iterable<String> iterateOnSplit( String text, String separator )
    {
        return Splitter.on( separator ).trimResults().omitEmptyStrings().split( text );
    }
}
