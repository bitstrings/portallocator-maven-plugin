package org.bitstrings.maven.plugins.portallocator;

import java.util.LinkedList;
import java.util.List;

public class Port
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

    public static class RelativePort
    {
        private String name;
        private int offset;

        public void setName(String name)
        {
            this.name = name;
        }

        public void setOffset(int offset)
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
    private int offsetBasePort;
    private String portNameSuffix;
    private String offsetNameSuffix;
    private String nameLevelSeparator;
    private final List<RelativePort> relativePorts = new LinkedList<>();

    public String getName()
    {
        return name;
    }

    public int getOffsetBasePort()
    {
        return offsetBasePort;
    }

    public String getPortNameSuffix()
    {
        return portNameSuffix;
    }

    public String getOffsetNameSuffix()
    {
        return offsetNameSuffix;
    }

    public String getNameLevelSeparator()
    {
        return nameLevelSeparator;
    }

    public List<RelativePort> getRelativePorts()
    {
        return relativePorts;
    }
}
