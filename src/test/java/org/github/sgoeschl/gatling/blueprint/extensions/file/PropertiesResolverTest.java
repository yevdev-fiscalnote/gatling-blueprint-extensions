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
package org.github.sgoeschl.gatling.blueprint.extensions.file;

import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static org.github.sgoeschl.gatling.blueprint.extensions.file.PropertiesResolver.resolveProperties;
import static org.junit.Assert.assertEquals;

public class PropertiesResolverTest {

    private static final File ROOT_DIRECTORY = new File("./src/test/files");

    private final String[] pathElements = new String[] { "tenant", "site", "application", "scope" };

    @Test
    public void shouldLocateInAscendingOrder() {
        final Properties properties = resolve(pathElements, "environment.properties");

        assertEquals("scope", properties.getProperty("value"));
    }

    @Test
    public void shouldTrimPropertyValues() {
        final Properties properties = resolve(pathElements, "environment.properties");

        assertEquals("value with space", properties.getProperty("value_with_space"));
    }

    private Properties resolve(String[] pathElements, String fileName) {
        return resolveProperties(ROOT_DIRECTORY, pathElements, fileName);
    }

}
