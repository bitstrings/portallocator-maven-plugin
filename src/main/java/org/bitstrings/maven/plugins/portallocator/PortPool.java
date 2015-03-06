package org.bitstrings.maven.plugins.portallocator;

import java.util.List;

public class PortPool
{
    public enum Strategy
    {
        LINEAR,
        RANDOM;
    }

    private String name;
    private Strategy strategy;
    private int basePort;
    private int minPort;
    private int maxPort;
    private List<Integer> ports;

    public String getName()
    {
        return name;
    }

    public int getBasePort()
    {
        return basePort;
    }

    public int getMinPort()
    {
        return minPort;
    }

    public int getMaxPort()
    {
        return maxPort;
    }

    public List<Integer> getPorts()
    {
        return ports;
    }
}
