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

    /** Default constructor **/
    public Invitation() {
    }

    /**
     * Constructor for user and event id
     *
     * @param userId
     *         User Id
     * @param eventId
     *         Event Id
     */
    public Invitation(Integer userId, Integer eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    /** Invitation id **/
    @DatabaseField(generatedId = true)
    private Integer id;

    /** User Id **/
    @DatabaseField(canBeNull = false, columnName = USER_ID_COLUMN)
    private Integer userId;

    /** Event **/
    @DatabaseField(canBeNull = false, columnName = EVENT_ID_COLUMN)
    private Integer eventId;

    /** Accepted **/
    @DatabaseField(canBeNull = false, columnName = ACCEPTED_COLUMN)
    private boolean accepted;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *         the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     *         the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the eventId
     */
    public Integer getEventId() {
        return eventId;
    }

    /**
     * @param eventId
     *         the eventId to set
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    /**
     * @return the accepted
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * @param accepted
     *         the accepted to set
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
