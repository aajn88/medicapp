package com.crossover.persistence.managers.api;

import com.crossover.common.model.common.User;

/**
 * This is the users manager where basic operations and custom queries are exposed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IUsersManager extends ICrudManager<User, Integer> {

    /**
     * This method finds a User by its username
     *
     * @param username
     *         The username
     *
     * @return A user if there is match. Otherwise returns null
     */
    User findByUsername(String username);

    /**
     * This method finds a user by its username and password (hash)
     *
     * @param username
     *         The username
     * @param password
     *         The password (hash)
     *
     * @return The user if exists a match. Otherwise returns null
     */
    User findByUsernameAndPassword(String username, String password);

    /**
     * This method finds the active user
     *
     * @return The current active user. Otherwise returns null
     */
    User findActiveUser();

    /**
     * This method inactives all stored users
     */
    void deactiveUsers();

}
