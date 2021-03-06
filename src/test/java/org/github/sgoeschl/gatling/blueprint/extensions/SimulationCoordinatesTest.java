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
package org.github.sgoeschl.gatling.blueprint.extensions;

import org.junit.Test;

import static org.github.sgoeschl.gatling.blueprint.extensions.SimulationCoordinates.from;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SimulationCoordinatesTest {

    private static final String SIMULATION_CLASS_NAME = "application.tenant.scenario.Test";
    private static final String[] PATH_ELEMENTS = new String[] { "tenant", "local", "application", "scenario" };

    private final SimulationCoordinates simulationCoordinates = new SimulationCoordinates("application", "tenant", "local", "scenario");

    @Test
    public void shouldCreateSimulationFromSimulationClassName() {
        assertEquals(simulationCoordinates, from(SIMULATION_CLASS_NAME));
    }

    @Test
    public void shouldCreateSimulationFromSimulationClassNameAndProperties() {
        assertEquals(simulationCoordinates, from(SIMULATION_CLASS_NAME, System.getProperties()));
    }

    @Test
    public void shouldConvertToScenarioName() {
        assertEquals("application-tenant-local-scenario", from(SIMULATION_CLASS_NAME).toScenarioName());
    }

    @Test
    public void shouldConvertToString() {
        assertEquals("{application='application', tenant='tenant', site='local', scope='scenario'}",
                from(SIMULATION_CLASS_NAME).toString());
    }

    @Test
    public void shouldGetPathElementsFromClassName() {
        assertArrayEquals(PATH_ELEMENTS, from(SIMULATION_CLASS_NAME).getPathElements());
    }


}
