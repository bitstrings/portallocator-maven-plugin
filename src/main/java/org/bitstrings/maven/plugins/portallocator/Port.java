package org.bitstrings.maven.plugins.portallocator;

public class Port
{
    private PortSelector portSelector;
    private String portSelectorRef;
    private String name;
    private int offsetBasePort;
    private String portNameSuffix;
    private String offsetNameSuffix;

    public PortSelector getPortSelector()
    {
        return portSelector;
    }

    public String getPortSelectorRef()
    {
        return portSelectorRef;
    }

    public String getName()
    {
        return name;
    }

    public int getOffsetBasePort()
    {
        return offsetBasePort;
    }

    public String getPortNameSuffix()
    {
        return portNameSuffix;
    }

    public String getOffsetNameSuffix()
    {
        return offsetNameSuffix;
    }
}
