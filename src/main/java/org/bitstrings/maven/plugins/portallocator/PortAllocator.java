package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class PortAllocator
{
    public static class Strategy
    {
        public enum Order
        {
            ASCENDING, DESCENDING, RANDOM;
        }

        public enum DepletionAction
        {
            CONTINUE, FAIL;
        }

        private Order order;
        private DepletionAction depletionAction;

        public Order getOrder()
        {
            return order;
        }

        public DepletionAction getDepletionAction()
        {
            return depletionAction;
        }
    }

    public static class PreferredPorts
    {
        private final List<String> portsList = new LinkedList<String>();

        public void addPorts( String ports )
        {
            portsList.add( ports );
        }

        public void set( String ports )
        {
            portsList.add( ports );
        }
    }

    private String id;
    private PreferredPorts preferredPorts;
    private Strategy strategy;

    public String getId()
    {
        return id;
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
