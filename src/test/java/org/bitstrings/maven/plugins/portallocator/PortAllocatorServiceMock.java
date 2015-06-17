package org.bitstrings.maven.plugins.portallocator;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import mockit.Mock;
import mockit.MockUp;

import org.bitstrings.maven.plugins.portallocator.PortAllocatorService.PortRange;

public class PortAllocatorServiceMock
    extends MockUp<PortAllocatorService>
{
    private final Set<PortRange> unavailablePorts = new LinkedHashSet<>();

    public PortAllocatorServiceMock() {}

    public PortAllocatorServiceMock( PortRange... portRanges )
    {
        addUnavailablePorts( portRanges );
    }

    public PortAllocatorServiceMock addUnavailablePorts( PortRange... portRanges )
    {
        Collections.addAll( unavailablePorts, portRanges );

        return this;
    }

    public Set<PortRange> availablePorts()
    {
        return unavailablePorts;
    }

    @Mock
    protected boolean isPortAllocatable( int port )
        throws IOException
    {
        for ( PortRange portRange : unavailablePorts )
        {
            if ( portRange.isInRange( port ) )
            {
                System.out.println( "Port [" + port + "] and is unavailable." );

                return false;
            }
        }

        System.out.println( "Port [" + port + "] and is available." );

        return true;
    }
}
