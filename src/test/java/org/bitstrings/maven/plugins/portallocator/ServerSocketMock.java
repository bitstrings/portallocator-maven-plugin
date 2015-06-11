package org.bitstrings.maven.plugins.portallocator;

import java.net.ServerSocket;

import mockit.MockUp;

public class ServerSocketMock
    extends MockUp<ServerSocket>
{
    public void $init( int port )
    {
        System.out.println( "hello port: " + port );
    }
}
