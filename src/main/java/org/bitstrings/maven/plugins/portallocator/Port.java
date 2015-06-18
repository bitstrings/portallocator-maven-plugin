/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitstrings.maven.plugins.portallocator;

import java.util.Iterator;

import org.apache.commons.lang3.BooleanUtils;
import org.bitstrings.maven.plugins.portallocator.util.Helpers;

public class Port
{
    private String name;
    private Integer PreferredPort;
    private String offsetFrom;
    private Boolean setOffsetProperty;

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

    public Boolean getSetOffsetProperty()
    {
        return setOffsetProperty;
    }

    public void setSetOffsetProperty( Boolean setOffsetProperty )
    {
        this.setOffsetProperty = setOffsetProperty;
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
            String value = components.next();

            if ( !value.isEmpty() )
            {
                setPreferredPort( Integer.parseInt( value ) );
            }
        }

        if ( components.hasNext() )
        {
            String value = components.next();

            if ( !value.isEmpty() )
            {
                setOffsetFrom( value );
            }
        }

        if ( components.hasNext() )
        {
            String value = components.next();

            if ( !value.isEmpty() )
            {
                setSetOffsetProperty( BooleanUtils.toBoolean( value ) );
            }
        }
    }
}
