package com.crossover.persistence.managers.impl;

import android.util.Log;

import com.crossover.common.model.common.User;
import com.crossover.common.model.utils.StringUtils;
import com.crossover.persistence.DatabaseHelper;
import com.crossover.persistence.managers.api.IUsersManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.SQLException;

/**
 * This is the implementation of the {@link IUsersManager} interface where users manager methods are
 * exposed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
public class UsersManager extends CrudManager<User, Integer> implements IUsersManager {

    /** Logs Tag **/
    private static final String TAG_LOG = UsersManager.class.getName();

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
    public UsersManager(DatabaseHelper helper) throws SQLException {
        super(helper, User.class);
    }

    /**
     * This method finds a User by its username
     *
     * @param username
     *         The username
     *
     * @return A user if there is match. Otherwise returns null
     */
    @Override
    public User findByUsername(String username) {
        User user = null;

        try {
            user = getDao().queryBuilder().where().eq(User.USERNAME_COLUMN, username)
                    .queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG_LOG, StringUtils
                            .format("An error occurred while finding a user by its username %s",
                                    username), e);
        }

        return user;
    }

    /**
     * This method finds the active user
     *
     * @return The current active user. Otherwise returns null
     */
    @Override
    public User findActiveUser() {
        User user = null;

        try {
            user = getDao().queryBuilder().where().eq(User.ACTIVE_COLUMN, true).queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG_LOG, "An error occurred while finding the active user", e);
        }

        return user;
    }

}
