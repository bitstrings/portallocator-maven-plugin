package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class Ports
{
    private final List<Port> ports = new LinkedList<>();

    public void addPort( Port port )
    {
        ports.add( port );
    }
}
