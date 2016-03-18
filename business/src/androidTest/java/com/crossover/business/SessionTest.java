package com.crossover.business;

import com.crossover.business.services.api.ISessionService;
import com.crossover.business.services.impl.SessionService;
import com.crossover.common.model.common.User;
import com.crossover.persistence.managers.api.IUsersManager;
import com.google.inject.AbstractModule;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:antonio-jimenez@accionplus.com">Antonio A. Jimenez N.</a>
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SessionTest {

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
    private void checkAvailableUsernameSuccessTest() {
        EasyMock.reset(usersManagerMock);

        EasyMock.expect(usersManagerMock.findByUsername("123456")).andReturn(new User());

        EasyMock.replay(usersManagerMock);

        boolean success = sessionService.checkAvailableUsername("123456");

        Assert.assertEquals(success, true);
        EasyMock.verify(usersManagerMock);
    }

    /**
     * Test module for injections
     */
    public class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(IUsersManager.class).toInstance(usersManagerMock);
        }
    }

}
