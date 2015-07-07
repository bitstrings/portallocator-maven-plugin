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

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bitstrings.maven.plugins.portallocator.PortAllocatorService.PortRange;

import mockit.Mock;
import mockit.MockUp;

public class PortAllocatorServiceMock
    extends MockUp<PortAllocatorService>
{
    private final Set<PortRange> unavailablePorts = new LinkedHashSet<>();

    public PortAllocatorServiceMock() {}

    public PortAllocatorServiceMock( PortRange... portRanges )
    {
        addUnavailablePorts( portRanges );
    }

    public PortAllocatorServiceMock addUnavailablePorts( PortRange... portRanges )
    {
        Collections.addAll( unavailablePorts, portRanges );

        return this;
    }

    public Set<PortRange> availablePorts()
    {
        return unavailablePorts;
    }

    @Mock
    protected boolean isPortAllocatable( int port )
        throws IOException
    {
        for ( PortRange portRange : unavailablePorts )
        {
            if ( portRange.isInRange( port ) )
            {
                return false;
            }
        }

        return true;
    }
}
