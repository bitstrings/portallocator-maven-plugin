package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.LinearSelectorStrategy.Order.*;

public class LinearSelectorStrategy
    implements SelectorStrategy
{
    public enum Order
    {
        ASCENDING
        {
            @Override
            protected int next( int reference )
            {
                return ( reference + 1 );
            }
        },
        DESCENDING
        {
            @Override
            protected int next( int reference )
            {
                return ( reference - 1 );
            }
        };

        protected abstract int next( int reference );
    }

    private final Order order;

    public LinearSelectorStrategy()
    {
        this( ASCENDING );
    }

    public LinearSelectorStrategy( Order order )
    {
        this.order = order;
    }

    @Override
    public int choose( int lowest, int highest, int lastSelected )
        throws SelectionOutOfBounds
    {
        final int next = order.next( lastSelected );

        if ( ( next < lowest ) || ( next > highest ) )
        {
            throw new SelectionOutOfBounds(
                "Next value [ " + next + " ] "
                    + "is out of bounds [ " + lowest + ", " + highest + " ] "
                    + "in " + order.toString() + " order." );
        }

        return next;
    }
}
