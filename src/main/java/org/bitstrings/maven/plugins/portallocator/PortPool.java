package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Splitter;

public class PortPool
{
    public static class PoolStrategy
    {
        private List<String> usePools;

        public void addUsePool( String id )
        {
            if ( usePools == null)
            {
                usePools = new LinkedList<>();
            }

            usePools.add( id );
        }
    }

    public static class SelectionStrategy
        extends PoolStrategy
    {
        public enum Type
        {
            ASCENDING,
            DESCENDING,
            RANDOM;
        }

        private Type type;

        public Type getType()
        {
            return type;
        }

        public void set( String type )
        {
            this.type = Type.valueOf( type.toUpperCase() );
        }
    }

    public static class DepletionStrategy
        extends PoolStrategy
    {
        public enum Type
        {
            FAIL,
            RANDOM;
        }

        private Type type;

        public Type getType()
        {
            return type;
        }

        public void set( String type )
        {
            this.type = Type.valueOf( type.toUpperCase() );
        }
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
    private SelectionStrategy selectionStrategy;
    private DepletionStrategy depletionStrategy;
    private int basePort;
    private int minPort;
    private int maxPort;
    private Ports ports;

    public String getName()
    {
        return name;
    }

    public SelectionStrategy getSelectionStrategy()
    {
        return selectionStrategy;
    }

    public DepletionStrategy getDepletionStrategy()
    {
        return depletionStrategy;
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
