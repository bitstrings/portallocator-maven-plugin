package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class PortAllocation
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

    public static enum DepletionAction
    {
        CONTINUE, FAIL;
    }

    public static class RelativePort
    {
        private String name;
        private int offset;

        public void setName( String name )
        {
            this.name = name;
        }

        public void setOffset( int offset )
        {
            this.offset = offset;
        }

        public String getName()
        {
            return name;
        }

        public int getOffset()
        {
            return offset;
        }
    }

    private String name;
    private PreferredPorts preferredPorts;
    private DepletionAction depletionAction;
    private Integer offsetBasePort;
    private String portNameSuffix;
    private String offsetNameSuffix;
    private String nameLevelSeparator;
    private final List<RelativePort> relativePorts = new LinkedList<>();

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public PreferredPorts getPreferredPorts()
    {
        return preferredPorts;
    }

    public void setPreferredPorts( PreferredPorts preferredPorts )
    {
        this.preferredPorts = preferredPorts;
    }

    public DepletionAction getDepletionAction()
    {
        return depletionAction;
    }

    public void setDepletionAction( String depletionAction )
    {
        this.depletionAction = DepletionAction.valueOf( depletionAction.toUpperCase() );
    }

    public void setDepletionAction( DepletionAction depletionAction )
    {
        this.depletionAction = depletionAction;
    }

    public Integer getOffsetBasePort()
    {
        return offsetBasePort;
    }

    public void setOffsetBasePort( Integer offsetBasePort )
    {
        this.offsetBasePort = offsetBasePort;
    }

    public String getPortNameSuffix()
    {
        return portNameSuffix;
    }

    public void setPortNameSuffix( String portNameSuffix )
    {
        this.portNameSuffix = portNameSuffix;
    }

    public String getOffsetNameSuffix()
    {
        return offsetNameSuffix;
    }

    public void setOffsetNameSuffix( String offsetNameSuffix )
    {
        this.offsetNameSuffix = offsetNameSuffix;
    }

    public String getNameLevelSeparator()
    {
        return nameLevelSeparator;
    }

    public void setNameLevelSeparator( String nameLevelSeparator )
    {
        this.nameLevelSeparator = nameLevelSeparator;
    }

    public List<RelativePort> getRelativePorts()
    {
        return relativePorts;
    }

    public void addRelativePort( RelativePort relativePort )
    {
        relativePorts.add( relativePort );
    }
}
