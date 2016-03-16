package com.crossover.business.services.api;

import com.crossover.common.model.common.Event;

import java.util.List;

/**
 * The services available to manage and consult the events
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface ICalendarService {

    /**
     * This method retrieves the events of the day
     *
     * @param year
     *         Year number to be searched
     * @param month
     *         Month number to be searched (0 - 11)
     * @param day
     *         The day of the month
     *
     * @return List of events of the searched month
     */
    List<Event> getDayEvents(int year, int month, int day);

    /**
     * This method creates an event
     *
     * @param event
     *         The event to be created
     *
     * @return True if it was successfully created. Otherwise returns False
     */
    boolean createEvent(Event event);

}
