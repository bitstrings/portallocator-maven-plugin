package org.bitstrings.maven.plugins.portallocator;

public class Port
{
    private String name;
    private Integer offsetBasePort;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Integer getOffsetBasePort()
    {
        return offsetBasePort;
    }

    public void setOffsetBasePort( Integer offsetBasePort )
    {
        this.offsetBasePort = offsetBasePort;
    }

    public void set( String name )
    {
        setName( name );
    }
}
