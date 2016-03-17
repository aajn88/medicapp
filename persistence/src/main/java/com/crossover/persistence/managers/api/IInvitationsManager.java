package com.crossover.persistence.managers.api;

import com.crossover.common.model.common.Invitation;

import java.util.List;

/**
 * This is the manager for invitations
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IInvitationsManager extends ICrudManager<Invitation, Integer> {

    /**
     * This method finds all invitations of a user
     *
     * @param userId
     *         User Id
     * @param accepted
     *         If the invitations are accepted/unaccepted
     *
     * @return List of user's invitations
     */
    List<Invitation> findByUserIdAndAccepted(int userId, boolean accepted);

    /**
     * This method finds an invitation given the event and user id
     *
     * @param eventId
     *         Event Id
     * @param userId
     *         User id
     *
     * @return Invitation if there is a match. Otherwise returns null
     */
    Invitation findByEventIdAndUserId(int eventId, int userId);

}
