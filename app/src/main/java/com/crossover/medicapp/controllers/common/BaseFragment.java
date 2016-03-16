package com.crossover.medicapp.controllers.common;

import android.os.Bundle;
import android.view.View;

import com.crossover.business.services.api.ISessionService;
import com.crossover.common.model.common.User;
import com.crossover.common.model.constants.Role;
import com.google.inject.Inject;

import roboguice.fragment.RoboFragment;

/**
 * This is the base fragment that includes the basic operations for every fragment, such as, hide
 * features based on the user role
 *
 * @author <a href="mailto:antonio-jimenez@accionplus.com">Antonio A. Jimenez N.</a>
 */
public abstract class BaseFragment extends RoboFragment {

    /** Session service **/
    @Inject
    protected ISessionService mSessionService;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User currentUser = mSessionService.getCurrentSession();
        if (currentUser != null) {
            setUpFeaturesByRole(currentUser.getRole());
        }
    }

    /**
     * This method is called to hide features based on the given role
     *
     * @param role
     *         The user's role
     */
    protected abstract void setUpFeaturesByRole(Role role);

}
