package com.crossover.common.model.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crossover.common.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This is the date utils
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class DateUtils {

    /** Tag for logs **/
    private static final String TAG_LOG = DateUtils.class.getName();

    /** Date format with the day of the week */
    public static final String FORMAT_WITH_DAY_OF_WEEK = "EEE, dd MMM yyyy";

    /** Time format with AM and PM */
    public static final String FORMAT_TIME_AM_PM = "hh:mm a";

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

    /**
     * This method formats a given date based on a format and the default locale
     *
     * @param context
     *         App context
     * @param date
     *         Date to be converted
     * @param format
     *         The target format
     *
     * @return The formatted date
     */
    @NonNull
    public static String formatDateDefaultLocale(Context context, Date date, String format) {
        if (date == null || format == null) {
            return StringUtils.EMPTY_STRING;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, MedicappUtils.getLocale(context));
        return sdf.format(date);
    }

    /**
     * This method formats a given date based on a format and the default locale. When the day is
     * "today", "yesterdar" or "tomorrow", then this words are shown respectively.
     *
     * @param context
     *         App context
     * @param date
     *         Date to be formatted
     * @param format
     *         target format
     *
     * @return Formatted Date
     */
    @NonNull
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

    /**
     * This method formatts a date using no locale
     *
     * @param date
     *         Date to be formatted
     * @param format
     *         Date format
     *
     * @return Formatted date
     */
    public static String formatDate(Date date, String format) {
        if (date == null || format == null) {
            return StringUtils.EMPTY_STRING;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * This method parsers a date string to a date given the format and the default locale
     *
     * @param context
     *         App context
     * @param dateStr
     *         Date String
     * @param format
     *         String Date format
     *
     * @return The date, if an error occurred, then null is returned
     */
    public static Date parseDateDefaultLocale(Context context, String dateStr, String format) {
        return parseDate(MedicappUtils.getLocale(context), dateStr, format);
    }

    /**
     * This method parsers a date string to a date given the format and the locale
     *
     * @param locale
     *         Locale
     * @param dateStr
     *         Date String
     * @param format
     *         String Date format
     *
     * @return The date, if an error occurred, then null is returned
     */
    public static Date parseDate(Locale locale, String dateStr, String format) {
        DateFormat formatter = new SimpleDateFormat(format, locale);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            Log.e(TAG_LOG, StringUtils
                    .format("An error occurred while parsing a date to [format = %s]", format), e);
            return null;
        }
    }

}
