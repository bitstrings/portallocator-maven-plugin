package org.bitstrings.maven.plugins.portallocator.util;

import com.google.common.base.Splitter;

public final class Helpers
{
    private Helpers() {}

    public static Iterable<String> iterateOnCsv( String csv )
    {
        return Splitter.on( ',' ).trimResults().omitEmptyStrings().split( csv );
    }
}
