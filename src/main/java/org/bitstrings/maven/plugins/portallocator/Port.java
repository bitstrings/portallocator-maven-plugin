package org.bitstrings.maven.plugins.portallocator;

import java.util.Iterator;

import org.bitstrings.maven.plugins.portallocator.util.Helpers;

public class Port
{
    private String name;
    private Integer offsetBasePort;
    private String offsetFrom;

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

    public String getOffsetFrom()
    {
        return offsetFrom;
    }

    public void setOffsetFrom(String offsetFrom)
    {
        this.offsetFrom = offsetFrom;
    }

    public void set( String portExpr )
    {
        final Iterator<String> components = Helpers.iterateOnSplit( portExpr, ":" ).iterator();

        setName( components.next() );

        if ( components.hasNext() )
        {
            setOffsetBasePort( Integer.parseInt(components.next()) );
        }

        if ( components.hasNext() )
        {
            setOffsetFrom( components.next() );
        }
    }
}
