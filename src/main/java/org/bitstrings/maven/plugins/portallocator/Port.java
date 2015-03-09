package org.bitstrings.maven.plugins.portallocator;

public class Port
{
    private PortSelector portSelector;
    private String poolRef;
    private String name;
    private int offsetRefPort;

    public PortSelector getPortSelector()
    {
        return portSelector;
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
