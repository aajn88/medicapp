package com.crossover.persistence.managers.impl;

import android.util.Log;

import com.crossover.common.model.common.Event;
import com.crossover.persistence.DatabaseHelper;
import com.crossover.persistence.managers.api.IEventsManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the interface {@link IEventsManager} that exposes all available methods for
 * Events management in the DB
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
public class EventsManager extends CrudManager<Event, Integer> implements IEventsManager {

    /** Tag for logs **/
    private static final String TAG_LOG = EventsManager.class.getName();

    /**
     * This is the main constructor of the CrudManager
     *
     * @param helper
     *         The DBHelper
     *
     * @throws SQLException
     *         If there's an error creating the Entity's DAO
     */
    @Inject
    public EventsManager(DatabaseHelper helper) throws SQLException {
        super(helper, Event.class);
    }

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
    @Override
    public List<Event> findBetweenDates(Date start, Date end) {
        List<Event> events = null;

        try {
            events = getDao().queryBuilder().where().between(Event.START_DATE_COLUMN, start, end)
                    .query();
        } catch (SQLException e) {
            Log.e(TAG_LOG, "An error occurred while finding events between dates", e);
        }

        return events;
    }
}
