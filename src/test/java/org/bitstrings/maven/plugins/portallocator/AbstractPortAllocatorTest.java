/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitstrings.maven.plugins.portallocator;

import static org.bitstrings.maven.plugins.portallocator.util.TestUtils.*;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Rule;

public class AbstractPortAllocatorTest
{
    @Rule
    public MojoRule rule = new MojoRule();

    @Rule
    public TestResources resources = new TestResources();

    protected PortAllocatorServiceMock portAllocatorServiceMock = new PortAllocatorServiceMock();

    public MavenProject createNewMavenProject()
        throws Exception
    {
        return rule.readMavenProject( resources.getBasedir( "base" ) );
    }

    public MavenProject executeMojo( String... params )
        throws Exception
    {
        return executeMojo( createNewMavenProject(), params );
    }

    public MavenProject executeMojo( MavenProject project, String... params )
        throws Exception
    {
        Xpp3Dom[] doms = new Xpp3Dom[ params.length ];

        for ( int i = 0; i < params.length; i++ )
        {
            doms[ i ] = domFromString( params[ i ] );
        }

        rule.executeMojo( project, "allocate", doms );

        return project;
    }
}
