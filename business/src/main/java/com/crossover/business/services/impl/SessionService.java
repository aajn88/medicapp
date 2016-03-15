package com.crossover.business.services.impl;

import com.crossover.business.services.api.ISessionService;
import com.crossover.common.model.common.User;
import com.crossover.common.model.utils.CodedUtils;
import com.crossover.persistence.managers.api.IUsersManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.commons.lang3.Validate;


/**
 * This is the implementation of the session service
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
public class SessionService implements ISessionService {

    /** Users Manager **/
    @Inject
    private IUsersManager mUsersManager;

    /** The current user in session **/
    private User mCurrentUser;

    /**
     * This method checks if the given username is available, that means, that there is no user
     * with
     * this username
     *
     * @param username
     *         The username to be checked
     *
     * @return True the username is available. False if username is null, its length is lesser than
     * 6 or the username already exists
     */
    @Override
    public boolean checkAvailableUsername(String username) {
        return !(username == null || username.length() < 6) &&
                mUsersManager.findByUsername(username) == null;
    }

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
    @Override
    public boolean createAccount(User newUser) throws IllegalArgumentException {
        Validate.notNull(newUser, "The user cannot be null");
        Validate.notNull(newUser.getUsername(), "The username cannot be null");
        Validate.notNull(newUser.getPassword(), "The password cannot be null");
        Validate.notNull(newUser.getName(), "The name cannot be null");
        Validate.notNull(newUser.getRole(), "The role cannot be null");
        Validate.isTrue(newUser.getPassword().length() >= 6,
                "The password length cannot be lesser than 6");

        newUser.setUsername(newUser.getUsername().toLowerCase().trim());

        if (!checkAvailableUsername(newUser.getUsername())) {
            return false;
        }

        String pass = newUser.getPassword();
        newUser.setPassword(CodedUtils.buildMd5(pass));
        boolean created = mUsersManager.createOrUpdate(newUser);
        newUser.setPassword(pass);

        return created;
    }

    /**
     * This method returns the current User in session. If there is an active user (user who
     * decided
     * to remember its password) then this user is returned. If there is no active user, then
     * returns null.
     *
     * @return The active User. Otherwise returns null
     */
    @Override
    public User getCurrentSession() {
        if (mCurrentUser == null) {
            mCurrentUser = mUsersManager.findActiveUser();
        }
        return mCurrentUser;
    }

    /**
     * This method does the log in process given a username and a password
     *
     * @param username
     *         The username
     * @param password
     *         The password
     * @param keepSession
     *         Keep session?
     *
     * @return True if login process has been successful. False if the login fails due to incorrect
     * username and/or password
     */
    @Override
    public boolean login(String username, String password, boolean keepSession) {
        Validate.notNull(username, "The username cannot be null");
        Validate.notNull(password, "The password cannot be null");

        if (username.length() == 0 || password.length() == 0) {
            return false;
        }

        User user =
                mUsersManager.findByUsernameAndPassword(username, CodedUtils.buildMd5(password));
        if (user != null) {
            clearActiveSessions();
            activateSession(user, keepSession);
            return true;
        }

        return false;
    }

    /**
     * This method logs the user out.
     *
     * @return True if the user was succesffuly logged out. Otherwise returns False
     */
    @Override
    public boolean logout() {
        mCurrentUser = null;
        clearActiveSessions();
        return true;
    }

    private void activateSession(User user, boolean keepSession) {
        mCurrentUser = user;
        if (keepSession) {
            mCurrentUser.setActive(true);
            mUsersManager.createOrUpdate(mCurrentUser);
        }
    }

    /**
     * This method clears all active session (in order to keep a clean session)
     */
    private void clearActiveSessions() {
        mUsersManager.deactiveUsers();
    }
}
