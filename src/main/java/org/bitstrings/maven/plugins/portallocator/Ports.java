package org.bitstrings.maven.plugins.portallocator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bitstrings.maven.plugins.portallocator.util.Helpers;

public class Ports
    implements Iterable<Port>
{
    private String portAllocatorRef;

    private PortAllocator portAllocator;

    private final List<Port> portList = new LinkedList<>();

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

    public List<Port> getPortList()
    {
        return portList;
    }

    public void addPort( Port port )
    {
        portList.add( port );
    }

    public void set( String portNames )
    {
        for ( String portName : Helpers.iterateOnCsv( portNames ) )
        {
            final Port port = new Port();

            port.setName( portName );

            addPort( port );
        }
    }

    @Override
    public Iterator<Port> iterator()
    {
        return portList.iterator();
    }
}
