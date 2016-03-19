package com.crossover.common.model.utils;

import com.crossover.common.model.common.Event;
import com.crossover.common.model.common.User;
import com.crossover.common.model.constants.Role;

import java.util.Date;

/**
 * Utils for common tests operation
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class TestUtils {

    /**
     * This method builds a basic Test User
     *
     * @return Test User
     */
    public static User buildTestUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setName("name");
        user.setRole(Role.ADMIN);
        return user;
    }

    /**
     * This method builds a test event
     *
     * @return The test event
     */
    public static Event buildTestEvent() {
        Event event = new Event();

        event.setName("Event 1");
        event.setStartDate(new Date());
        event.setAttendantsIds(new Integer[]{1, 2, 3, 4, 5});

        return event;
    }

}
