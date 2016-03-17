package com.crossover.medicapp.controllers.invitations;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crossover.common.model.constants.Role;
import com.crossover.medicapp.R;
import com.crossover.medicapp.controllers.common.BaseFragment;

/**
 * A simple {@link Fragment} subclass. Use the {@link InvitationsFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class InvitationsFragment extends BaseFragment {

    public InvitationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @return A new instance of fragment InvitationsFragment.
     */
    public static InvitationsFragment newInstance() {
        InvitationsFragment fragment = new InvitationsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invitations, container, false);
    }

    /**
     * This method is called to hide features based on the given role
     *
     * @param role
     *         The user's role
     */
    @Override
    public void setUpFeaturesByRole(Role role) {
        // Nothing to be modified
    }
}
