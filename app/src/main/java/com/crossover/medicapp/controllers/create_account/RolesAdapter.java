package com.crossover.medicapp.controllers.create_account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crossover.common.model.constants.Role;
import com.crossover.medicapp.R;

import java.util.Arrays;
import java.util.List;

/**
 * This is the roles adapter for spinner values
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class RolesAdapter extends ArrayAdapter<Role> {

    /** The layout inflater **/
    private LayoutInflater mInflater;

    /**
     * Constructor
     *
     * @param context
     *         The current context.
     * @param objects
     *         The objects to represent in the ListView.
     */
    public RolesAdapter(Context context, Role[] objects) {
        this(context, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context
     *         The current context.
     * @param objects
     *         The objects to represent in the ListView.
     */
    public RolesAdapter(Context context, List<Role> objects) {
        super(context, R.layout.list_item_simple, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_simple, parent, false);
            holder = new Holder();

            holder.role = (TextView) convertView.findViewById(R.id.main_text_rtv);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Role role = getItem(position);
        holder.role.setText(role.getStringRes());

        return convertView;
    }

    /**
     * Holder View
     */
    private class Holder {
        TextView role;
    }
}
