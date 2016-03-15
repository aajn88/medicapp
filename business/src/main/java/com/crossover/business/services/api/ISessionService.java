package com.crossover.business.services.api;

import com.crossover.common.model.common.User;

/**
 * This is the Session interface where all session basic methods are exposed, such as Log-in,
 * Log-out, create an account and so on.
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface ISessionService {

    /**
     * This method checks if the given username is available, that means, that there is no user with
     * this username
     *
     * @param username
     *         The username to be checked
     *
     * @return True the username is available. False if username is null, its length is lesser than
     * 6 or the username already exists
     */
    boolean checkAvailableUsername(String username);

    /**
     * This method creates a new account. If the user already exists, then returns false. Otherwise
     * returns True if the user was created successfully
     *
     * @param newUser
     *         The user to be created
     *
     * @return True if the user was created. False if the username already exists
     *
     * @throws IllegalArgumentException
     *         If:
     *         <p/>
     *         - Any of the following fields are null: username, password, name and role
     *         <p/>
     *         - username length < 4
     *         <p/>
     *         - Password length < 6
     */
    boolean createAccount(User newUser) throws IllegalArgumentException;

    /**
     * This method returns the current User in session. If there is an active user (user who decided
     * to remember its password) then this user is returned. If there is no active user, then
     * returns null.
     *
     * @return The active User. Otherwise returns null
     */
    User getCurrentSession();

}
