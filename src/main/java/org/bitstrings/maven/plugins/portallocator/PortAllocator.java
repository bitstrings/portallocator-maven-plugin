package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class PortAllocator
{
    public static class PreferredPorts
    {
        private final List<String> portsList = new LinkedList<>();

        public void addPorts( String ports )
        {
            portsList.add( ports );
        }

        public void set( String ports )
        {
            portsList.add( ports );
        }

        public List<String> getPortsList()
        {
            return portsList;
        }
    }

    public static enum DepletedAction
    {
        CONTINUE, FAIL;
    }

    private String id;
    private PreferredPorts preferredPorts;
    private DepletedAction depletedAction;
    private boolean permitOverride;

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public PreferredPorts getPreferredPorts()
    {
        return preferredPorts;
    }

    public void setPreferredPorts( PreferredPorts preferredPorts )
    {
        this.preferredPorts = preferredPorts;
    }

    public DepletedAction getDepletedAction()
    {
        return depletedAction;
    }

    public void setDepletedAction( DepletedAction depletedAction )
    {
        this.depletedAction = depletedAction;
    }

    public boolean isPermitOverride()
    {
        return permitOverride;
    }

    public void setPermitOverride( boolean permitOverride )
    {
        this.permitOverride = permitOverride;
    }
}
