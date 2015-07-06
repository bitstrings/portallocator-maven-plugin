import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import java.io.*;

File resultFile = new File( basedir, "target/ports.properties" );
File expectedFile = new File( basedir, "expected.properties" );

Properties result = new Properties();
resultFile.withInputStream
{
    result.load( it );
}

Properties expected = new Properties();
expectedFile.withInputStream
{
    expected.load( it );
}

assertPropertiesContainsEntries( expected, result );
