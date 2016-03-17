package com.crossover.persistence.managers.impl;

import android.util.Log;

import com.crossover.common.model.common.Invitation;
import com.crossover.persistence.DatabaseHelper;
import com.crossover.persistence.managers.api.IInvitationsManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.SQLException;
import java.util.List;

/**
 * The implementation of the invitations manager
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
public class InvitationsManager extends CrudManager<Invitation, Integer>
        implements IInvitationsManager {

    /** Tag for Logs **/
    private static final String TAG_LOG = InvitationsManager.class.getName();

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
    public InvitationsManager(DatabaseHelper helper) throws SQLException {
        super(helper, Invitation.class);
    }

    /**
     * This method finds all invitations of a user
     *
     * @param userId
     *         User Id
     * @param accepted
     *         Confirmed/unconfirmed invitation
     *
     * @return List of user's invitations
     */
    @Override
    public List<Invitation> findByUserIdAndAccepted(int userId, boolean accepted) {
        List<Invitation> invitations = null;

        try {
            invitations = getDao().queryBuilder().where().eq(Invitation.USER_ID_COLUMN, userId)
                    .and().eq(Invitation.ACCEPTED_COLUMN, accepted).query();
        } catch (SQLException e) {
            Log.e(TAG_LOG, "An error occurred while finding the invitations of the user", e);
        }

        return invitations;
    }

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
    @Override
    public Invitation findByEventIdAndUserId(int eventId, int userId) {
        Invitation invitation = null;

        try {
            invitation = getDao().queryBuilder().where().eq(Invitation.USER_ID_COLUMN, userId).and()
                    .eq(Invitation.EVENT_ID_COLUMN, eventId).queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG_LOG,
                    "An error occurred while finding the invitations by user id and event Id", e);
        }

        return invitation;
    }
}
