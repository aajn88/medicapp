package com.crossover.common.model.common;

import com.j256.ormlite.field.DatabaseField;

/**
 * This is the invitation to an event
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class Invitation {

    /** User id **/
    public static final String USER_ID_COLUMN = "user_id";

    /** Event id **/
    public static final String EVENT_ID_COLUMN = "event_id";

    /** Accepted **/
    public static final String ACCEPTED_COLUMN = "accepted";

    /** Invitation id **/
    @DatabaseField(generatedId = true)
    private Integer id;

    /** User **/
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private User user;

    /** Event **/
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Event event;

    /** Accepted **/
    @DatabaseField(canBeNull = false)
    private boolean accepted;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @return user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @return event the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * @return the accepted
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * @return accepted the accepted to set
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
