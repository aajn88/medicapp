package com.crossover.medicapp.controllers.common;

import android.os.Bundle;

import com.crossover.business.services.api.ISessionService;
import com.crossover.common.model.common.User;
import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;

/**
 * This is the base activity where basic functionalities are added through application activities
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public abstract class BaseActivity extends RoboActionBarActivity implements ISetUpFeaturesByRole {

    /** Session service **/
    @Inject
    protected ISessionService mSessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User currentUser = mSessionService.getCurrentSession();
        if (currentUser != null) {
            setUpFeaturesByRole(currentUser.getRole());
        }
    }
}
