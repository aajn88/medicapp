package com.crossover.common.model.utils;

import android.content.Context;

import com.crossover.common.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is the date utils
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class DateUtils {

    /** Empty String */
    private static final String EMPTY_STRING = "";

    /** Private constructor to avoid instances **/
    private DateUtils() {}

    /**
     * This method returns a new Date with a specified hour, minute, second and millis
     *
     * @param date
     *         Start date
     * @param hourOfDay
     *         Target hour (0 - 23)
     * @param minute
     *         Target minute (0 - 59)
     * @param second
     *         Target second (0 - 59)
     *
     * @return The requested time
     */
    public static Date setTime(Date date, int hourOfDay, int minute, int second) {
        return setTime(date, hourOfDay, minute, second, 0);
    }

    /**
     * This method returns a new Date with a specified hour, minute, second and millis
     *
     * @param date
     *         Start date
     * @param hourOfDay
     *         Target hour (0 - 23)
     * @param minute
     *         Target minute (0 - 59)
     * @param second
     *         Target second (0 - 59)
     * @param millis
     *         Target millis (0 - 999)
     *
     * @return The requested time
     */
    public static Date setTime(Date date, int hourOfDay, int minute, int second, int millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millis);
        return cal.getTime();
    }

    public static String formatDateDefaultLocale(Context context, Date fecha, String formato) {
        if (fecha == null || formato == null) {
            return EMPTY_STRING;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formato, MedicappUtils.getLocale(context));
        return sdf.format(fecha);
    }

    public static String formatSpecialDate(Context context, Date date, String format) {
        String message;

        Calendar yesterdayCalendar = Calendar.getInstance();
        yesterdayCalendar.setTime(new Date());
        yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);

        Calendar tomorrowCalendar = Calendar.getInstance();
        tomorrowCalendar.setTime(new Date());
        tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);

        String today = DateUtils.formatDateDefaultLocale(context, new Date(), format);
        String yesterday = DateUtils.formatDateDefaultLocale(context, yesterdayCalendar.getTime(),
                format);
        String tomorrow = DateUtils.formatDateDefaultLocale(context, tomorrowCalendar.getTime(),
                format);
        String selected = DateUtils.formatDateDefaultLocale(context, date, format);

        if (today.equals(selected)) {
            message = context.getString(R.string.today);
        } else if (yesterday.equals(selected)) {
            message = context.getString(R.string.yesterday);
        } else if (tomorrow.equals(selected)) {
            message = context.getString(R.string.tomorrow);
        } else {
            message = selected;
        }

        return message;
    }

    public static String formatDate(Date date, String format) {
        if (date == null || format == null) {
            return EMPTY_STRING;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
