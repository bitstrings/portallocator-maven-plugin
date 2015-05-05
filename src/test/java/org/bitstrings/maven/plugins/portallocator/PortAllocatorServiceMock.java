package org.bitstrings.maven.plugins.portallocator;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import mockit.Mock;
import mockit.MockUp;

public class PortAllocatorServiceMock
    extends MockUp<PortAllocatorService>
{
    private final Set<Integer> availablePorts = new LinkedHashSet<>();

    public PortAllocatorServiceMock( Integer... ports )
    {
        Collections.addAll( availablePorts, ports );
    }

    public Set<Integer> availablePorts()
    {
        return availablePorts;
    }

    @Mock
    protected boolean isPortAllocatable( int port )
        throws IOException
    {
        System.out.println( "<------------------------------> " + port );

        return availablePorts.remove( port );
    }
}
