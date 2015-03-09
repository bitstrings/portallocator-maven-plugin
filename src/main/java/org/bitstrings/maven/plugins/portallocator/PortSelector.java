package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class PortSelector
{
    public static enum DepletionAction
    {
        CONTINUE, FAIL;
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
    private DepletionAction depletionAction;

    public String getId()
    {
        return id;
    }

    public PreferredPorts getPreferredPorts()
    {
        return preferredPorts;
    }

    public DepletionAction getDepletionAction()
    {
        return depletionAction;
    }

    public void setDepletionAction( String action )
    {
        depletionAction = DepletionAction.valueOf( action.toUpperCase() );
    }
}
