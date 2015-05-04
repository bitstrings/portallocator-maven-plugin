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

    public void set( String preferredPortsStr )
    {
        final PreferredPorts preferredPorts = new PreferredPorts();

        preferredPorts.addPorts( preferredPortsStr );

        setPreferredPorts( preferredPorts );
    }
}
