/*
 * Copyright 2014 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugins

import grails.config.Config
import grails.core.ArtefactHandler
import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware
import grails.util.Environment
import groovy.transform.CompileStatic
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware


/**
 * Super class for plugins to implement. Plugin implementations should define the various plugin hooks
 * (doWithSpring, doWithApplicationContext, doWithDynamicMethods etc.)
 *
 * @author Graeme Rocher
 * @since 3.0
 */
@CompileStatic
abstract class Plugin implements GrailsApplicationAware, ApplicationContextAware, PluginManagerAware {

    /**
     * The {@link GrailsApplication} instance
     */
    GrailsApplication grailsApplication
    /**
     * The {@link ApplicationContext} instance
     */
    ApplicationContext applicationContext
    /**
     * The {@link GrailsPlugin} definition for this plugin
     */
    GrailsPlugin plugin
    /**
     * The {@link GrailsPluginManager} instance
     */
    GrailsPluginManager pluginManager

    /**
     * The current Grails {@link Environment}
     */
    Environment environment = Environment.current

    /**
     * @return The {@link Config} instance for this plugin
     */
    Config getConfig() { this.plugin?.pluginConfig ?: grailsApplication.config }
    /**
     * The {@link GrailsPluginManager} instance
     */
    GrailsPluginManager getManager() { pluginManager}

    /**
     * Whether the plugin is enabled
     */
    boolean enabled = true

    /**
     * List of {@link ArtefactHandler} instances provided by this plugin
     */
    final List<ArtefactHandler> artefacts = []

    /**
     * Sub classes should override to provide implementations
     *
     * @return A closure that defines beans to be executed by Spring
     */
    Closure doWithSpring() { null }

    /**
     * Invoked in a phase where plugins can add dynamic methods. Subclasses should override
     */
    void doWithDynamicMethods() {
        // no-op
    }

    /**
     * Invokes once the {@link ApplicationContext} has been refreshed and after {#doWithDynamicMethods()} is invoked. Subclasses should override
     */
    void doWithApplicationContext() {
        // no-op
    }

    /**
     * Invoked when a object this plugin is watching changes
     *
     * @param event The event
     */
    void onChange(Map<String, Object> event) {
        // no-op
    }

    /**
     * Invoked when the application configuration changes
     *
     * @param event The event
     */
    void onConfigChange(Map<String, Object> event) {
        // no-op
    }

    /**
     * Invokes when the {@link ApplicationContext} is closed
     *
     * @param event The event
     */
    void onShutdown(Map<String, Object> event) {
        // no-op
    }
}
