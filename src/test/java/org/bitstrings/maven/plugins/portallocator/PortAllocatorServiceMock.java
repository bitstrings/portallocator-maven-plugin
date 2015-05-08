package org.bitstrings.maven.plugins.portallocator;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import mockit.Mock;
import mockit.MockUp;

import org.bitstrings.maven.plugins.portallocator.PortAllocatorService;
import org.bitstrings.maven.plugins.portallocator.PortAllocatorService.PortRange;

public class PortAllocatorServiceMock
    extends MockUp<PortAllocatorService>
{
    private final Set<PortRange> unavailablePortRanges = new LinkedHashSet<>();

    public PortAllocatorServiceMock( PortRange... portRanges )
    {
        Collections.addAll( unavailablePortRanges, portRanges );
    }

    public Set<PortRange> availablePortRanges()
    {
        return unavailablePortRanges;
    }

    @Mock
    protected boolean isPortAllocatable( int port )
        throws IOException
    {
        for ( PortRange portRange : unavailablePortRanges )
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
