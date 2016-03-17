package com.crossover.medicapp.controllers.common;

import com.crossover.common.model.constants.Role;

/**
 * This interface should be implemented by the classes that wish to be called to set up its views or
 * model based on the user's role
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
interface ISetUpFeaturesByRole {

    /**
     * This method is called to hide features based on the given role
     *
     * @param role
     *         The user's role
     */
    void setUpFeaturesByRole(Role role);

}
