package com.crossover.business;

import com.crossover.business.services.api.ISessionService;
import com.crossover.business.services.impl.SessionService;
import com.crossover.common.model.common.User;
import com.crossover.common.model.constants.Role;
import com.crossover.persistence.managers.api.IUsersManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SessionServiceUnitTest {

    /** User's manager mock **/
    private IUsersManager usersManagerMock;

    /** Session service **/
    private ISessionService sessionService;

    @Before
    public void setup() throws Exception {
        sessionService = new SessionService();
        usersManagerMock = EasyMock.createNiceMock(IUsersManager.class);
        Field field = SessionService.class.getDeclaredField("mUsersManager");
        field.setAccessible(true);
        field.set(sessionService, usersManagerMock);
    }

    @Test
    public void checkAvailableUsernameAvailableTest() throws Exception {
        EasyMock.reset(usersManagerMock);

        EasyMock.expect(usersManagerMock.findByUsername("123456")).andReturn(null);

        EasyMock.replay(usersManagerMock);

        boolean success = sessionService.checkAvailableUsername("123456");

        assertEquals(success, true);
        EasyMock.verify(usersManagerMock);
    }

    @Test
    public void checkAvailableUsernameNotAvailableTest() throws Exception {
        EasyMock.reset(usersManagerMock);

        EasyMock.expect(usersManagerMock.findByUsername("123456")).andReturn(new User());

        EasyMock.replay(usersManagerMock);

        boolean success = sessionService.checkAvailableUsername("123456");

        assertEquals(success, false);
        EasyMock.verify(usersManagerMock);
    }

    @Test
    public void checkAvailableUsernameNotValidTest() throws Exception {
        boolean success = sessionService.checkAvailableUsername("123");
        assertEquals(success, false);
        success = sessionService.checkAvailableUsername(null);
        assertEquals(success, false);
    }

    @Test(expected = NullPointerException.class)
    public void createAccountUserNullTest() throws Exception {
        sessionService.createAccount(null);

        fail("No exception thrown");
    }

    @Test(expected = NullPointerException.class)
    public void createAccountUsernameNullTest() throws Exception {
        User user = buildUserTest();
        user.setUsername(null);
        sessionService.createAccount(user);

        fail("No exception thrown");
    }

    @Test(expected = NullPointerException.class)
    public void createAccountPasswordNullTest() throws Exception {
        User user = buildUserTest();
        user.setPassword(null);
        sessionService.createAccount(user);

        fail("No exception thrown");
    }

    @Test(expected = NullPointerException.class)
    public void createAccountNameNullTest() throws Exception {
        User user = buildUserTest();
        user.setName(null);
        sessionService.createAccount(user);

        fail("No exception thrown");
    }

    @Test(expected = NullPointerException.class)
    public void createAccountRoleNullTest() throws Exception {
        User user = buildUserTest();
        user.setRole(null);
        sessionService.createAccount(user);

        fail("No exception thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccountInvalidPasswordTest() throws Exception {
        User user = buildUserTest();
        user.setPassword("123");
        sessionService.createAccount(user);

        fail("No exception thrown");
    }

    private User buildUserTest() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setName("name");
        user.setRole(Role.ADMIN);
        return user;
    }
}