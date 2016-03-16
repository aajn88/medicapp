package com.crossover.medicapp.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.crossover.common.model.utils.DateUtils;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class ViewUtils {

    /**
     * This method returns an animation for an adapter given the base adapter and a key
     *
     * @param key
     *         Key from 0 to 4 to select an animation
     * @param adapter
     *         The base adapter that will take the animation adapter
     *
     * @return Animated Adapter
     */
    public static AnimationAdapter animateAdapter(int key, BaseAdapter adapter) {
        AnimationAdapter animAdapter;
        switch (key) {
            default:
            case 0:
                animAdapter = new AlphaInAnimationAdapter(adapter);
                break;
            case 1:
                animAdapter = new ScaleInAnimationAdapter(adapter);
                break;
            case 2:
                animAdapter = new SwingBottomInAnimationAdapter(adapter);
                break;
            case 3:
                animAdapter = new SwingLeftInAnimationAdapter(adapter);
                break;
            case 4:
                animAdapter = new SwingRightInAnimationAdapter(adapter);
                break;
        }
        return animAdapter;
    }

    /**
     * This method makes a toast.
     *
     * @param context
     *         Current context
     * @param resId
     *         String to be shown
     * @param duration
     *         Toast duration
     * @param color
     *         SuperToast color. Selected from {@link Style} constants, such as, {@link Style#BLUE}
     *
     * @return SuperToast instance
     */
    @NonNull
    public static SuperToast makeToast(Context context, @StringRes int resId, int duration,
                                       int color) {
        return makeToast(context, context.getString(resId), duration, color);
    }

    /**
     * This method makes a toast.
     *
     * @param context
     *         Current context
     * @param text
     *         String to be shown
     * @param duration
     *         Toast duration
     * @param color
     *         SuperToast color. Selected from {@link Style} constants, such as, {@link Style#BLUE}
     *
     * @return SuperToast instance
     */
    @NonNull
    public static SuperToast makeToast(Context context, String text, int duration, int color) {
        return SuperToast.create(context, text, duration,
                Style.getStyle(color, SuperToast.Animations.FLYIN));
    }

    /**
     * This method hides the keyboard
     *
     * @param activity
     *         Active activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * This method converts dp to pixels
     *
     * @param dp
     *         Oiringla dp
     * @param context
     *         App context
     *
     * @return Dp value in pixels
     */
    public static int dp2Pixels(int dp, Context context) {
        Resources r = context.getResources();
        return (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    /**
     * This method builds a date picker given a min and max date
     *
     * @param context
     *         App context
     * @param minDate
     *         Min date. Can be null to represent no limit
     * @param maxDate
     *         Max date. Can be null to represent no limit
     *
     * @return The DatePickerDialog
     */
    @NonNull
    public static DatePickerDialog buildDatePicker(final Context context, final TextView tv,
                                                   Calendar minDate, Calendar maxDate) {
        return buildDatePicker(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                Date targetDate = DateUtils.setTime(cal.getTime(), 0, 0, 0);
                String calendario = DateUtils.formatDateDefaultLocale(context, targetDate,
                        DateUtils.FORMAT_WITH_DAY_OF_WEEK);
                if (tv != null) {
                    tv.setText(WordUtils.capitalizeFully(calendario.toLowerCase()));
                }
            }
        }, minDate, maxDate);
    }

    /**
     * This method builds a date picker given a min and max date
     *
     * @param context
     *         App context
     * @param listener
     *         Listener when selected date
     * @param minDate
     *         Min date. Can be null to represent no limit
     * @param maxDate
     *         Max date. Can be null to represent no limit
     *
     * @return The DatePickerDialog
     */
    @NonNull
    public static DatePickerDialog buildDatePicker(Context context,
                                                   DatePickerDialog.OnDateSetListener listener,
                                                   Calendar minDate, Calendar maxDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpDialog = new DatePickerDialog(context, listener, year, month, day);
        DatePicker datePicker = dpDialog.getDatePicker();
        if (minDate != null) {
            datePicker.setMinDate(minDate.getTimeInMillis());
        }
        if (maxDate != null) {
            datePicker.setMaxDate(maxDate.getTimeInMillis());
        }
        return dpDialog;
    }

}
