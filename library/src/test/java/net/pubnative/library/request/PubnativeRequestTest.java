// The MIT License (MIT)
//
// Copyright (c) 2016 PubNative GmbH
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package net.pubnative.library.request;

import android.content.Context;

import net.pubnative.AdvertisingIdClient;
import net.pubnative.library.BuildConfig;
import net.pubnative.library.PubnativeTestUtils;
import net.pubnative.library.network.PubnativeHttpRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 21)
public class PubnativeRequestTest {

    private Context applicationContext;

    @Before
    public void setUp() {

        this.applicationContext = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void testWithValidListenerForSuccess() {

        PubnativeRequest.Listener listener = spy(PubnativeRequest.Listener.class);
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mListener = listener;
        request.invokeOnSuccess(mock(ArrayList.class));
        verify(listener, times(1)).onPubnativeRequestSuccess(eq(request), any(List.class));
    }

    @Test
    public void testWithValidListenerForFailure() {

        Exception error = mock(Exception.class);
        PubnativeRequest.Listener listener = spy(PubnativeRequest.Listener.class);
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mListener = listener;
        request.invokeOnFail(error);
        verify(listener, times(1)).onPubnativeRequestFailed(eq(request), eq(error));
    }

    @Test
    public void testWithNoListenerForSuccess() {

        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mListener = null;
        request.invokeOnSuccess(mock(ArrayList.class));
    }

    @Test
    public void testWithNoListenerForFailure() {

        PubnativeRequest request = spy(PubnativeRequest.class);
        Exception error = mock(Exception.class);
        request.mListener = null;
        request.invokeOnFail(error);
    }

    @Test
    public void testParameterIsSet() {

        String testKey = "testKey";
        String testValue = "testValue";
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.setParameter(testKey, testValue);
        assertThat(request.mRequestParameters.get(testKey)).isEqualTo(testValue);
    }

    @Test
    public void testWithNullParameters() {

        PubnativeRequest request = spy(PubnativeRequest.class);
        String testKey = "testKey";
        request.setParameter(testKey, null);
        assertThat(request.mRequestParameters.containsKey(testKey)).isFalse();
    }

    @Test
    public void testParameterSize() {

        PubnativeRequest request = spy(PubnativeRequest.class);
        request.setParameter("test1", "1");
        request.setParameter("test2", "2");
        assertThat(request.mRequestParameters.size() == 2).isTrue();
    }

    @Test
    public void testDuplicateParametersOverridesValue() {

        String testKey = "testKey";
        String testValue1 = "value1";
        String testValue2 = "value2";
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.setParameter(testKey, testValue1);
        request.setParameter(testKey, testValue2);
        assertThat(request.mRequestParameters.size()).isEqualTo(1);
        assertThat(request.mRequestParameters.get(testKey)).isEqualTo(testValue2);
    }

    @Test
    public void testNetworkRequestInitiatedOnStart() {

        PubnativeRequest.Listener listener = spy(PubnativeRequest.Listener.class);
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mListener = listener;
        request.start(this.applicationContext, listener);
        verify(request, times(1)).fillDefaultParameters();
    }

    @Test
    public void test_start_withNullContext_pass() {

        PubnativeRequest.Listener listener = mock(PubnativeRequest.Listener.class);
        PubnativeRequest pubnativeRequest = spy(PubnativeRequest.class);
        pubnativeRequest.setParameter(PubnativeRequest.Parameters.ANDROID_ADVERTISER_ID, "test");
        pubnativeRequest.start(null, listener);
    }

    @Test
    public void test_start_withNullListener_pass() {

        PubnativeRequest pubnativeRequest = spy(PubnativeRequest.class);
        pubnativeRequest.setParameter(PubnativeRequest.Parameters.ANDROID_ADVERTISER_ID, "test");
        pubnativeRequest.start(RuntimeEnvironment.application.getApplicationContext(), null);
    }

    @Test
    public void test_start_withRunningRequest_pass() {

        PubnativeRequest.Listener listener = mock(PubnativeRequest.Listener.class);
        PubnativeRequest pubnativeRequest = spy(PubnativeRequest.class);
        pubnativeRequest.setParameter(PubnativeRequest.Parameters.ANDROID_ADVERTISER_ID, "test");
        pubnativeRequest.mIsRunning = true;
        pubnativeRequest.start(RuntimeEnvironment.application.getApplicationContext(), listener);
    }

    @Test
    public void testSetsUpDefaultParametersAutomatically() {

        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mContext = this.applicationContext;
        request.fillDefaultParameters();
        assertThat(request.mRequestParameters.containsKey(PubnativeRequest.Parameters.OS)).isTrue();
        assertThat(request.mRequestParameters.containsKey(PubnativeRequest.Parameters.OS_VERSION)).isTrue();
        assertThat(request.mRequestParameters.containsKey(PubnativeRequest.Parameters.DEVICE_MODEL)).isTrue();
        assertThat(request.mRequestParameters.containsKey(PubnativeRequest.Parameters.LOCALE)).isTrue();
    }

    @Test
    public void testRequestUrlValidity() {

        String testKey = "testKey";
        String testValue = "testValue";
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.setParameter(testKey, testValue);
        String url = request.getRequestURL();
        assertThat(url).isNotNull();
        assertThat(url).isNotEmpty();
        assertThat(url).startsWith(PubnativeRequest.BASE_URL);
        assertThat(url).contains(testKey);
    }

    @Test
    public void testOnResponseSuccess() {

        String result = PubnativeTestUtils.getResponseJSON("success.json");
        PubnativeRequest.Listener listener = spy(PubnativeRequest.Listener.class);
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mListener = listener;
        request.onPubnativeHttpRequestFinish(null, result, PubnativeHttpRequest.HTTP_OK);
        verify(listener, times(1)).onPubnativeRequestSuccess(eq(request), any(List.class));
    }

    @Test
    public void testOnResponseWithInvalidData() {


        String result = PubnativeTestUtils.getResponseJSON("failure.json");
        PubnativeRequest.Listener listener = spy(PubnativeRequest.Listener.class);
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mListener = listener;
        request.onPubnativeHttpRequestFinish(null, result, PubnativeHttpRequest.HTTP_INVALID_REQUEST);
        verify(listener, times(1)).onPubnativeRequestFailed(eq(request), any(Exception.class));
    }

    @Test
    public void testOnResponseWithNullData() {

        PubnativeRequest.Listener listener = spy(PubnativeRequest.Listener.class);
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mListener = listener;
        request.onPubnativeHttpRequestFinish(null, null, 0);
        verify(listener, times(1)).onPubnativeRequestFailed(eq(request), any(Exception.class));
    }

    @Test
    public void testOnErrorResponseFromRequestManager() {

        Exception error = mock(Exception.class);
        PubnativeRequest.Listener listener = spy(PubnativeRequest.Listener.class);
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mListener = listener;
        request.onPubnativeHttpRequestFail(null, error);
        verify(listener, times(1)).onPubnativeRequestFailed(eq(request), eq(error));
    }

    @Test
    public void setCoppaMode_enable_shouldAvoidSettingSomeDefaultParameters() {
        PubnativeRequest request = spy(PubnativeRequest.class);
        request.mContext = this.applicationContext;
        request.setCoppaMode(true);
        request.fillDefaultParameters();
        verify(request, never()).setAdvertisingID(any(AdvertisingIdClient.AdInfo.class));
    }
}
