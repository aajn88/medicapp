package com.crossover.business.config;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.business.services.api.ISessionService;
import com.crossover.business.services.impl.CalendarService;
import com.crossover.business.services.impl.SessionService;
import com.crossover.persistence.managers.api.IEventsManager;
import com.crossover.persistence.managers.api.IUsersManager;
import com.crossover.persistence.managers.impl.EventsManager;
import com.crossover.persistence.managers.impl.UsersManager;
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
        bindManagers();
    }

    /**
     * This method binds all the exposed services
     */
    private void bindServices() {
        bind(ISessionService.class).to(SessionService.class);
        bind(ICalendarService.class).to(CalendarService.class);
    }

    /**
     * This method binds all the exposed managers
     */
    private void bindManagers() {
        bind(IUsersManager.class).to(UsersManager.class);
        bind(IEventsManager.class).to(EventsManager.class);
    }

}
