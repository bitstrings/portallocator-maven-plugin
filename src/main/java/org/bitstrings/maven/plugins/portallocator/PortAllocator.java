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

    private static final int PREFERRED_PORT_DEFAULT = 8090;

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

        public PortRange( int port )
        {
            this( port, port );
        }

        public PortRange( int lowest, int highest )
            throws IllegalArgumentException
        {
            this.lowest = lowest;
            this.highest = highest;

            if ( ( lowest > highest ) || ( lowest < 0 ) )
            {
                throw new IllegalArgumentException( "Range must be valid, i.e.: lowest >= 0 and lowest <= highest." );
            }
        }

        public int getLowest()
        {
            return lowest;
        }

        public int getHighest()
        {
            return highest;
        }
    }

    private final List<PortRange> portRanges = new ArrayList<>();
    private boolean overflowPermitted;

    private Iterator<PortRange> portRangesIterator;
    private PortRange lastPortRange;

    public PortAllocator( boolean overflowPermitted, Collection<PortRange> portRanges )
    {
        this.portRanges.addAll( portRanges );
        this.overflowPermitted = overflowPermitted;

        this.portRangesIterator = portRanges.iterator();
    }

    public boolean isOverflowPermitted()
    {
        return overflowPermitted;
    }

    public int next()
    {
//        if ( !portRangesIterator.hasNext() )
//        {
//            if ( !overflowPermitted )
//            {
//                return PORT_NA;
//            }
//
//        }

        if ( ( lastPortRange == null ) && portRangesIterator.hasNext() )
        {
            lastPortRange = portRangesIterator.next();
        }

        return 0;
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
