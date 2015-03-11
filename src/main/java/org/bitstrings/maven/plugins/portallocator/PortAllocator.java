package org.bitstrings.maven.plugins.portallocator;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PortAllocator
{
    private static final int LOWEST_PORT_DEFAULT = 0x0400;
    private static final int HIGHEST_PORT_DEFAULT = 0xFFFF;

    private static final int PORT_NA = -1;

    public static class Builder
    {
        private final List<PortRange> portRanges = new LinkedList<>();
        private boolean overflowPermitted;

        public Builder port( int port )
        {
            portRanges.add( new PortRange( port ) );

            return this;
        }

        public Builder overflowPermitted()
        {
            overflowPermitted = true;

            return this;
        }

        public Builder portRange( int lowest, int highest )
            throws IllegalArgumentException
        {
            portRanges.add( new PortRange( lowest, highest ) );

            return this;
        }

        public PortAllocator build()
        {
            return new PortAllocator( overflowPermitted, portRanges );
        }
    }

    public static class PortRange
    {
        private int lowest;
        private int highest;

        private int current;

        private boolean wrapAround;

        public PortRange( int port )
        {
            this( port, port );
        }

        public PortRange( int lowest, int highest, int current, boolean wrapAround )
            throws IllegalArgumentException
        {
            this.lowest = lowest;
            this.highest = highest;

            if ( ( lowest > highest ) || ( lowest < 0 ) )
            {
                throw new IllegalArgumentException( "Range must be valid, i.e.: lowest >= 0 and lowest <= highest." );
            }

            this.current = current;

            this.wrapAround = wrapAround;
        }

        public PortRange( int lowest, int highest )
            throws IllegalArgumentException
        {
            this( lowest, highest, PORT_NA, false );
        }

        public int getLowest()
        {
            return lowest;
        }

        public int getHighest()
        {
            return highest;
        }

        public int nextPort()
        {
            if ( current == PORT_NA )
            {
                current = lowest;
            }
            else if ( ( ++current ) > highest )
            {
                current = ( wrapAround ? lowest : PORT_NA );
            }

            return current;
        }
    }

    private final List<PortRange> portRanges = new ArrayList<>();
    private final Iterator<PortRange> portRangesIterator;
    private boolean overflowPermitted;
    private PortRange lastPortRange;
    private int lastPort;
    private int lastValidPort;

    public PortAllocator( boolean overflowPermitted, Collection<PortRange> portRanges )
    {
        this.portRanges.addAll( portRanges );
        this.overflowPermitted = overflowPermitted;

        this.portRangesIterator = portRanges.iterator();

        this.lastPortRange = portRangesIterator.next();
    }

    public boolean isOverflowPermitted()
    {
        return overflowPermitted;
    }

    public int nextAvailablePort()
        throws IOException
    {
        do
        {
            while ( ( lastPort = lastPortRange.nextPort() ) == PORT_NA )
            {
                if ( portRangesIterator.hasNext() )
                {
                    lastPortRange = portRangesIterator.next();
                }
                else
                {
                    if ( overflowPermitted )
                    {
                        lastPortRange = new PortRange( LOWEST_PORT_DEFAULT, HIGHEST_PORT_DEFAULT, lastValidPort, true );
                    }

                    return PORT_NA;
                }
            }
        }
        while ( !isPortAvail( lastPort ) );

        return ( lastValidPort = lastPort );
    }

    protected boolean isPortAvail( final int port )
        throws IOException
    {
        ServerSocket server;
        try
        {
            server = new ServerSocket( port );
        }
        catch (IOException e)
        {
            return false;
        }

        server.close();

        return true;
    }
}
