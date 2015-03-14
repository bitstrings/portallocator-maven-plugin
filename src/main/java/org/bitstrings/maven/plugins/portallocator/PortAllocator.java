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
    public static final int LOWEST_PORT_DEFAULT = 0x0400;
    public static final int HIGHEST_PORT_DEFAULT = 0xFFFF;

    public static final int PORT_NA = -1;

    public static class Builder
    {
        private final List<PortRange> portRanges = new LinkedList<>();
        private boolean overflowPermitted;
        private final List<Listener> listeners = new LinkedList<>();

        public Builder overflowPermitted()
        {
            overflowPermitted = true;

            return this;
        }

        public Builder port( int port )
        {
            portRanges.add( new PortRange( port ) );

            return this;
        }

        public Builder portFrom( int lowest )
        {
            portRanges.add( new PortRange( lowest, HIGHEST_PORT_DEFAULT ) );

            return this;
        }

        public Builder portRange( int lowest, int highest )
            throws IllegalArgumentException
        {
            portRanges.add( new PortRange( lowest, highest ) );

            return this;
        }

        public Builder listener( Listener listener )
        {
            listeners.add( listener );

            return this;
        }

        public PortAllocator build()
        {
            return new PortAllocator( overflowPermitted, portRanges, listeners );
        }
    }

    public static interface Listener
    {
        boolean beforeAllocation( int potentialPort );
        void afterAllocation( int port );
    }

    public static class PortRange
    {
        private int lowest;
        private int highest;

        private int next;

        private boolean wrapAround;

        public PortRange( int port )
        {
            this( port, port );
        }

        public PortRange( int lowest, int highest, int start, boolean wrapAround )
            throws IllegalArgumentException
        {
            this.lowest = lowest;
            this.highest = highest;

            if ( ( lowest > highest ) || ( lowest < 0 ) )
            {
                throw new IllegalArgumentException( "Range must be valid, i.e.: lowest >= 0 and lowest <= highest." );
            }

            this.next = start;

            this.wrapAround = wrapAround;
        }

        public PortRange( int lowest, int highest )
            throws IllegalArgumentException
        {
            this( lowest, highest, lowest, false );
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
            if ( next > highest )
            {
                next = ( wrapAround ? lowest : PORT_NA );
            }

            try
            {
                return next;
            }
            finally
            {
                ++next;
            }
        }
    }

    private final List<PortRange> portRanges = new ArrayList<>();
    private final Iterator<PortRange> portRangesIterator;
    private boolean overflowPermitted;
    private PortRange lastPortRange;

    private final List<Listener> listeners = new LinkedList<>();

    public PortAllocator( boolean overflowPermitted, Collection<PortRange> portRanges, List<Listener> listeners )
    {
        if ( ( portRanges == null ) || portRanges.isEmpty() )
        {
            portRanges.add( new PortRange( LOWEST_PORT_DEFAULT, HIGHEST_PORT_DEFAULT, LOWEST_PORT_DEFAULT, true ) );
        }
        else
        {
            this.portRanges.addAll( portRanges );
        }

        this.overflowPermitted = overflowPermitted;

        this.portRangesIterator = portRanges.iterator();

        this.lastPortRange = portRangesIterator.next();

        if ( listeners != null )
        {
            this.listeners.addAll( listeners );
        }
    }

    public boolean isOverflowPermitted()
    {
        return overflowPermitted;
    }

    public int nextAvailablePort()
        throws IOException
    {
        int port;
        int lastPort = LOWEST_PORT_DEFAULT;

        do
        {
            while ( ( port = lastPortRange.nextPort() ) == PORT_NA )
            {
                if ( portRangesIterator.hasNext() )
                {
                    lastPortRange = portRangesIterator.next();
                }
                else if ( overflowPermitted )
                {
                    lastPortRange = new PortRange( LOWEST_PORT_DEFAULT, HIGHEST_PORT_DEFAULT, lastPort, true );
                }
                else
                {
                    return PORT_NA;
                }
            }

            lastPort = port;

            for ( Listener l : listeners )
            {
                if ( !l.beforeAllocation( port ) )
                {
                    port = PORT_NA;

                    break;
                }
            }
        }
        while ( ( port == PORT_NA ) || !isPortAvail( port ) );

        for ( Listener l : listeners )
        {
            l.afterAllocation( port );
        }

        return port;
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
