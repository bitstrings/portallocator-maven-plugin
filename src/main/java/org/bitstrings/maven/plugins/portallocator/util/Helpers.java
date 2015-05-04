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
package org.bitstrings.maven.plugins.portallocator.util;

import com.google.common.base.Splitter;

public final class Helpers
{
    private Helpers() {}

    public static Iterable<String> iterateOnCsv( String csv )
    {
        return iterateOnSplit( csv, "," );
    }

    public static Iterable<String> iterateOnSplit( String text, String separator )
    {
        return iterateOnSplit( text, separator, true );
    }

    public static Iterable<String> iterateOnSplit( String text, String separator, boolean omitEmpty )
    {
        final Splitter splitter = Splitter.on( separator ).trimResults();

        if ( omitEmpty )
        {
            splitter.omitEmptyStrings();
        }

        return splitter.split( text );
    }
}
