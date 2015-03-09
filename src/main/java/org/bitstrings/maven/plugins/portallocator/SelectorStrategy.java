package org.bitstrings.maven.plugins.portallocator;

public interface SelectorStrategy
{
    class SelectionOutOfBounds
        extends RuntimeException
    {
        public SelectionOutOfBounds(String message, Throwable cause)
        {
            super(message, cause);
        }

        public SelectionOutOfBounds(String message)
        {
            super(message);
        }

        public SelectionOutOfBounds(Throwable cause)
        {
            super(cause);
        }
    }

    int choose( int lowest, int highest, int lastSelected );
}
