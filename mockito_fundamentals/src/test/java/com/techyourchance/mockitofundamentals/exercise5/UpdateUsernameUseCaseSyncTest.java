package com.techyourchance.mockitofundamentals.exercise5;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

public class UpdateUsernameUseCaseSyncTest {

    public static final String USER_ID = "user_id";

    public static final String USER_NAME = "user_name";

    UsersCache mUsersCacheMock;
    UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSyncMock;
    EventBusPoster mEventBusPosterMock;

    UpdateUsernameUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        mUsersCacheMock = mock(UsersCache.class);
        mUpdateUsernameHttpEndpointSyncMock = mock(UpdateUsernameHttpEndpointSync.class);
        mEventBusPosterMock = mock(EventBusPoster.class);
        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameHttpEndpointSyncMock,mUsersCacheMock,mEventBusPosterMock);
        success();
    }

    @Test
    public void updateUsername_success_userIDandUsernamePassedToEndpoint() throws Exception{
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verify(mUpdateUsernameHttpEndpointSyncMock,times(1)).updateUsername(ac.capture(),ac.capture());
        List<String> captures = ac.getAllValues();
        MatcherAssert.assertThat(captures.get(0), is(USER_ID));
        MatcherAssert.assertThat(captures.get(1), is(USER_NAME));
    }

    @Test
    public void updateUsername_success_userNameCached() throws Exception {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verify(mUsersCacheMock,times(1)).cacheUser(ac.capture());
        MatcherAssert.assertThat(ac.getValue().getUsername(), is(USER_NAME));
    }
    @Test
    public void updateUsername_success_userIDCached() throws Exception {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verify(mUsersCacheMock, times(1)).cacheUser(ac.capture());
        MatcherAssert.assertThat(ac.getValue().getUserId(), is(USER_ID));
    }
    @Test
    public void updateUsername_generalError_userNotCahed() throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateUsername_authError_userNotCahed() throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateUsername_serverError_userNotCahed() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateUsername_success_loggedInEventPosted() throws Exception{
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        MatcherAssert.assertThat(ac.getValue(),is(instanceOf(UserDetailsChangedEvent.class)));
    }

    @Test
    public void updateUsername_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }
    @Test
    public void updateUsername_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }
    @Test
    public void updateUsername_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }
    @Test
    public void updateUsername_success_networkErrorReturned() throws Exception{
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        MatcherAssert.assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }
    @Test
    public void updateUsername_networkError_networkErrorReturned() throws Exception{
        networkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        MatcherAssert.assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }
    @Test
    public void updateUsername_generalError_failureReturned() throws Exception{
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        MatcherAssert.assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }
    @Test
    public void updateUsername_authError_failureReturned() throws Exception{
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        MatcherAssert.assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }
    @Test
    public void updateUsername_serverError_failureReturned() throws Exception{
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        MatcherAssert.assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    private void success() throws com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS, USER_ID,USER_NAME));
    }

    private void generalError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, "",""));
    }

    private void authError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,"",""));
    }

    private void serverError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "",""));
    }
    private void networkError() throws Exception{
        doThrow(new NetworkErrorException())
                .when(mUpdateUsernameHttpEndpointSyncMock).updateUsername(any(String.class),any(String.class));
    }
}

