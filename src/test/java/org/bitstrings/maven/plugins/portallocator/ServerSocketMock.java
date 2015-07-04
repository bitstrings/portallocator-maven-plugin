package org.bitstrings.maven.plugins.portallocator;

import java.net.ServerSocket;

import mockit.Mock;
import mockit.MockUp;

public class ServerSocketMock
    extends MockUp<ServerSocket>
{
    @Mock
    public void $init( int port )
    {
    }

    @Mock
    public void close()
    {
    }
}
