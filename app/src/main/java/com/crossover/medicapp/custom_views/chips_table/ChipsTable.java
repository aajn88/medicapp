package com.crossover.medicapp.custom_views.chips_table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;

/**
 * This is a custom view to show chips in a table layout
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class ChipsTable extends TableLayout {

    /** Adapter **/
    private IChipsTableAdapter adapter;

    /**
     * <p>Creates a new TableLayout for the given context.</p>
     *
     * @param context
     *         the application environment
     */
    public ChipsTable(Context context) {
        super(context);
    }

    /**
     * <p>Creates a new TableLayout for the given context and with the
     * specified set attributes.</p>
     *
     * @param context
     *         the application environment
     * @param attrs
     *         a collection of attributes
     */
    public ChipsTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * The adapter to set the chips on the table
     *
     * @param adapter
     *         Chips Adapter
     */
    public void setAdapter(IChipsTableAdapter adapter) {
        this.adapter = adapter;
        removeAllViews();

        while (adapter.hasNextRow()) {
            addView(adapter.nextRow());
        }

    }

    /**
     * @return the listener
     */
    public IOnItemSelectedListener getOnItemSelectedListener() {
        return adapter.getOnItemSelectedListener();
    }

    /**
     * @param listener
     *         the listener to set
     */
    public void setOnItemSelectedListener(IOnItemSelectedListener listener) {
        adapter.setOnItemSelectedListener(listener);
    }

    /**
     * This method gets an item given its position
     *
     * @param position
     *         Position
     *
     * @return The selected object
     */
    public Object getItem(int position) {
        return adapter.getItem(position);
    }

    /**
     * Interface to show when an item is selected
     */
    public interface IOnItemSelectedListener {

        /**
         * This method is called when an item is selected
         *
         * @param view
         *         Selected item view
         * @param position
         *         Position of the selected view
         */
        void onItemSelected(View view, int position);
    }

}
