package org.bitstrings.maven.plugins.portallocator;

public class Port
{
    private PortAllocator portAllocator;
    private String poolRef;
    private String name;
    private int offsetRefPort;

    public PortAllocator getPortAllocator()
    {
        return portAllocator;
    }

    public String getPoolRef()
    {
        return poolRef;
    }

    public String getName()
    {
        return name;
    }

    public int getOffsetRefPort()
    {
        return offsetRefPort;
    }
}
