package org.bitstrings.maven.plugins.portallocator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import mockit.Mock;
import mockit.MockUp;

public class PortAllocatorServiceMock
    extends MockUp<PortAllocatorService>
{
    private Set<Integer> portExcludes = new HashSet<>();

    public Set<Integer> portExcludes()
    {
        return portExcludes;
    }

    @Mock
    protected boolean isPortAvail( final int port )
        throws IOException
    {
        System.out.println( "MOCK MOCK MOCK - " + port );

        return !portExcludes.contains( port );
    }
}
