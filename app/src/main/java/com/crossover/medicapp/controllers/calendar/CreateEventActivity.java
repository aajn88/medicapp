package com.crossover.medicapp.controllers.calendar;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.business.services.api.ISessionService;
import com.crossover.business.services.api.IUserService;
import com.crossover.common.model.common.Event;
import com.crossover.common.model.common.User;
import com.crossover.common.model.utils.DateUtils;
import com.crossover.medicapp.R;
import com.crossover.medicapp.custom_views.chips_table.ChipsTable;
import com.crossover.medicapp.custom_views.chips_table.ChipsTableAdapter;
import com.crossover.medicapp.utils.ViewUtils;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_create_event)
public class CreateEventActivity extends RoboActionBarActivity implements View.OnClickListener {

    /** Add attendant **/
    @InjectView(R.id.add_attendant_actv)
    private AutoCompleteTextView mAddAttendantActv;

    /** Chips table **/
    @InjectView(R.id.attendants_ct)
    private ChipsTable mAttendantsCt;

    /** Toolbar Relative Layout **/
    @InjectView(R.id.toolbar_rl)
    private View mToolbarRl;

    /** Close activity **/
    @InjectView(R.id.close_mditv)
    private TextView mCloseBtn;

    /** Save event **/
    @InjectView(R.id.save_mditv)
    private TextView mSaveBtn;

    /** Event title EditText **/
    @InjectView(R.id.event_title_et)
    private EditText mEventTitleEt;

    /** Event Date Rtv **/
    @InjectView(R.id.event_date_rtv)
    private TextView mEventDateRtv;

    /** Event time Rtv **/
    @InjectView(R.id.event_time_rtv)
    private TextView mEventTimeRtv;

    /** Required field **/
    @InjectResource(R.string.required_field)
    private String mRequiredFieldMsg;

    /** Users service **/
    @Inject
    private IUserService mUserService;

    /** Session service **/
    @Inject
    private ISessionService mSessionService;

    /** Calendar service **/
    @Inject
    private ICalendarService mCalendarService;

    /** The selected attendants for the event **/
    private Set<User> mSelectedUsers = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCloseBtn.setOnClickListener(this);

        ViewCompat.setElevation(mToolbarRl, getResources().getDimension(R.dimen.toolbar_elevation));

        // Event creator is set as an attendant by default
        mSelectedUsers.add(mSessionService.getCurrentSession());

        loadAttendants();

        mSaveBtn.setOnClickListener(this);

        mEventDateRtv.setOnClickListener(this);
        String date = DateUtils.formatDateDefaultLocale(this, new Date(),
                DateUtils.FORMAT_WITH_DAY_OF_WEEK);
        mEventDateRtv.setText(WordUtils.capitalizeFully(date.toLowerCase()));
        mEventTimeRtv.setText(DateUtils.formatDate(new Date(), DateUtils.FORMAT_TIME_AM_PM));

        mEventTimeRtv.setOnClickListener(this);
    }

    /**
     * This method loads the attendants
     */
    private void loadAttendants() {
        List<User> usersList = new ArrayList<>(mSelectedUsers);
        ChipsTableAdapter<User> adapter =
                new ChipsTableAdapter<User>(this, R.layout.deletable_chip_layout, usersList);
        mAttendantsCt.setAdapter(adapter);

        final ArrayAdapter<User> autoAdapter =
                new ArrayAdapter<User>(this, R.layout.support_simple_spinner_dropdown_item,
                        mUserService.getAllUsers());
        mAddAttendantActv.setAdapter(autoAdapter);
        mAddAttendantActv.setThreshold(1);
        mAddAttendantActv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAddAttendantActv.setText(null);
                User selectedUser = autoAdapter.getItem(position);
                if (!mSelectedUsers.contains(selectedUser)) {
                    mSelectedUsers.add(selectedUser);
                    loadAttendants();
                }
            }
        });

        mAttendantsCt.setOnItemSelectedListener(new ChipsTable.IOnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                User selectedUser = (User) mAttendantsCt.getItem(position);
                mSelectedUsers.remove(selectedUser);
                loadAttendants();
            }
        });
    }

    /**
     * This method validates and then saves the current event
     */
    private void saveEvent() {
        Event event = buildEvent();
        if (validateFields(event)) {
            // TODO: Save event
            mCalendarService.createEvent(event);
            ViewUtils.makeToast(this, R.string.event_succesffuly_created, SuperToast.Duration.LONG,
                    Style.BLUE).show();
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * This method builds the event based on the information given by the user in the UI
     *
     * @return The resulting event
     */
    private Event buildEvent() {
        Event event = new Event();

        event.setName(mEventTitleEt.getText().toString());

        Integer[] usersIds = new Integer[mSelectedUsers.size()];
        int i = 0;
        for (User user : mSelectedUsers) {
            usersIds[i++] = user.getId();
        }
        event.setAttendantsIds(usersIds);

        Date finalDate = DateUtils.parseDateDefaultLocale(this, mEventDateRtv.getText().toString(),
                DateUtils.FORMAT_WITH_DAY_OF_WEEK);
        Date timeDate = DateUtils.parseDateDefaultLocale(this, mEventTimeRtv.getText().toString(),
                DateUtils.FORMAT_TIME_AM_PM);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(timeDate);

        Calendar finalCal = Calendar.getInstance();
        finalCal.setTime(finalDate);
        finalCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        finalCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));

        event.setStartDate(finalCal.getTime());

        return event;
    }

    /**
     * This method validates the fields
     *
     * @param event
     *         Event to be validated
     *
     * @return True if the fields were validated. Otherwise returns False
     */
    private boolean validateFields(Event event) {
        if (event.getName().length() == 0) {
            mEventTitleEt.setError(mRequiredFieldMsg);
            return false;
        }
        return true;
    }

    /**
     * This method shows the picker time
     */
    private void pickTime() {
        Calendar today = Calendar.getInstance();
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                mEventTimeRtv.setText(
                        DateUtils.formatDate(calendar.getTime(), DateUtils.FORMAT_TIME_AM_PM));
            }
        }, today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), true);
        mTimePicker.show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v
     *         The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_mditv:
                finish();
                break;
            case R.id.save_mditv:
                saveEvent();
                break;
            case R.id.event_date_rtv:
                ViewUtils.buildDatePicker(this, mEventDateRtv, Calendar.getInstance(), null).show();
                break;
            case R.id.event_time_rtv:
                pickTime();
                break;
        }
    }
}
