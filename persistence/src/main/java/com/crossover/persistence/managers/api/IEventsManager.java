package com.crossover.persistence.managers.api;

import com.crossover.common.model.common.Event;

import java.util.Date;
import java.util.List;

/**
 * The methods exposed to manage events
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IEventsManager extends ICrudManager<Event, Integer> {

    /**
     * This method finds events that *starts* between given two dates
     *
     * @param start
     *         Start date (inclusive)
     * @param end
     *         End date (inclusive)
     *
     * @return List of events that starts between the given dates
     */
    List<Event> findBetweenDates(Date start, Date end);

}
