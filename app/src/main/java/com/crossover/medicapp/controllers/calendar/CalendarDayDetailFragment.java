package com.crossover.medicapp.controllers.calendar;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.common.model.common.Event;
import com.crossover.common.model.constants.Role;
import com.crossover.common.model.utils.DateUtils;
import com.crossover.common.model.utils.MedicappUtils;
import com.crossover.medicapp.R;
import com.crossover.medicapp.controllers.common.BaseFragment;
import com.google.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import roboguice.inject.InjectView;

/**
 * A simple {@link Fragment} subclass. Use the {@link CalendarDayDetailFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class CalendarDayDetailFragment extends BaseFragment {

    /** The event key **/
    public static final String EVENT = "EVENT";

    /** Date format to be shown **/
    private static final String TIME_FORMAT = "HH:mm";

    /** Code request of the detail **/
    private static final int DETAIL_REQUEST_CODE = 1;

    /** Day hours **/
    private static List<Integer> mHours;
    /** Time formatter **/
    private static SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);

    static {
        final int hours = 24;
        mHours = new ArrayList<>(hours);
        for (int i = 0; i < hours; i++) {
            mHours.add(i);
        }
    }

    /** Events per hour **/
    private final Map<Integer, List<Event>> mEventsPerHour = new HashMap<>();
    /** Calendar service **/
    @Inject
    private ICalendarService mCalendarService;
    /** Hours ListView **/
    @InjectView(R.id.hours_lv)
    private ListView mHoursLv;

    /** Day number TextView **/
    @InjectView(R.id.day_number_rtv)
    private TextView mDayNumberRtv;

    /** Events of the day **/
    private List<Event> mEvents;

    /** Day of week TextView **/
    @InjectView(R.id.day_of_week_rtv)
    private TextView mDayOfWeekRtv;

    /** Date that is currently showing **/
    private Date mCurDate;

    /** Start position of the events **/
    private int mStartPosition = -1;

    public CalendarDayDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @return A new instance of fragment CalendarDayDetailFragment.
     */
    public static CalendarDayDetailFragment newInstance(List<Event> events, Date curDate) {
        CalendarDayDetailFragment fragment = new CalendarDayDetailFragment();
        fragment.mEvents = events;
        fragment.mCurDate = curDate;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_day_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mEvents == null) {
            mEvents = new ArrayList<Event>(0);
        }

        for (Event event : mEvents) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getStartDate());
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            List<Event> events = mEventsPerHour.get(hora);
            if (events == null) {
                events = new ArrayList<>();
                mEventsPerHour.put(hora, events);
                if (mStartPosition == -1 || hora < mStartPosition) {
                    mStartPosition = hora;
                }
            }
            events.add(event);
        }

        HoursAdapter adapter = new HoursAdapter(getActivity(), mEventsPerHour);
        mHoursLv.setAdapter(adapter);
        if (mStartPosition != -1) {
            int realPos = mStartPosition != 0 ? mStartPosition - 1 : mStartPosition;
            mHoursLv.smoothScrollToPosition(realPos);
            mHoursLv.setSelection(realPos);
        }

        Locale curLocale = MedicappUtils.getLocale(getContext());
        mDayNumberRtv.setText(new SimpleDateFormat("dd", curLocale).format(mCurDate));
        String dayOfWeek = new SimpleDateFormat("EEE", curLocale).format(mCurDate);
        dayOfWeek = WordUtils.capitalizeFully(dayOfWeek);
        mDayOfWeekRtv.setText(dayOfWeek);

    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateData();
    }

    /**
     * This method is called to hide features based on the given role
     *
     * @param role
     *         The user's role
     */
    @Override
    public void setUpFeaturesByRole(Role role) {
        // Nothing to set up
    }

    /**
     * This method invalidates the data and refresh the ListView
     */
    private void invalidateData() {
        if (mEvents != null) {
            for (Event event : mEvents) {
                Event dbEvent = mCalendarService.findEventById(event.getId());
                copyEvents(dbEvent, event);
            }
            ((BaseAdapter) mHoursLv.getAdapter()).notifyDataSetInvalidated();
        }
    }

    /**
     * This method copies the event information to another
     *
     * @param origin
     *         Origin event
     * @param target
     *         Target event
     */
    private void copyEvents(Event origin, Event target) {
        target.setName(origin.getName());
        target.setStartDate(origin.getStartDate());
        target.setEndDate(origin.getEndDate());
        target.setAttendantsIds(origin.getAttendantsIds());
    }

    /**
     * This is the hours adapter. The hour view is created here
     */
    private class HoursAdapter extends ArrayAdapter<Integer> {

        /** LayoutInflater **/
        LayoutInflater mInflater;

        /** Mapa con los eventos dividido por horas **/
        Map<Integer, List<Event>> mEventosHora;

        /**
         * Constructor
         *
         * @param context
         *         The current context.
         * @param mEventosHora
         *         Eventos a mostrar
         */
        public HoursAdapter(Context context, Map<Integer, List<Event>> mEventosHora) {
            super(context, R.layout.day_event_view, mHours);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mEventosHora = mEventosHora;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.day_event_view, parent, false);
                holder = new ViewHolder();
                holder.eventsLl = (LinearLayout) convertView.findViewById(R.id.events_ll);
                holder.horaEventoTv = (TextView) convertView.findViewById(R.id.hora_evento_rtv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            int hour = getItem(position) + 1;
            Calendar calendar = Calendar.getInstance();
            Date horaFinal = DateUtils.setTime(calendar.getTime(), hour, 0, 0, 0);
            holder.horaEventoTv.setText(formatter.format(horaFinal));

            List<Event> events = mEventosHora.get(hour - 1);
            holder.eventsLl.removeAllViews();
            if (events != null) {
                for (Event event : events) {
                    View eventView = EventCalendarGridAdapter
                            .createEventView(event, mInflater, true);
                    configEventClicked(event, eventView);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 2);
                    holder.eventsLl.addView(eventView, params);

                }
            }

            return convertView;
        }

        /**
         * Este método configura el click en el evento y realiza la transición para mostrar el
         * detalle del mismo
         *
         * @param eventView
         *         Vista del evento
         */
        private void configEventClicked(final Event event, final View eventView) {
            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetail(event, eventView);
                }
            });
        }

        private void showDetail(Event event, View view) {
            Intent eventDetailIntent = new Intent(getContext(), EventDetailActivity.class);
            eventDetailIntent.putExtra(EVENT, event);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), view,
                                getString(R.string.transition_toolbar));
                ActivityCompat.startActivityForResult(getActivity(), eventDetailIntent,
                        DETAIL_REQUEST_CODE, options.toBundle());
            } else {
                startActivityForResult(eventDetailIntent, DETAIL_REQUEST_CODE);
            }
        }

        class ViewHolder {
            public LinearLayout eventsLl;
            public TextView horaEventoTv;
        }
    }

}
