package com.crossover.common.model.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * This is the date utils
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class DateUtils {

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

}
