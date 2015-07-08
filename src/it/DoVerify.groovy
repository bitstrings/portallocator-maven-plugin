import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import groovy.io.*;

import java.io.*;

def verify( File resultFile, File expectedFile )
{
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
}

basedir.eachDirRecurse(
{
    if ( !it.name.equals( "target" ) )
    {
        return;
    }

    it.eachFileRecurse
    {
        if ( it.isFile() && it.name.equals( "ports.properties" ) )
        {
            verify( it, new File( it.parentFile, "expected.properties" ) );
        }
    }
})
