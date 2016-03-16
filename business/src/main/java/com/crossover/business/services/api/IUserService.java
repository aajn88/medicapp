package com.crossover.business.services.api;

import com.crossover.common.model.common.User;

import java.util.List;

/**
 * This interface exposes the services to manage and consult the Users
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IUserService {

    /**
     * This method finds a user by his Id
     *
     * @param id
     *         The user id
     *
     * @return The user if there is a match. Otherwise returns null
     */
    User findUserById(int id);

    /**
     * This method returns the list of all available users
     *
     * @return List of users
     */
    List<User> getAllUsers();

}
