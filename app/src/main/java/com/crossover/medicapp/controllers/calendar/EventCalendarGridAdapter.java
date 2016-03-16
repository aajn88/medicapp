package com.crossover.medicapp.controllers.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crossover.common.model.common.Event;
import com.crossover.medicapp.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * This adapter is to create events in the calendar
 *
 * @author <a href="mailto:antonio-jimenez@accionplus.com">Antonio A. Jimenez N.</a>
 */
public class EventCalendarGridAdapter extends CaldroidGridAdapter {

    /** Max qty of events **/
    private static final int MAX_EVENTS = 3;

    /** LayoutInflater **/
    private LayoutInflater mInflater;

    /** Manager de eventos **/
    private EventCalendarFragment.ICalendarEventsListener mListener;

    /** Main Activity **/
    private Activity mActivity;

    /**
     * Constructor
     *
     * @param activity
     *         Main Activity
     * @param month
     *         Month to be shown
     * @param year
     *         Year to be shown
     * @param caldroidData
     *         CaldroidCalendar data
     * @param extraData
     *         extra Data
     */
    public EventCalendarGridAdapter(Activity activity, int month, int year,
                                    HashMap<String, Object> caldroidData,
                                    HashMap<String, Object> extraData,
                                    EventCalendarFragment.ICalendarEventsListener mListener) {
        super(activity, month, year, caldroidData, extraData);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListener = mListener;
    }

    /**
     * This method intializes the event view
     *
     * @param event
     *         Event source of the view
     * @param isBig
     *         True for a big event view. False for a standard view
     *
     * @return The event view
     */
    static View createEventView(Event event, LayoutInflater mInflater, boolean isBig) {
        int drawable = new Date().compareTo(event.getStartDate()) < 0 ? R.drawable.blue_event :
                R.drawable.green_event;

        View view = mInflater
                .inflate(isBig ? R.layout.big_event_item : R.layout.event_item, null, false);
        view.findViewById(R.id.background_ll).setBackgroundResource(drawable);
        TextView eventTextTv = (TextView) view.findViewById(R.id.event_text_rtv);
        eventTextTv.setText(event.getName());

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;

        // For reuse
        if (cellView == null) {
            cellView = mInflater.inflate(R.layout.event_calendar_item, parent, false);
        }

        initViews(position, cellView);

        showEvents(position, cellView);

        return cellView;
    }

    /**
     * This method shows the events in the Calendar
     *
     * @param position
     *         Position at the day in the Calendar
     * @param cellView
     *         Root View
     */
    protected void showEvents(int position, View cellView) {
        LinearLayout cellLl = (LinearLayout) cellView.findViewById(R.id.events_ll);
        ImageView ellipsisIv = (ImageView) cellView.findViewById(R.id.ellipsis_iv);
        ellipsisIv.setVisibility(View.GONE);
        cellLl.removeAllViews();
        final DateTime dateTime = this.datetimeList.get(position);
        if (mListener == null) {
            cellLl.setOnClickListener(null);
            return;
        }
        List<Event> events = mListener
                .getDayEvents(dateTime.getYear(), dateTime.getMonth() - 1, dateTime.getDay());
        if (events == null || events.isEmpty()) {
            return;
        }

        int size = Math.min(events.size(), MAX_EVENTS);

        if (events.size() > MAX_EVENTS) {
            ellipsisIv.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < size; i++) {
            showEvents(cellLl, events.get(i));
        }

    }

    /**
     * This method adds the event view
     *
     * @param root
     *         Root view
     * @param event
     *         Event to be added
     */
    private void showEvents(ViewGroup root, Event event) {
        View eventView = createEventView(event, mInflater, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 2);
        root.addView(eventView, params);
    }

    /**
     * This method inits the views
     *
     * @param position
     *         Position to be shown
     * @param cellView
     *         The cell view
     */
    protected void initViews(int position, View cellView) {
        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);

        tv1.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        setColorPreviousNextDates(tv1, dateTime, resources);

        boolean shouldResetDisabledView;
        boolean shouldResetSelectedView;

        shouldResetDisabledView = customizedForDisabledDates(cellView, tv1, dateTime, false);

        shouldResetSelectedView = customizeForSelectedDates(cellView, tv1, dateTime, resources,
                false);

        customizeForToday(cellView, dateTime, shouldResetDisabledView, shouldResetSelectedView);

        tv1.setText(Integer.toString(dateTime.getDay()));

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);
    }

    protected void setColorPreviousNextDates(TextView tv1, DateTime dateTime,
                                             Resources resources) {// Set color of the dates in
        // previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources.getColor(com.caldroid.R.color.caldroid_darker_gray));
        }
    }

    protected boolean customizedForDisabledDates(View cellView, TextView tv1, DateTime dateTime,
                                                 boolean shouldResetDiabledView) {// Customize
        // for disabled dates and date outside min/max
        // dates
        if ((minDateTime != null && dateTime.lt(minDateTime)) ||
                (maxDateTime != null && dateTime.gt(maxDateTime)) ||
                (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }
        return shouldResetDiabledView;
    }

    protected boolean customizeForSelectedDates(View cellView, TextView tv1, DateTime dateTime,
                                                Resources resources,
                                                boolean shouldResetSelectedView) {// Customize
        // for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(resources.getColor(com.caldroid.R.color.caldroid_sky_blue));

            tv1.setTextColor(Color.BLACK);

        } else {
            shouldResetSelectedView = true;
        }
        return shouldResetSelectedView;
    }

    protected void customizeForToday(View cellView, DateTime dateTime,
                                     boolean shouldResetDiabledView,
                                     boolean shouldResetSelectedView) {
        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
        }
    }
}
