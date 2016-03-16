package com.crossover.business.services.impl;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.common.model.common.Event;
import com.crossover.persistence.managers.api.IEventsManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.commons.lang3.Validate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The implementation of the exposed services in {@link ICalendarService}
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
public class CalendarService implements ICalendarService {

    /** Events manager **/
    @Inject
    private IEventsManager mEventsManager;

    /**
     * This method retrieves the month events
     *
     * @param month
     *         Month number to be searched (0 - 11)
     * @param year
     *         Year number to be searched
     *
     * @return List of events of the searched month
     */
    @Override
    public List<Event> getDayEvents(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date startDate = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);

        Date endDate = cal.getTime();

        return mEventsManager.findBetweenDates(startDate, endDate);
    }

    /**
     * This method creates an event
     *
     * @param event
     *         The event to be created
     *
     * @return True if it was successfully created. Otherwise returns False
     */
    @Override
    public boolean createEvent(Event event) {
        Validate.notNull(event, "The event cannot be null");
        Validate.notNull(event.getName(), "The event name cannot be null");
        Validate.isTrue(event.getName().trim().length() != 0, "The event name cannot be empty");
        Validate.notNull(event.getStartDate(), "The start date cannot be null");

        return mEventsManager.createOrUpdate(event);
    }

}
