package org.bitstrings.maven.plugins.portallocator;

import java.util.Iterator;

import org.apache.commons.lang3.BooleanUtils;
import org.bitstrings.maven.plugins.portallocator.util.Helpers;

public class Port
{
    private String name;
    private Integer PreferredPort;
    private Boolean setOffset;
    private String offsetFrom;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Integer getPreferredPort()
    {
        return PreferredPort;
    }

    public void setPreferredPort( Integer preferredPort )
    {
        PreferredPort = preferredPort;
    }

    public Boolean getSetOffset()
    {
        return setOffset;
    }

    public void setSetOffset( Boolean setOffset )
    {
        this.setOffset = setOffset;
    }

    public String getOffsetFrom()
    {
        return offsetFrom;
    }

    public void setOffsetFrom( String offsetFrom )
    {
        this.offsetFrom = offsetFrom;
    }

    public void set( String portExpr )
    {
        final Iterator<String> components = Helpers.iterateOnSplit( portExpr, ":", false ).iterator();

        setName( components.next() );

        if ( components.hasNext() )
        {
            setPreferredPort( Integer.parseInt( components.next() ) );
        }

        if ( components.hasNext() )
        {
            setSetOffset( BooleanUtils.toBoolean( components.next() ) );
        }

        if ( components.hasNext() )
        {
            setOffsetFrom( components.next() );
        }
    }
}
