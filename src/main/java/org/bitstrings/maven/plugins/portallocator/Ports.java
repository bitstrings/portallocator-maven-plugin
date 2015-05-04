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
import java.util.LinkedList;
import java.util.List;

import org.bitstrings.maven.plugins.portallocator.util.Helpers;

public class Ports
    implements Iterable<Port>
{
    private String portAllocatorRef;

    private PortAllocator portAllocator;

    private final List<Port> portList = new LinkedList<>();

    public String getPortAllocatorRef()
    {
        return portAllocatorRef;
    }

    public void setPortAllocatorRef( String portAllocatorRef )
    {
        this.portAllocatorRef = portAllocatorRef;
    }

    public PortAllocator getPortAllocator()
    {
        return portAllocator;
    }

    public void setPortAllocator( PortAllocator portAllocator )
    {
        this.portAllocator = portAllocator;
    }

    public List<Port> getPortList()
    {
        return portList;
    }

    public void addPort( Port port )
    {
        portList.add( port );
    }

    public void set( String portNames )
    {
        for ( String portName : Helpers.iterateOnCsv( portNames ) )
        {
            final Port port = new Port();

            port.setName( portName );

            addPort( port );
        }
    }

    @Override
    public Iterator<Port> iterator()
    {
        return portList.iterator();
    }
}
