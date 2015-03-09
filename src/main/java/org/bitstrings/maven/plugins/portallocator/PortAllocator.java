package org.bitstrings.maven.plugins.portallocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PortAllocator
{
    public static class Builder
    {
        private final List<PortRange> portRanges = new LinkedList<>();

        public Builder port( int port )
        {
            portRanges.add( new PortRange( port ) );

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
            return new PortAllocator( portRanges );
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

    private SelectorStrategy selectorStrategy;

    public PortAllocator( Collection<PortRange> portRanges )
    {
        this.portRanges.addAll( portRanges );
    }

    public PortAllocator() {}

    public void addPortRange( PortRange portRange )
    {
        portRanges.add( portRange );
    }

    public void setSelectorStrategy( SelectorStrategy selectorStrategy )
    {
        this.selectorStrategy = selectorStrategy;
    }

    public int next()
    {
//        selectorStrategy.choose( lowest, highest, lastSelected );

        return 0;
    }
}
