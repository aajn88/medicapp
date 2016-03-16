package com.crossover.medicapp.controllers.calendar;

import android.content.Context;
import android.os.Build;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;

import com.crossover.common.model.common.Event;
import com.crossover.medicapp.R;
import com.crossover.common.model.utils.MedicappUtils;
import com.crossover.medicapp.utils.ViewUtils;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class EventCalendarFragment extends CaldroidFragment {

    /** Calendar events listener **/
    private ICalendarEventsListener mListener;

    /** Needed constructor **/
    public EventCalendarFragment() {}

    /**
     * New EventCalendarFragment instance
     *
     * @return New EventCalendarFragment instance
     */
    public static EventCalendarFragment newInstance(ICalendarEventsListener listener) {
        EventCalendarFragment fragment = new EventCalendarFragment();
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        setUpListener();
        return new EventCalendarGridAdapter(getActivity(), month, year, getCaldroidData(),
                extraData, mListener);
    }

    /**
     * This method sets up the Caldroid Listener for events
     */
    private void setUpListener() {
        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                notifyListener(date, view);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                List<Event> events = (mListener != null ? mListener.getDayEvents(year, month, day) :
                        null);
                showDetail(date, events, view);
            }

            @Override
            public void onChangeMonth(int month, int year) {
                mListener.onChangeMonth(month, year);
            }

            @Override
            public void onLongClickDate(Date date, View view) {}

            @Override
            public void onCaldroidViewCreated() {
                Button left = getLeftArrowButton();
                if (left == null) {
                    return;
                }

                left.setBackground(getResources().getDrawable(R.drawable.ic_arrow_back));
                int padding = ViewUtils.dp2Pixels(5, getActivity());
                left.setPadding(padding, padding, padding, padding);
                Button right = getRightArrowButton();
                right.setBackground(getResources().getDrawable(R.drawable.ic_arrow_next));
                right.setPadding(padding, padding, padding, padding);
            }

        };

        // Setup Caldroid
        setCaldroidListener(listener);
    }

    /**
     * This method shows the detail of the day events
     *
     * @param date
     *         Current Date
     * @param events
     *         Events to be shown
     * @param view
     *         Parent View
     */
    private void showDetail(Date date, List<Event> events, View view) {
        CalendarDayDetailFragment detailFragment = CalendarDayDetailFragment
                .newInstance(events, date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            detailFragment.setSharedElementEnterTransition(new EventsDetailTransition());
            detailFragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            detailFragment.setSharedElementReturnTransition(new EventsDetailTransition());
        }

        getActivity().getSupportFragmentManager().beginTransaction()
                .addSharedElement(view, getString(R.string.transition_event))
                .replace(R.id.main_content_rl, detailFragment).addToBackStack(null).commit();
    }

    /**
     * Notify listener of click event
     *
     * @param date
     *         Date of the clicked event
     * @param view
     *         View parent
     */
    protected void notifyListener(Date date, View view) {
        if (mListener == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mListener.onEventDayClickListener(getActivity(), view, year, month, day);
    }

    @Override
    protected void refreshMonthTitleTextView() {
        super.refreshMonthTitleTextView();

        StringBuilder sb = new StringBuilder();
        String tituloMes = new DateFormatSymbols(MedicappUtils.getLocale(getActivity()))
                .getMonths()[getMonth() - 1].toUpperCase();
        sb.append(tituloMes);
        sb.append(" ");
        sb.append(getYear());
        getMonthTitleTextView().setText(sb.toString());
    }

    @Override
    protected ArrayList<String> getDaysOfWeek() {
        ArrayList<String> list = new ArrayList<String>();

        SimpleDateFormat fmt = new SimpleDateFormat("EEE", MedicappUtils.getLocale(getActivity()));

        // 17 Feb 2013 is Sunday
        DateTime sunday = new DateTime(2013, 2, 17, 0, 0, 0, 0);
        DateTime nextDay = sunday.plusDays(startDayOfWeek - SUNDAY);

        for (int i = 0; i < 7; i++) {
            Date date = CalendarHelper.convertDateTimeToDate(nextDay);
            list.add(fmt.format(date).toUpperCase());
            nextDay = nextDay.plusDays(1);
        }

        return list;
    }

    /**
     * This interface will be used when events occur by the user
     */
    public interface ICalendarEventsListener {

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
        List<Event> getDayEvents(int year, int month, int day);

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
        void onEventDayClickListener(Context context, View root, int year, int month, int day);

        /**
         * This method is invoked when a change of month is done
         *
         * @param month
         *         target month
         * @param year
         *         target year
         */
        void onChangeMonth(int month, int year);

    }
}
