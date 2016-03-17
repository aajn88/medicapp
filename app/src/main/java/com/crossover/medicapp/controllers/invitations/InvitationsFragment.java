package com.crossover.medicapp.controllers.invitations;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.common.model.common.Event;
import com.crossover.common.model.constants.Role;
import com.crossover.medicapp.R;
import com.crossover.medicapp.controllers.common.BaseFragment;
import com.crossover.medicapp.custom_views.progress_bars.ProgressWheel;
import com.crossover.medicapp.utils.ViewUtils;
import com.google.inject.Inject;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

import java.util.List;
import java.util.Random;

import roboguice.inject.InjectView;

/**
 * A simple {@link Fragment} subclass. Use the {@link InvitationsFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class InvitationsFragment extends BaseFragment {

    /** Initial delay **/
    private static final int INITIAL_DELAY_MILLIS = 300;

    /** Invitations DynamicListView **/
    @InjectView(R.id.invitations_dlv)
    private DynamicListView mInvitationsDlv;

    /** Loading ProgressView **/
    @InjectView(R.id.loading_pw)
    private ProgressWheel mLoadingPw;

    /** No pending invitations Rtv **/
    @InjectView(R.id.no_invitations_rtv)
    private TextView mNoInvitationsRtv;

    /** Invitations adapter **/
    private InvitationsCardsAdapter mInvitationsAdapter;

    /** Calendar service **/
    @Inject
    private ICalendarService mCalendarService;

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
        return new InvitationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invitations, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoInvitationsRtv.setVisibility(View.GONE);

        new LoadPendingInvitationsAsyncTask().execute();
    }

    /**
     * This method enables/disables the progress wheel and its related views
     */
    private void enableLoading(boolean enable) {
        ViewUtils.enableProgressWheel(mLoadingPw, enable, mInvitationsDlv);
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

    private class LoadPendingInvitationsAsyncTask extends AsyncTask<Void, Void, List<Event>>
            implements OnDismissCallback {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enableLoading(true);
        }

        @Override
        protected List<Event> doInBackground(Void... params) {
            try {
                // Simulating server connection
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return mCalendarService.getPendingInvitations();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            enableLoading(false);

            if (!events.isEmpty()) {
                mNoInvitationsRtv.setVisibility(View.GONE);
                setUpCards(events);
            } else {
                mNoInvitationsRtv.setVisibility(View.VISIBLE);
            }
        }

        private void setUpCards(List<Event> events) {
            mInvitationsAdapter = new InvitationsCardsAdapter(getActivity(), events);
            AnimationAdapter animAdapter = ViewUtils
                    .animateAdapter(Math.abs(new Random().nextInt()),
                            new SwipeDismissAdapter(mInvitationsAdapter, this));
            animAdapter.setAbsListView(mInvitationsDlv);
            animAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
            mInvitationsDlv.setAdapter(animAdapter);
        }

        /**
         * Called when the user has indicated they she would like to dismiss one or
         * more list item positions. When this method is called given positions should be
         * removed from the adapter.
         *
         * @param listView
         *         The originating ListView implementation
         * @param reverseSortedPositions
         *         An array of positions to dismiss, sorted in descending order
         */
        @Override
        public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                mInvitationsAdapter.remove(mInvitationsAdapter.getItem(position));
            }
        }
    }
}
