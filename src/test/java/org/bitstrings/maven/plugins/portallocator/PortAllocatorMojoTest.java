package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.maven.project.MavenProject;
import org.bitstrings.maven.plugins.portallocator.PortAllocatorService.PortRange;
import org.bitstrings.test.junit.runner.ClassLoaderPerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( ClassLoaderPerTestRunner.class )
public class PortAllocatorMojoTest
    extends AbstractPortAllocatorTest
{
    private static final String PORTS_OFFSETFROM =
        "<ports>"
            + "<port>"
            + "<name>name1</name>"
            + "</port>"
            + "<port>"
            + "<name>name2</name>"
            + "<preferredPort>8096</preferredPort>"
            + "<setOffsetProperty>true</setOffsetProperty>"
            + "</port>"
            + "<port>"
            + "<name>name3</name>"
            + "<preferredPort>8090</preferredPort>"
            + "<offsetFrom>name2</offsetFrom>"
            + "</port>"
            + "<port>"
            + "<name>name4</name>"
            + "<preferredPort>8100</preferredPort>"
            + "<offsetFrom>name2</offsetFrom>"
            + "</port>"
            + "</ports>";


    @Test
    public void should_changeNameSeparator_when_usingNameSeparator()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<nameSeparator>-</nameSeparator>",
                "<ports>port</ports>"
            );

        assertPropertyEquals( "8090", "port-port", project );
    }

    @Test
    public void should_changePortSuffix_when_usingPortNameSuffix()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<portNameSuffix>different</portNameSuffix>",
                "<ports>port</ports>"
            );

        assertPropertyEquals( "8090", "port.different", project );
    }

    @Test
    public void should_assignPortDefaultPort_when_usingCompactFormAndNoPort()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<ports>compact</ports>"
            );

        assertPropertyEquals( "8090", "compact.port", project );
    }

    @Test
    public void should_assignPort_when_usingCompactFormAndPreferredPort()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<ports>compact:8095</ports>"
            );

        assertPropertyEquals( "8095", "compact.port", project );
    }

    @Test
    public void should_assignMultiPort_when_usingCompactFormAndPreferredPort()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<ports>compact1:8095,compact2:9999,  compact3:8888 </ports>"
            );

        assertPropertyEquals( "8095", "compact1.port", project );
        assertPropertyEquals( "9999", "compact2.port", project );
        assertPropertyEquals( "8888", "compact3.port", project );
    }

    @Test
    public void should_assignPort_when_usingFullFormAndPreferredPort()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                    "<ports>"
                        + "<port>"
                        + "<name>full</name>"
                        + "</port>"
                        + "<port>"
                        + "<name>full-preferred</name>"
                        + "<preferredPort>9191</preferredPort>"
                        + "</port>"
                        + "</ports>"
            );

        assertPropertyEquals( "8090", "full.port", project );
        assertPropertyEquals( "9191", "full-preferred.port", project );
    }

    @Test
    public void should_assignNextAvailablePort_when_usingPortUnavailable()
        throws Exception
    {
        portAllocatorServiceMock.addUnavailablePorts( new PortRange( 8090, 8099 ) );

        MavenProject project =
            executeMojo(
                "<ports>next:8095</ports>"
            );

        assertPropertyNotBetween( 8090, 8099, "next.port", project );
    }

    @Test
    public void should_assignOffset_when_usingCompactForm()
        throws Exception
    {
        portAllocatorServiceMock.addUnavailablePorts( new PortRange( 8090, 8099 ) );

        MavenProject project =
            executeMojo(
                "<ports>compact:8090::true</ports>"
            );

        assertPropertyEquals( "10", "compact.port-offset", project );
    }

    @Test
    public void should_assignOffset_when_usingFullForm()
        throws Exception
    {
        portAllocatorServiceMock.addUnavailablePorts( new PortRange( 8080, 8092 ) );

        MavenProject project =
            executeMojo(
                "<ports>"
                    + "<port>"
                    + "<name>full</name>"
                    + "<preferredPort>8080</preferredPort>"
                    + "<setOffsetProperty>true</setOffsetProperty>"
                    + "</port>"
                    + "</ports>"
            );

        assertPropertyEquals( "13", "full.port-offset", project );
    }

    @Test
    public void should_assignRelativePort_when_usingOffsetFrom()
        throws Exception
    {
        portAllocatorServiceMock.addUnavailablePorts( new PortRange( 8090, 8095 ) );

        MavenProject project = executeMojo( PORTS_OFFSETFROM );

        assertPropertyEquals( "8096", "name1.port", project );
        assertPropertyEquals( "8103", "name2.port", project );
        assertPropertyEquals( "8097", "name3.port", project );
        assertPropertyEquals( "8107", "name4.port", project );
    }

    @Test
    public void should_writePropertiesFile_when_usingWritePropertiesFile()
        throws Exception
    {
        final String testPropsFileName = "test.properties";

        MavenProject project =
            executeMojo(
                PORTS_OFFSETFROM,
                "<writePropertiesFile>" + testPropsFileName + "</writePropertiesFile>"
            );

        final File testPropsFile = new File( project.getBasedir(), testPropsFileName );

        assertTrue( "Port properties file doesn't exist [" + testPropsFile + "].", testPropsFile.exists() );

        Properties testProps = new Properties();

        try ( Reader r = IOUtils.toBufferedReader( new FileReader( testPropsFile ) ) )
        {
            testProps.load( r );
        }

        assertEquals( testProps, project.getProperties() );
    }
}
