package com.crossover.business;

import com.crossover.business.services.api.IUserService;
import com.crossover.business.services.impl.UserService;
import com.crossover.common.model.common.User;
import com.crossover.common.model.utils.TestUtils;
import com.crossover.persistence.managers.api.IUsersManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UserServiceUnitTest {

    /** User's manager mock **/
    private IUsersManager usersManagerMock;

    /** UserService **/
    private IUserService userService;

    @Before
    public void setup() throws Exception {
        userService = new UserService();
        usersManagerMock = EasyMock.createNiceMock(IUsersManager.class);
        Field field = UserService.class.getDeclaredField("mUsersManager");
        field.setAccessible(true);
        field.set(userService, usersManagerMock);
    }

    @Test
    public void findUserByIdTest() throws Exception {
        EasyMock.reset(usersManagerMock);

        User userTest = TestUtils.buildTestUser();
        userTest.setId(1);
        EasyMock.expect(usersManagerMock.findById(userTest.getId())).andReturn(userTest);

        EasyMock.replay(usersManagerMock);

        User user = userService.findUserById(userTest.getId());

        assertNotNull(user);
        assertEquals(userTest.getId(), user.getId());
        EasyMock.verify(usersManagerMock);
    }

    @Test
    public void findAllUsersTest() throws Exception {
        EasyMock.reset(usersManagerMock);

        List<User> users = new ArrayList<>();
        users.add(TestUtils.buildTestUser());
        users.add(TestUtils.buildTestUser());
        users.add(TestUtils.buildTestUser());
        EasyMock.expect(usersManagerMock.all()).andReturn(users).times(1);

        EasyMock.replay(usersManagerMock);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(users, result);
        EasyMock.verify(usersManagerMock);
    }

}
