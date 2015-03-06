package org.bitstrings.maven.plugins.portallocator;

public class Port
{
    private PortPool pool;
    private String poolRef;
    private String name;
    private boolean setOffset;

    public PortPool getPool()
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
