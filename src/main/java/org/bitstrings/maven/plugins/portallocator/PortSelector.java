package org.bitstrings.maven.plugins.portallocator;

import java.util.BitSet;

public class PortSelector
{
    private static final int MAX_NUMBER_OF_PORTS = 65536;
    private static final int BLOCK_SIZE_IN_BITS = 16;
    private static final int NUMBER_OF_BLOCKS = ( MAX_NUMBER_OF_PORTS / BLOCK_SIZE_IN_BITS );

    private final BitSet potentialPorts = new BitSet( MAX_NUMBER_OF_PORTS );
//    private final List<Integer> activeBlocks; // sorted
}
