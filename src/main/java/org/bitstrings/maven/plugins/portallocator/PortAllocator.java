package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Splitter;

public class PortAllocator
{
    public static class Strategy
    {
    }

    public static class PreferredPorts
    {
        private List<Integer> ports = new LinkedList<Integer>();

        public void addPort( int port )
        {
            ports.add( port );
        }

        public void set( String csvPorts )
        {
            for ( String portStr : Splitter.on( ',' ).trimResults().omitEmptyStrings().split( csvPorts ) )
            {
                ports.add( Integer.parseInt( portStr ) );
            }
        }
    }

    private String name;
    private PreferredPorts preferredPorts;
    private Strategy strategy;

    public String getName()
    {
        return name;
    }

    public PreferredPorts getPreferredPorts()
    {
        return preferredPorts;
    }

    public Strategy getStrategy()
    {
        return strategy;
    }
}
