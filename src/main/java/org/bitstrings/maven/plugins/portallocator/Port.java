package org.bitstrings.maven.plugins.portallocator;

public class Port
{
    private PortAllocator pool;
    private String poolRef;
    private String name;
    private boolean setOffset;

    public PortAllocator getPool()
    {
        return pool;
    }

    public String getPoolRef()
    {
        return poolRef;
    }

    public String getName()
    {
        return name;
    }

    public boolean isSetOffset()
    {
        return setOffset;
    }
}
