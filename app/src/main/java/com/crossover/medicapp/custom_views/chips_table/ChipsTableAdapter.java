package com.crossover.medicapp.custom_views.chips_table;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.crossover.medicapp.R;
import com.crossover.medicapp.custom_views.font.RobotoTextView;
import com.crossover.medicapp.utils.ViewUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Adapter to shown chips in {@link ChipsTable}
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class ChipsTableAdapter<T> implements IChipsTableAdapter<T>, View.OnClickListener {

    /** Empty string **/
    private static final String EMPTY_STRING = "";

    /** Divider dp **/
    private static final int DIVIDER_DP = 4;

    /** Threshold between chips **/
    private static final int THRESHOLD = 200;

    /** List of objects in the table **/
    private List<T> mObjects;

    /** Layout Inflater **/
    private LayoutInflater mInflater;

    /** Context **/
    private Context mContext;

    /** Current position of the shown elements **/
    private int mCurrentPos;

    /** Screen Width **/
    private int mScreenWidth;

    /** Listener On Item Selected Listener **/
    private ChipsTable.IOnItemSelectedListener listener;

    /** Chip Layout Resource **/
    private final int mResource;

    /**
     * Adapter constructor
     *
     * @param mContext
     *         App contezt
     * @param mObjects
     *         Objects to be shown
     */
    public ChipsTableAdapter(Context mContext, T[] mObjects) {
        this(mContext, Arrays.asList(mObjects));
    }

    /**
     * Adapter constructor
     *
     * @param mContext
     *         App context
     * @param mObjects
     *         Objects to be shown
     */
    public ChipsTableAdapter(Context mContext, List<T> mObjects) {
        this(mContext, R.layout.chip_layout, mObjects);
    }

    public ChipsTableAdapter(Context mContext, int mResource, T[] mObjects) {
        this(mContext, mResource, Arrays.asList(mObjects));
    }

    public ChipsTableAdapter(Context mContext, int mResource, List<T> mObjects) {
        this.mObjects = mObjects;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mResource = mResource;

        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
    }

    /**
     * Indicates if there is a next row
     *
     * @return True if there is a next row. Otherwise returns false
     */
    @Override
    public boolean hasNextRow() {
        return mCurrentPos < mObjects.size();
    }

    /**
     * This method requests the next row
     *
     * @return Returns the next row to be shown. Otherwise returns null
     */
    @Override
    public TableRow nextRow() {
        if (!hasNextRow()) {
            return null;
        }

        int pxs = ViewUtils.dp2Pixels(DIVIDER_DP, mContext);

        TableRow row = new TableRow(mContext);
        row.setPadding(0, 0, 0, pxs);

        for (boolean first = true; validateNextChip(row); mCurrentPos++) {
            if (!first) {
                row.addView(getSpacing(), pxs, ViewGroup.LayoutParams.MATCH_PARENT);
            } else {
                first = false;
            }
            row.addView(getChip(mCurrentPos, row));
        }

        return row;
    }

    /**
     * This method validated if there is possible to set another chip in the row
     *
     * @param viewGroup
     *         target ViewGroup
     *
     * @return True if is possible to yield. Otherwise returns False
     */
    private boolean validateNextChip(ViewGroup viewGroup) {
        viewGroup.measure(0, 0);
        int threshold = ViewUtils.dp2Pixels(THRESHOLD, mContext);
        return hasNextRow() && viewGroup.getMeasuredWidth() + threshold <= mScreenWidth;
    }

    /**
     * This method creates spacing between the chips
     *
     * @return View with space between chips
     */
    private View getSpacing() {
        LinearLayout view = new LinearLayout(mContext);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewUtils.dp2Pixels(DIVIDER_DP, mContext),
                        ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        return view;
    }

    /**
     * This method returns an item in the given position
     *
     * @param position
     *         Item position
     *
     * @return Requested element
     */
    @Override
    public T getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * This method creates the chip view
     *
     * @param position
     *         Position where the chip will be shown
     *
     * @return Chip view
     */
    private View getChip(int position, ViewGroup viewGroup) {
        T item = getItem(position);

        View chip = mInflater.inflate(mResource, viewGroup, false);
        RobotoTextView chipTextRtv = (RobotoTextView) chip.findViewById(R.id.chip_text_rtv);
        chipTextRtv.setText(item == null ? EMPTY_STRING : item.toString());
        chip.setTag(position);
        chip.setOnClickListener(this);
        return chip;
    }

    /**
     * @param listener
     *         the listener to set
     */
    public void setOnItemSelectedListener(ChipsTable.IOnItemSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * @return the listener
     */
    public ChipsTable.IOnItemSelectedListener getOnItemSelectedListener() {
        return listener;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v
     *         The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (listener != null) {
            listener.onItemSelected(v, position);
        }
    }
}
