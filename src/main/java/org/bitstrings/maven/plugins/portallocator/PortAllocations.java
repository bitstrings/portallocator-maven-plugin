package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class PortAllocations
{
    private List<PortAllocation> portAllocations = new LinkedList<>();

    public void addPortAllocation( PortAllocation portAllocation )
    {
        portAllocations.add( portAllocation );
    }
}
