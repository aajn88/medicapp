package com.crossover.medicapp.controllers.calendar;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.common.model.common.Event;
import com.crossover.medicapp.R;
import com.google.inject.Inject;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.List;

import roboguice.fragment.RoboFragment;

/**
 * A simple {@link Fragment} subclass. Use the {@link MainCalendarFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class MainCalendarFragment extends RoboFragment
        implements EventCalendarFragment.ICalendarEventsListener {

    /** Caldroid Calendar Saved State **/
    private static final String CALDROID_SAVED_STATE = "CALDROID_SAVED_STATE";

    /** Calendar Fragment **/
    private CaldroidFragment mCalendarFragment;

    /** Calendar Service **/
    @Inject
    private ICalendarService mCalendarService;

    /** Required empty public constructor **/
    public MainCalendarFragment() {}

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @return A new instance of fragment MainCalendarFragment.
     */
    public static MainCalendarFragment newInstance() {
        MainCalendarFragment fragment = new MainCalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mCalendarFragment = EventCalendarFragment.newInstance(this);

        if (savedInstanceState != null) {
            mCalendarFragment.restoreStatesFromKey(savedInstanceState, CALDROID_SAVED_STATE);
        } else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, Calendar.MONDAY);
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            mCalendarFragment.setArguments(args);
        }

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.calendario_ll, mCalendarFragment);
        ft.commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mCalendarFragment != null) {
            mCalendarFragment.saveStatesToKey(outState, CALDROID_SAVED_STATE);
        }
    }

    /**
     * This method returns the day events
     *
     * @param year
     *         the year
     * @param month
     *         the month (0 - 11)
     * @param day
     *         the day of month
     *
     * @return List of events of the requested day
     */
    @Override
    public List<Event> getDayEvents(int year, int month, int day) {
        return mCalendarService.getDayEvents(year, month, day);
    }

    /**
     * This method is called when a event day is clicked
     *
     * @param context
     *         Current context
     * @param root
     *         Root view
     * @param year
     *         Clicked year
     * @param month
     *         Clicked month (0 - 11)
     * @param day
     *         Clicked day
     */
    @Override
    public void onEventDayClickListener(Context context, View root, int year, int month, int day) {
        // I developed this method in case someone in the future could need it
    }

    /**
     * This method is invoked when a change of month is done
     *
     * @param month
     *         target month
     * @param year
     *         target year
     */
    @Override
    public void onChangeMonth(int month, int year) {
        // I developed this method in case someone in the future could need it
    }
}
