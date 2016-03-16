package com.crossover.medicapp.utils;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

/**
 * This utils class contains basic Medicapp operations
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class MedicappUtils {

    /** Private constructor to avoid instances **/
    private MedicappUtils() {}

    /**
     * This method checks if the Android version is grater or equals than Lollipop
     *
     * @return True if the current Android Version is grater or equals to Lollipop
     */
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * This method gets the device locale
     *
     * @param context
     *         App context
     *
     * @return Device locale
     */
    public static Locale getLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

}
