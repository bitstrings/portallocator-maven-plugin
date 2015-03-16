package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class Ports
{
    private String portAllocatorRef;

    private PortAllocator portAllocator;

    private final List<Port> ports = new LinkedList<>();

    public String getPortAllocatorRef()
    {
        return portAllocatorRef;
    }

    public void setPortAllocatorRef( String portAllocatorRef )
    {
        this.portAllocatorRef = portAllocatorRef;
    }

    public PortAllocator getPortAllocator()
    {
        return portAllocator;
    }

    public void setPortAllocator( PortAllocator portAllocator )
    {
        this.portAllocator = portAllocator;
    }

    public List<Port> getPorts()
    {
        return ports;
    }

    public void addPort( Port port )
    {
        ports.add( port );
    }
}
