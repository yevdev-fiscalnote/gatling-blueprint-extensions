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

import org.github.sgoeschl.gatling.blueprint.extensions.utils.Validate;

import java.util.Objects;
import java.util.Properties;

/**
 * Captures the coordinates of a Gatling simulation (test), similar to Maven coordinates.
 */
public class SimulationCoordinates {

    private static final int NR_OF_STRING_PARTS = 4;
    private static final String SITE_DEFAULT = "local";
    private static final String SIMULATION_COORDINATES_PROPERTY_KEY = "simulation.coordinates";

    /** The application under test, e.g. "copmuterdatabase" */
    private final String application;

    /** The tenant used for testing, e.g. "gatling" */
    private final String tenant;

    /** The staging site, e.g. "local, "dev", "uat", "prod" */
    private final String site;

    /** The scope of testing, e.g. "smoketest", "loadtest" */
    private final String scope;

    public static SimulationCoordinates from(Object object) {
        Validate.notNull(object, "object");

        return from(getClassName(object), System.getProperties());
    }

    public static SimulationCoordinates from(Object object, Properties properties) {
        Validate.notNull(object, "object");
        Validate.notNull(properties, "properties");

        return from(getClassName(object), properties);
    }

    public SimulationCoordinates(String application, String tenant, String site, String scope) {
        Validate.notEmpty(application, "application");
        Validate.notEmpty(tenant, "tenant");
        Validate.notEmpty(site, "site");
        Validate.notEmpty(scope, "scope");

        this.application = normalize(application);
        this.tenant = normalize(tenant);
        this.site = normalize(site);
        this.scope = normalize(scope);
    }

    public String getApplication() {
        return application;
    }

    public String getTenant() {
        return tenant;
    }

    public String getSite() {
        return site;
    }

    public String getScope() {
        return scope;
    }

    public String toScenarioName() {
        return getApplication() + "-" + getTenant() + "-" + getSite() + "-" + getScope();
    }

    public String[] getPathElements() {
        return new String[] { tenant, site, application, scope };
    }

    @Override
    public String toString() {
        return "{" +
                "application='" + getApplication() + '\'' +
                ", tenant='" + getTenant() + '\'' +
                ", site='" + getSite() + '\'' +
                ", scope='" + getScope() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SimulationCoordinates that = (SimulationCoordinates) o;
        return Objects.equals(application, that.application) &&
                Objects.equals(tenant, that.tenant) &&
                Objects.equals(site, that.site) &&
                Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application, tenant, site, scope);
    }

    private static SimulationCoordinates from(String simulationClassName, Properties properties) {
        if (properties.containsKey(SIMULATION_COORDINATES_PROPERTY_KEY)) {
            return fromSimulationCoordinatesName(properties.getProperty(SIMULATION_COORDINATES_PROPERTY_KEY));
        } else {
            return fromSimulationClassName(simulationClassName, getSiteFrom(properties));
        }
    }

    private static SimulationCoordinates fromSimulationCoordinatesName(String value) {
        final String[] parts = value.split("-");
        final int length = parts.length;

        Validate.isTrue(length == NR_OF_STRING_PARTS, "Expecting exactly four parts: " + value);

        final String application = parts[0];
        final String tenant = parts[1];
        final String site = parts[2];
        final String scope = parts[3];

        return new SimulationCoordinates(application, tenant, site, scope);
    }

    private static SimulationCoordinates fromSimulationClassName(String simulationClassName, String site) {
        final String[] parts = simulationClassName.split("\\.");
        final int length = parts.length;

        Validate.isTrue(length >= NR_OF_STRING_PARTS, "Expecting at least four parts: " + simulationClassName);

        final String application = parts[length - 4];
        final String tenant = parts[length - 3];
        final String scenario = parts[length - 2];

        return new SimulationCoordinates(application, tenant, site, scenario);
    }

    /**
     * Take the class name or the string value,
     */
    private static String getClassName(Object object) {
        if (object == null) {
            return "";
        }

        return object instanceof String ? object.toString() : object.getClass().getName();
    }

    private static String getSiteFrom(Properties properties) {
        return properties.getProperty("site", SITE_DEFAULT);
    }

    private static String normalize(String value) {
        Validate.notEmpty(value, "value");

        return value.toLowerCase().trim();
    }
}
