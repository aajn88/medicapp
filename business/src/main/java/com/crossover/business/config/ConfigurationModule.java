package com.crossover.business.config;

import com.google.inject.AbstractModule;

/**
 * This os the Roboguice configuration module. Here all the services, managers, clients, etc. are
 * bind to their interfaces to be used across all the app
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {

        bindServices();

    }

    /**
     * This method binds all the exposed services
     */
    private void bindServices() {

    }

    /**
     * This method binds all the exposed managers
     */
    private void bindManagers() {

    }

}
