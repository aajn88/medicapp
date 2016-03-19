package com.crossover.business.services.impl;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.business.services.api.ISessionService;
import com.crossover.common.model.common.Event;
import com.crossover.common.model.common.Invitation;
import com.crossover.common.model.common.User;
import com.crossover.persistence.managers.api.IEventsManager;
import com.crossover.persistence.managers.api.IInvitationsManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /** Invitations manager **/
    @Inject
    private IInvitationsManager mInvitationsManager;

    /** ISession service **/
    @Inject
    private ISessionService mSessionService;

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

        User currentUser = mSessionService.getCurrentSession();
        List<Event> todayEvents = mEventsManager.findBetweenDates(startDate, endDate);
        List<Event> userEvents = new ArrayList<>();

        for (Event event : todayEvents) {
            Invitation userInvitation = mInvitationsManager
                    .findByEventIdAndUserId(event.getId(), currentUser.getId());
            if (userInvitation != null && userInvitation.isAccepted()) {
                userEvents.add(event);
            }
        }

        return userEvents;
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

        User currentUser = mSessionService.getCurrentSession();

        boolean created = mEventsManager.createOrUpdate(event);
        if (!created || event.getAttendantsIds() == null) {
            return created;
        }

        Set<Integer> invitations = new HashSet<Integer>();
        for (Integer userId : event.getAttendantsIds()) {
            Invitation invitation = mInvitationsManager
                    .findByEventIdAndUserId(event.getId(), userId);
            if (invitation == null) {
                invitation = new Invitation(userId, event.getId());
                invitation.setAccepted((int) userId == currentUser.getId());
                mInvitationsManager.createOrUpdate(invitation);
            }
            invitations.add(userId);
        }

        List<Invitation> allInvitations = mInvitationsManager.findByEventId(event.getId());
        for (Invitation invitation : allInvitations) {
            if (!invitations.contains(invitation.getUserId())) {
                mInvitationsManager.deleteById(invitation.getId());
            }
        }

        return true;
    }

    /**
     * This method finds an event by its Id
     *
     * @param eventId
     *         The event Id
     *
     * @return The requested event. Null if it does not exist
     */
    @Override
    public Event findEventById(int eventId) {
        return mEventsManager.findById(eventId);
    }

    /**
     * This method gets the user's pending invitations
     *
     * @return List of pending events to be accepted
     */
    @Override
    public List<Event> getPendingInvitations() {
        User currentUser = mSessionService.getCurrentSession();
        List<Invitation> invitations = mInvitationsManager
                .findByUserIdAndAccepted(currentUser.getId(), false);
        List<Event> events = new ArrayList<>(invitations.size());
        for (Invitation invitation : invitations) {
            events.add(mEventsManager.findById(invitation.getEventId()));
        }
        return events;
    }

    /**
     * This method accepts/rejects an invitation
     *
     * @param eventId
     *         The eventId invitation
     * @param accept
     *         Accepts/rejects the invitation
     */
    @Override
    public boolean acceptRejectInvitation(int eventId, boolean accept) {
        User currentUser = mSessionService.getCurrentSession();
        Invitation invitation = mInvitationsManager
                .findByEventIdAndUserId(eventId, currentUser.getId());
        if (invitation == null) {
            return false;
        }

        if (accept) {
            invitation.setAccepted(true);
            return mInvitationsManager.createOrUpdate(invitation);
        }

        mInvitationsManager.deleteById(invitation.getId());

        Event event = mEventsManager.findById(eventId);
        List<Integer> newAttendants = new ArrayList<>();
        for (Integer id : event.getAttendantsIds()) {
            if (!currentUser.getId().equals(id)) {
                newAttendants.add(id);
            }
        }

        Integer[] finalAttnds = new Integer[newAttendants.size()];
        event.setAttendantsIds(newAttendants.toArray(finalAttnds));
        return mEventsManager.createOrUpdate(event);
    }

}
