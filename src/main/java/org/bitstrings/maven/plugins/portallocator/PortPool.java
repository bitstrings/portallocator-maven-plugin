package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Splitter;

public class PortPool
{
    public enum Strategy
    {
        LINEAR,
        RANDOM;
    }

    public static class Ports
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
    private Strategy strategy;
    private int basePort;
    private int minPort;
    private int maxPort;
    private Ports ports;

    public String getName()
    {
        return name;
    }

    public Strategy getStrategy()
    {
        return strategy;
    }

    public void setStrategy( String strategy )
    {
        this.strategy = Strategy.valueOf( strategy.toUpperCase() );
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

    public Ports getPorts()
    {
        return ports;
    }
}
