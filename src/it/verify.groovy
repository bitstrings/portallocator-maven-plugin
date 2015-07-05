import java.io.*;
import java.nio.file.Files;

import org.bitstrings.maven.plugins.portallocator.util.TestUtils;

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

TestUtils.assertPropertiesContainsEntries( expected, result );
