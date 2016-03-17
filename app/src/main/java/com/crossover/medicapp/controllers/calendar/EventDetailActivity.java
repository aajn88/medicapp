package com.crossover.medicapp.controllers.calendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.business.services.api.IUserService;
import com.crossover.common.model.common.Event;
import com.crossover.common.model.common.User;
import com.crossover.common.model.constants.Role;
import com.crossover.common.model.utils.DateUtils;
import com.crossover.common.model.utils.MedicappUtils;
import com.crossover.medicapp.R;
import com.crossover.medicapp.controllers.common.BaseActivity;
import com.crossover.medicapp.custom_views.progress_bars.ProgressWheel;
import com.crossover.medicapp.utils.ViewUtils;
import com.google.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Date;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_event_detail)
public class EventDetailActivity extends BaseActivity implements View.OnClickListener {

    /** Tag logs **/
    private static final String TAG_LOG = EventDetailActivity.class.getName();

    /** Edit request **/
    private static final int EDIT_CODE_REQUEST = 1;

    /** Main content view **/
    @InjectView(R.id.main_content_ll)
    private View mMainContentLl;

    /** Toolbar window **/
    @InjectView(R.id.event_toolbar_rl)
    private RelativeLayout mEventToolbarRl;

    /** Toolbar Title **/
    @InjectView(R.id.event_title_rtv)
    private TextView mEventTitleTv;

    /** Close event detail Button **/
    @InjectView(R.id.close_mditv)
    private TextView mCloseBtn;

    /** Date TextView **/
    @InjectView(R.id.date_rtv)
    private TextView mDateTv;

    /** Event date caption **/
    @InjectView(R.id.date_caption_rtv)
    private TextView mDateCaptionTv;

    /** Event attendants **/
    @InjectView(R.id.attendant_rtv)
    private TextView mAttendantsTv;

    /** Event state **/
    @InjectView(R.id.event_state_rtv)
    private TextView mEventStateRtv;

    /** Event caption event state **/
    @InjectView(R.id.state_caption_rtv)
    private TextView mStateCaptionTv;

    /** Attendants View **/
    @InjectView(R.id.attendants_rl)
    private View mAttendantsRl;

    /** Loading Progress Wheel **/
    @InjectView(R.id.loading_pw)
    private ProgressWheel mLoadingPw;

    /** Edit event Fab **/
    @InjectView(R.id.edit_event_fab)
    private FloatingActionButton mEditEventFab;

    /** The event sent as a parameter **/
    private Event mEvent;

    /** Users service **/
    @Inject
    private IUserService mUserService;

    /** Calendar service **/
    @Inject
    private ICalendarService mCalendarService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (!extras.containsKey(CalendarDayDetailFragment.EVENT)) {
            Log.w(TAG_LOG, "An event has not been received. The activity cannot be initialized");
            finishActivity();
            return;
        }

        mEvent = (Event) extras.getSerializable(CalendarDayDetailFragment.EVENT);

        init();

        mEditEventFab.setOnClickListener(this);
    }

    /**
     * Views init
     */
    private void init() {
        int dif = new Date().compareTo(mEvent.getStartDate());
        mEventStateRtv.setText(dif < 0 ? R.string.scheduled : R.string.completed);
        mStateCaptionTv.setText(dif < 0 ? R.string.future_event : R.string.past_event);

        int color = dif < 0 ? R.color.material_blue_400 : R.color.material_green_400;
        mEventToolbarRl.setBackgroundResource(color);
        mEventTitleTv.setText(mEvent.getName());
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        String date = DateUtils.formatSpecialDate(this, mEvent.getStartDate(), "EEEE, MMMM dd");
        mDateTv.setText(WordUtils.capitalizeFully(date));
        mDateCaptionTv.setText(DateUtils.formatDate(mEvent.getStartDate(), "h:mm a"));

        Integer[] attendantsIds = mEvent.getAttendantsIds();
        if (attendantsIds == null || attendantsIds.length == 0) {
            mAttendantsRl.setVisibility(View.GONE);
            return;
        }
        StringBuilder attendantsSb = new StringBuilder();
        boolean first = true;
        for (Integer id : attendantsIds) {
            if (first) {
                first = false;
            } else {
                attendantsSb.append("\n");
            }
            User user = mUserService.findUserById(id);
            if (user != null) {
                attendantsSb.append(user.getName());
            }
        }
        mAttendantsTv.setText(attendantsSb.toString());
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    /**
     * This method finishes the activity based on its Android version
     */
    private void finishActivity() {
        mCloseBtn.setVisibility(View.GONE);
        if (MedicappUtils.isLollipop()) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    /**
     * This method is called to hide features based on the given role
     *
     * @param role
     *         The user's role
     */
    @Override
    public void setUpFeaturesByRole(Role role) {
        if (role != Role.ADMIN) {
            mEditEventFab.setVisibility(View.GONE);
        }
    }

    /**
     * This method starts the activity to edit the event
     */
    private void editEvent() {
        Intent editIntent = new Intent(this, CreateEventActivity.class);
        editIntent.putExtra(CreateEventActivity.EDIT_EVENT, mEvent);
        startActivityForResult(editIntent, EDIT_CODE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CODE_REQUEST && resultCode == RESULT_OK) {
            new LoadEventUpdateAsyncTask().execute();
        }
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
            case R.id.edit_event_fab:
                editEvent();
                break;
        }
    }

    /**
     * This method enables/disables the progress wheel and its related views
     */
    private void enableLoading(boolean enable) {
        ViewUtils.enableProgressWheel(mLoadingPw, enable, mMainContentLl, mEditEventFab);
    }

    /**
     * This async task loads the current event update
     */
    private class LoadEventUpdateAsyncTask extends AsyncTask<Void, Void, Event> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enableLoading(true);
        }

        @Override
        protected Event doInBackground(Void... params) {
            try {
                // Simulating server connection
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return mCalendarService.findEventById(mEvent.getId());

        }

        @Override
        protected void onPostExecute(Event event) {
            super.onPostExecute(event);
            mEvent = event;
            init();

            enableLoading(false);
        }
    }

}
