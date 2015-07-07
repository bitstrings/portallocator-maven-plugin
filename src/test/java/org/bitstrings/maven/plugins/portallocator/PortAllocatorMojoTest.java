package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.bitstrings.maven.plugins.portallocator.PortAllocatorService.PortRange;
import org.bitstrings.test.junit.runner.ClassLoaderPerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableMap;

@RunWith( ClassLoaderPerTestRunner.class )
public class PortAllocatorMojoTest
    extends AbstractPortAllocatorTest
{
    private static final String PORTS_OFFSETFROM_XML =
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

        assertPropertiesContainsEntries(
            ImmutableMap.<String, String> builder()
                .put( "compact1.port", "8095" )
                .put( "compact2.port", "9999" )
                .put( "compact3.port", "8888" )
                .build(),
            project
        );
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

        assertPropertiesContainsEntries(
            ImmutableMap.<String, String> builder()
                .put( "full.port", "8090" )
                .put( "full-preferred.port", "9191" )
                .build(),
            project
        );
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

        MavenProject project = executeMojo( PORTS_OFFSETFROM_XML );

        assertPropertiesContainsEntries(
            ImmutableMap.<String, String> builder()
                .put( "name1.port", "8096" )
                .put( "name2.port", "8103" )
                .put( "name3.port", "8097" )
                .put( "name4.port", "8107" )
                .build(),
            project
        );
    }

    @Test
    public void should_writePropertiesFile_when_usingWritePropertiesFile()
        throws Exception
    {
        final String testPropsFileName = "test.properties";

        MavenProject project =
            executeMojo(
                PORTS_OFFSETFROM_XML,
                "<writePropertiesFile>" + testPropsFileName + "</writePropertiesFile>"
            );

        final File testPropsFile = new File( project.getBasedir(), testPropsFileName );

        assertTrue( "Port properties file doesn't exist <" + testPropsFile + ">.", testPropsFile.exists() );

        Properties testProps = new Properties();

        try ( Reader r = IOUtils.toBufferedReader( new FileReader( testPropsFile ) ) )
        {
            testProps.load( r );
        }

        assertEquals( testProps, project.getProperties() );
    }

    @Test
    public void should_allocatePortsInRange_when_usingPortAllocatorShortestForm()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<portAllocators>65000-65004::fail</portAllocators>",
                "<ports>name1,name2,name3,name4,name5</ports>"

            );

        assertPropertiesContainsEntries(
            ImmutableMap.<String, String> builder()
                .put( "name1.port", "65000" )
                .put( "name2.port", "65001" )
                .put( "name3.port", "65002" )
                .put( "name4.port", "65003" )
                .put( "name5.port", "65004" )
                .build(),
            project
        );
    }

    @Test
    public void should_allocatePortsInRange_when_usingMultiplePreferredPortsForSinglePortAllocator()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<portAllocators>65000-65001;5000-5002;9990-9992</portAllocators>",
                "<ports>name1,name2,name3,name4,name5,name6,name7,name8</ports>"

            );

        assertPropertiesContainsEntries(
            ImmutableMap.<String, String> builder()
                .put( "name1.port", "65000" )
                .put( "name2.port", "65001" )
                .put( "name3.port", "5000" )
                .put( "name4.port", "5001" )
                .put( "name5.port", "5002" )
                .put( "name6.port", "9990" )
                .put( "name7.port", "9991" )
                .put( "name8.port", "9992" )
                .build(),
            project
        );
    }

    @Test
    public void should_allocatePortsInRange_when_usingCustomPortAllocator()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<portAllocators>"
                    + "<portAllocator>"
                    + "<id>custom</id>"
                    + "<preferredPorts>7777-7778,8000</preferredPorts>"
                    + "</portAllocator>"
                    + "</portAllocators>",
                "<ports>"
                    + "<portAllocatorRef>custom</portAllocatorRef>"
                    + "<port>name1</port>"
                    + "<port>name2</port>"
                    + "<port>name3</port>"
                    + "</ports>"
            );

        assertPropertiesContainsEntries(
            ImmutableMap.<String, String> builder()
                .put( "name1.port", "7777" )
                .put( "name2.port", "7778" )
                .put( "name3.port", "8000" )
                .build(),
            project
        );
    }

    @Test
    public void should_allocatePortsInRange_when_usingInnerPortAllocator()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<ports>"
                    + "<portAllocator>8000</portAllocator>"
                    + "<port>name1</port>"
                    + "<port>name2</port>"
                    + "<port>name3</port>"
                    + "</ports>"
            );

        assertPropertiesContainsEntries(
            ImmutableMap.<String, String> builder()
                .put( "name1.port", "8000" )
                .put( "name2.port", "8001" )
                .put( "name3.port", "8002" )
                .build(),
            project
        );
    }

    @Test
    public void should_allocatePortsFromMultiPreferred_when_usingInnerPortAllocator()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<ports>"
                    + "<portAllocator>8000,8050,9000</portAllocator>"
                    + "<port>name1</port>"
                    + "<port>name2</port>"
                    + "<port>name3</port>"
                    + "<port>name4</port>"
                    + "</ports>"
            );

        assertPropertiesContainsEntries(
            ImmutableMap.<String, String> builder()
                .put( "name1.port", "8000" )
                .put( "name2.port", "8050" )
                .put( "name3.port", "9000" )
                .put( "name4.port", "9001" )
                .build(),
            project
        );
    }

    @Test( expected = MojoExecutionException.class )
    public void should_failIfNeedMorePortsThanAvailable_when_portAllocatorIsBounded()
        throws Exception
    {
        MavenProject project =
            executeMojo(
                "<portAllocators>"
                    + "<portAllocator>8011::fail</portAllocator>"
                    + "</portAllocators>",
                "<ports>name1,name2</ports>"

            );

        assertPropertyEquals( "8011", "name1.port", project );
    }
}
