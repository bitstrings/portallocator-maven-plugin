package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class PortPools
{
    private final List<PortPool> portPools = new LinkedList<>();

    public void addPortPool( PortPool portPool )
    {
        portPools.add( portPool );
    }
}
