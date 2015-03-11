package org.bitstrings.maven.plugins.portallocator;

public class PortAllocationExeption
    extends RuntimeException
{
    public PortAllocationExeption( String message, Throwable cause )
    {
        super( message, cause );
    }

    public PortAllocationExeption( String message )
    {
        super( message );
    }

    public PortAllocationExeption( Throwable cause )
    {
        super( cause );
    }
}
