package org.bitstrings.maven.plugins.portallocator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bitstrings.maven.plugins.portallocator.util.Helpers;

public class PortAllocators
    implements Iterable<PortAllocator>
{
    private final List<PortAllocator> portAllocatorList = new LinkedList<PortAllocator>();

    public void addPortAllocator( PortAllocator portAllocator )
    {
        portAllocatorList.add( portAllocator );
    }

    public void set( String portAllocatorsStr )
    {
        for ( String portAllocatorStr : Helpers.iterateOnCsv( portAllocatorsStr ) )
        {
            final PortAllocator portAllocator = new PortAllocator();

            portAllocator.set( portAllocatorStr );

            addPortAllocator( portAllocator );
        }
    }

    @Override
    public Iterator<PortAllocator> iterator()
    {
        return portAllocatorList.iterator();
    }
}
