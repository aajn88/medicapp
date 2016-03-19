package com.crossover.business;

import com.crossover.business.services.api.ICalendarService;
import com.crossover.business.services.api.ISessionService;
import com.crossover.business.services.impl.CalendarService;
import com.crossover.common.model.common.Event;
import com.crossover.common.model.common.Invitation;
import com.crossover.common.model.common.User;
import com.crossover.common.model.utils.TestUtils;
import com.crossover.persistence.managers.api.IEventsManager;
import com.crossover.persistence.managers.api.IInvitationsManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CalendarServiceUnitTest {

    /** Event's manager mock **/
    private IEventsManager eventsManagerMock;

    /** Invitation's manager mock **/
    private IInvitationsManager invitationsManagerMock;

    /** SessionService mock **/
    private ISessionService sessionServiceMock;

    /** Calendar service **/
    private ICalendarService calendarService;

    @Before
    public void setup() throws Exception {
        calendarService = new CalendarService();

        eventsManagerMock = EasyMock.createNiceMock(IEventsManager.class);
        Field field = CalendarService.class.getDeclaredField("mEventsManager");
        field.setAccessible(true);
        field.set(calendarService, eventsManagerMock);

        invitationsManagerMock = EasyMock.createNiceMock(IInvitationsManager.class);
        field = CalendarService.class.getDeclaredField("mInvitationsManager");
        field.setAccessible(true);
        field.set(calendarService, invitationsManagerMock);

        sessionServiceMock = EasyMock.createNiceMock(ISessionService.class);
        field = CalendarService.class.getDeclaredField("mSessionService");
        field.setAccessible(true);
        field.set(calendarService, sessionServiceMock);
    }

    @Test(expected = NullPointerException.class)
    public void createEventEventNullTest() throws Exception {
        calendarService.createEvent(null);

        fail("Exception not thrown");
    }

    @Test(expected = NullPointerException.class)
    public void createEventNameNullTest() throws Exception {
        Event event = TestUtils.buildTestEvent();
        event.setName(null);
        calendarService.createEvent(event);

        fail("Exception not thrown");
    }

    @Test(expected = NullPointerException.class)
    public void createEventStartDateNullTest() throws Exception {
        Event event = TestUtils.buildTestEvent();
        event.setStartDate(null);
        calendarService.createEvent(event);

        fail("Exception not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEventEmptyNameNullTest() throws Exception {
        Event event = TestUtils.buildTestEvent();
        event.setName("");
        calendarService.createEvent(event);

        fail("Exception not thrown");
    }

    @Test
    public void createEventNotCreatedTest() throws Exception {
        EasyMock.reset(eventsManagerMock);

        EasyMock.expect(eventsManagerMock.createOrUpdate(EasyMock.anyObject(Event.class)))
                .andReturn(false);

        EasyMock.replay(eventsManagerMock);

        boolean success = calendarService.createEvent(TestUtils.buildTestEvent());

        assertEquals(success, false);
        EasyMock.verify(eventsManagerMock);
    }

    @Test
    public void createEventNoAttendantsTest() throws Exception {
        EasyMock.reset(eventsManagerMock);

        EasyMock.expect(eventsManagerMock.createOrUpdate(EasyMock.anyObject(Event.class)))
                .andReturn(true);

        EasyMock.replay(eventsManagerMock);

        Event event = TestUtils.buildTestEvent();
        event.setAttendantsIds(null);
        boolean success = calendarService.createEvent(event);

        assertEquals(success, true);
        EasyMock.verify(eventsManagerMock);
    }

    @Test
    public void createEventWithAttendantsTest() throws Exception {
        EasyMock.reset(eventsManagerMock, invitationsManagerMock, sessionServiceMock);

        EasyMock.expect(eventsManagerMock.createOrUpdate(EasyMock.anyObject(Event.class)))
                .andReturn(true);

        EasyMock.expect(
                invitationsManagerMock.findByEventIdAndUserId(EasyMock.anyInt(), EasyMock.anyInt()))
                .andReturn(null).times(2);

        EasyMock.expect(
                invitationsManagerMock.findByEventIdAndUserId(EasyMock.anyInt(), EasyMock.anyInt()))
                .andReturn(new Invitation()).anyTimes();

        List<Invitation> invitations = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Invitation invitation = new Invitation(i, i);
            invitation.setId(i);
            invitations.add(invitation);
        }

        EasyMock.expect(invitationsManagerMock.findByEventId(EasyMock.anyInt()))
                .andReturn(invitations).times(1);

        EasyMock.expect(invitationsManagerMock.deleteById(0)).andReturn(new Invitation()).times(1);

        User user = TestUtils.buildTestUser();
        user.setId(1);
        EasyMock.expect(sessionServiceMock.getCurrentSession()).andReturn(user).times(1);

        EasyMock.replay(eventsManagerMock, invitationsManagerMock, sessionServiceMock);

        Event event = TestUtils.buildTestEvent();
        event.setId(1);
        boolean success = calendarService.createEvent(event);

        assertEquals(success, true);
        EasyMock.verify(eventsManagerMock, invitationsManagerMock, sessionServiceMock);
    }

    @Test
    public void findEventByIdTest() throws Exception {
        EasyMock.reset(eventsManagerMock);

        Event event = TestUtils.buildTestEvent();
        event.setId(1);
        EasyMock.expect(eventsManagerMock.findById(event.getId())).andReturn(event);

        EasyMock.replay(eventsManagerMock);

        Event result = calendarService.findEventById(1);

        assertEquals(result.getId(), event.getId());
        EasyMock.verify(eventsManagerMock);
    }

    @Test
    public void getPendingInvitationsTest() throws Exception {
        EasyMock.reset(eventsManagerMock, invitationsManagerMock, sessionServiceMock);

        User user = TestUtils.buildTestUser();
        user.setId(1);
        EasyMock.expect(sessionServiceMock.getCurrentSession()).andReturn(user).times(1);

        List<Invitation> invitations = new ArrayList<>();
        invitations.add(new Invitation(1, 1));
        invitations.add(new Invitation(2, 2));

        EasyMock.expect(invitationsManagerMock.findByUserIdAndAccepted(user.getId(), false))
                .andReturn(invitations).times(1);

        EasyMock.expect(eventsManagerMock.findById(1)).andReturn(TestUtils.buildTestEvent())
                .times(1);
        EasyMock.expect(eventsManagerMock.findById(2)).andReturn(TestUtils.buildTestEvent())
                .times(1);

        EasyMock.replay(eventsManagerMock, invitationsManagerMock, sessionServiceMock);

        List<Event> events = calendarService.getPendingInvitations();

        assertNotNull(events);
        assertEquals(2, events.size());
        EasyMock.verify(eventsManagerMock, invitationsManagerMock, sessionServiceMock);
    }

    @Test
    public void acceptRejectInvitationNoInvitationTest() throws Exception {
        EasyMock.reset(eventsManagerMock, sessionServiceMock, invitationsManagerMock);

        User user = TestUtils.buildTestUser();
        user.setId(1);
        EasyMock.expect(sessionServiceMock.getCurrentSession()).andReturn(user).times(1);

        EasyMock.expect(
                invitationsManagerMock.findByEventIdAndUserId(EasyMock.anyInt(), EasyMock.anyInt()))
                .andReturn(null).times(1);

        EasyMock.replay(eventsManagerMock, sessionServiceMock, invitationsManagerMock);

        boolean success = calendarService.acceptRejectInvitation(1, true);

        assertEquals(false, success);
        EasyMock.verify(eventsManagerMock, sessionServiceMock, invitationsManagerMock);
    }

    @Test
    public void acceptRejectInvitationAcceptInvitationTest() throws Exception {
        EasyMock.reset(eventsManagerMock, sessionServiceMock, invitationsManagerMock);

        User user = TestUtils.buildTestUser();
        user.setId(1);
        EasyMock.expect(sessionServiceMock.getCurrentSession()).andReturn(user).times(1);

        EasyMock.expect(
                invitationsManagerMock.findByEventIdAndUserId(EasyMock.anyInt(), EasyMock.anyInt()))
                .andReturn(new Invitation()).times(1);

        EasyMock.expect(invitationsManagerMock.createOrUpdate(EasyMock.anyObject(Invitation.class)))
                .andReturn(true).times(1);

        EasyMock.replay(eventsManagerMock, sessionServiceMock, invitationsManagerMock);

        boolean success = calendarService.acceptRejectInvitation(1, true);

        assertEquals(true, success);
        EasyMock.verify(eventsManagerMock, sessionServiceMock, invitationsManagerMock);
    }

    @Test
    public void acceptRejectInvitationRejectInvitationTest() throws Exception {
        EasyMock.reset(eventsManagerMock, sessionServiceMock, invitationsManagerMock);

        User user = TestUtils.buildTestUser();
        user.setId(1);
        EasyMock.expect(sessionServiceMock.getCurrentSession()).andReturn(user).times(1);

        Invitation invitation = new Invitation();
        invitation.setId(1);
        EasyMock.expect(
                invitationsManagerMock.findByEventIdAndUserId(EasyMock.anyInt(), EasyMock.anyInt()))
                .andReturn(invitation).times(1);

        EasyMock.expect(invitationsManagerMock.deleteById(EasyMock.anyInt())).andReturn(null)
                .times(1);

        EasyMock.expect(eventsManagerMock.findById(1)).andReturn(TestUtils.buildTestEvent())
                .times(1);

        EasyMock.expect(eventsManagerMock.createOrUpdate(EasyMock.anyObject(Event.class)))
                .andReturn(true).times(1);

        EasyMock.replay(eventsManagerMock, sessionServiceMock, invitationsManagerMock);

        boolean success = calendarService.acceptRejectInvitation(1, false);

        assertEquals(true, success);
        EasyMock.verify(eventsManagerMock, sessionServiceMock, invitationsManagerMock);
    }

    @Test
    public void getDayEventsTest() throws Exception {
        EasyMock.reset(invitationsManagerMock, sessionServiceMock, eventsManagerMock);

        User user = TestUtils.buildTestUser();
        user.setId(1);
        EasyMock.expect(sessionServiceMock.getCurrentSession()).andReturn(user).times(1);

        List<Event> events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Event event = TestUtils.buildTestEvent();
            event.setId(i);
            events.add(event);
        }
        EasyMock.expect(eventsManagerMock
                .findBetweenDates(EasyMock.anyObject(Date.class), EasyMock.anyObject(Date.class)))
                .andReturn(events).times(1);

        EasyMock.expect(invitationsManagerMock.findByEventIdAndUserId(0, 1)).andReturn(null)
                .times(1);
        Invitation invitation = new Invitation(1, 1);
        invitation.setAccepted(true);
        EasyMock.expect(invitationsManagerMock.findByEventIdAndUserId(1, 1)).andReturn(invitation)
                .times(1);
        EasyMock.expect(invitationsManagerMock.findByEventIdAndUserId(2, 1)).andReturn(null)
                .times(1);

        EasyMock.replay(invitationsManagerMock, sessionServiceMock, eventsManagerMock);

        List<Event> result = calendarService.getDayEvents(2016, 3, 19);

        assertNotNull(result);
        assertEquals(false, result.isEmpty());
        assertEquals(1, (int) result.get(0).getId());
        EasyMock.verify(invitationsManagerMock, sessionServiceMock, eventsManagerMock);
    }

}
