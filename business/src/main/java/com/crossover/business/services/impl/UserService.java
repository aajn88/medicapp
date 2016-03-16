package com.crossover.business.services.impl;

import com.crossover.business.services.api.IUserService;
import com.crossover.common.model.common.User;
import com.crossover.persistence.managers.api.IUsersManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

/**
 * The implementation of the interface {@link IUserService} for users management
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
public class UserService implements IUserService {

    /** Users Manager **/
    @Inject
    private IUsersManager mUsersManager;

    /**
     * This method finds a user by his Id
     *
     * @param id
     *         The user id
     *
     * @return The user if there is a match. Otherwise returns null
     */
    @Override
    public User findUserById(int id) {
        return mUsersManager.findById(id);
    }

    /**
     * This method returns the list of all available users
     *
     * @return List of users
     */
    @Override
    public List<User> getAllUsers() {
        return mUsersManager.all();
    }

}
