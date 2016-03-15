package com.crossover.common.model.constants;

import android.support.annotation.StringRes;

import com.crossover.common.R;

/**
 * This is the enum where roles are specified
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public enum Role {

    /** Administrator **/
    ADMIN(R.string.admin),

    /** Doctor user **/
    DOCTOR(R.string.doctor);

    @StringRes
    private final int stringRes;

    /**
     * Role constructor to have the string value
     *
     * @param stringRes
     *         Id of the role string
     */
    Role(@StringRes int stringRes) {
        this.stringRes = stringRes;
    }

    /**
     * @return the stringRes
     */
    public int getStringRes() {
        return stringRes;
    }
}
