package com.crossover.medicapp.controllers.invitations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.business.services.api.IUserService;
import com.crossover.common.model.common.Event;
import com.crossover.common.model.utils.DateUtils;
import com.crossover.medicapp.R;
import com.google.inject.Inject;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

/**
 * This method shows the invitations of the current user
 *
 * @author <a href="mailto:antonio-jimenez@accionplus.com">Antonio A. Jimenez N.</a>
 */
public class InvitationsCardsAdapter extends ArrayAdapter<Event> {

    /** Layout Inflater **/
    private LayoutInflater mInflater;

    /** Calendar service **/
    @Inject
    private ICalendarService mCalendarService;

    /** Users service **/
    @Inject
    private IUserService mUserService;

    /** DynamicListView **/
    private DynamicListView mListView;

    /**
     * Constructor
     *
     * @param context
     *         The current context.
     * @param objects
     *         The objects to represent in the ListView.
     */
    public InvitationsCardsAdapter(Context context, Event[] objects) {
        this(context, new ArrayList<Event>(Arrays.asList(objects)));
    }

    /**
     * Constructor
     *
     * @param context
     *         The current context.
     * @param objects
     *         The objects to represent in the ListView.
     */
    public InvitationsCardsAdapter(Context context, List<Event> objects) {
        super(context, R.layout.invitation_google_card, objects);
        mInflater = LayoutInflater.from(context);
        RoboInjector injector = RoboGuice.getInjector(context);
        injector.injectMembersWithoutViews(this);
    }

    /**
     * This method sets the DynamicListView for communication
     *
     * @param listView
     *         DynamicListView
     */
    public void setDynamicListView(DynamicListView listView) {
        mListView = listView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.invitation_google_card, parent, false);
            holder = new Holder();

            holder.title = (TextView) convertView.findViewById(R.id.event_time_rtv);
            holder.time = (TextView) convertView.findViewById(R.id.time_rtv);
            holder.amPm = (TextView) convertView.findViewById(R.id.am_pm_rtv);
            holder.date = (TextView) convertView.findViewById(R.id.date_rtv);
            holder.attendants = (TextView) convertView.findViewById(R.id.attendants_rtv);
            holder.reject = (TextView) convertView.findViewById(R.id.reject_rtv);
            holder.accept = (TextView) convertView.findViewById(R.id.accept_rtv);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Event event = getItem(position);
        holder.title.setText(event.getName());
        holder.time.setText(DateUtils.formatDate(event.getStartDate(), "hh:mm"));
        holder.amPm.setText(DateUtils.formatDate(event.getStartDate(), "a"));
        holder.date.setText(
                DateUtils.formatDate(event.getStartDate(), DateUtils.FORMAT_WITH_DAY_OF_WEEK));
        printAttendants(holder, event);

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRejectInvitation(false, position);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRejectInvitation(true, position);
            }
        });

        return convertView;
    }

    /**
     * This method prints the attendants
     *
     * @param holder
     *         The view holder
     * @param event
     *         The event
     */
    private void printAttendants(Holder holder, Event event) {
        if (event.getAttendantsIds() != null && event.getAttendantsIds().length != 0) {
            holder.attendants.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (Integer userId : event.getAttendantsIds()) {
                sb.append(mUserService.findUserById(userId));
                sb.append("\n");
            }
            holder.attendants.setText(sb.toString());
        } else {
            holder.attendants.setVisibility(View.GONE);
        }
    }

    /**
     * This method accepts/rejects an invitation to an event
     *
     * @param accept
     *         Accept/reject
     * @param position
     *         Event position into the adapter
     */
    private void acceptRejectInvitation(boolean accept, int position) {
        Event event = getItem(position);
        mCalendarService.acceptRejectInvitation(event.getId(), accept);
        if (mListView != null) {
            mListView.dismiss(position);
        } else {
            remove(event);
            notifyDataSetChanged();
        }
    }

    /**
     * Holder view for Holder pattern
     */
    private class Holder {
        TextView title;
        TextView time;
        TextView amPm;
        TextView date;
        TextView attendants;
        TextView reject;
        TextView accept;
    }

}
