package com.crossover.medicapp.controllers.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crossover.business.services.api.IUserService;
import com.crossover.common.model.common.Event;
import com.crossover.common.model.common.User;
import com.crossover.common.model.utils.DateUtils;
import com.crossover.common.model.utils.MedicappUtils;
import com.crossover.medicapp.R;
import com.google.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Date;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_event_detail)
public class EventDetailActivity extends RoboActivity {

    /** Tag logs **/
    private static final String TAG_LOG = EventDetailActivity.class.getName();

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

    /** The event sent as a parameter **/
    private Event mEvent;

    /** Users service **/
    @Inject
    private IUserService mUserService;

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

}
