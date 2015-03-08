package org.bitstrings.maven.plugins.portallocator;

public class PortRange
{
    public static final int NA = -1;

    private int lowest;
    private int highest;

    public PortRange()
    {
        this( NA, NA );
    }

    public PortRange( int lowest )
    {
        this( lowest, NA );
    }

    public PortRange( int lowest, int highest )
    {
        this.lowest = lowest;
        this.highest = highest;
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
