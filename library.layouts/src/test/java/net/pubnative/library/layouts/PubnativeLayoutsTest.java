package net.pubnative.library.layouts;

import android.os.Handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 16)
@RunWith(RobolectricTestRunner.class)
public class PubnativeLayoutsTest {

    @Test
    public void invokeLoadFail_withValidListener_shouldCallOnLoadFail() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        Exception ex = mock(Exception.class);
        assetLayout.invokeLoadFail(ex);

        verify(listener).onPubnativeLayoutLoadFailed(eq(assetLayout), eq(ex));
    }

    @Test
    public void invokeLoadFail_withNullListener_shouldPass() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        assetLayout.mHandler = new Handler();
        assetLayout.invokeLoadFail(mock(Exception.class));
    }

    @Test
    public void invokeLoadFinish_withValidListener_shouldCallOnFinish() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        assetLayout.invokeLoadFinish();

        verify(listener).onPubnativeLayoutLoadFinish(eq(assetLayout));
    }

    @Test
    public void invokeLoadFinish_withNullListener_shouldPass() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        assetLayout.mHandler = new Handler();
        assetLayout.invokeLoadFinish();
    }

    @Test
    public void invokeShow_withValidListener_shouldCallOnShow() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        assetLayout.invokeShow();

        verify(listener).onPubnativeLayoutShow(eq(assetLayout));
    }

    @Test
    public void invokeShow_withNullListener_shouldPass() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        assetLayout.mHandler = new Handler();
        assetLayout.invokeShow();
    }

    @Test
    public void invokeImpressionConfirmed_withValidListener_shouldCallOnImpressionConfirmed() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        assetLayout.invokeImpressionConfirmed();

        verify(listener).onPubnativeLayoutImpressionConfirmed(eq(assetLayout));
    }

    @Test
    public void invokeImpressionConfirmed_withValidListener_shouldPass() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        assetLayout.mHandler = new Handler();
        assetLayout.invokeImpressionConfirmed();
    }

    @Test
    public void invokeClick_withValidListener_shouldCallOnClick() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        assetLayout.invokeClick();

        verify(listener).onPubnativeLayoutClick(eq(assetLayout));
    }

    @Test
    public void invokeClick_withNullListener_shouldPass() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        assetLayout.mHandler = new Handler();
        assetLayout.invokeClick();
    }

    @Test
    public void invokeHide_withValidListener_shouldCallOnHide() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        assetLayout.invokeHide();

        verify(listener).onPubnativeLayoutHide(eq(assetLayout));
    }

    @Test
    public void invokeHide_withNullListener_shouldPass() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        assetLayout.mHandler = new Handler();
        assetLayout.invokeHide();
    }

    @Test
    public void load_withNullContext_callOnPubnativeLayoutLoadFail() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        assetLayout.load(null, "123456", "1", PubnativeLayouts.LayoutType.SMALL);

        verify(listener).onPubnativeLayoutLoadFailed(eq(assetLayout), any(Exception.class));
    }

    @Test
    public void load_withEmptyAppToken_callOnPubnativeLayoutLoadFail() {

        String appToken = "";

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        assetLayout.load(RuntimeEnvironment.application.getApplicationContext(), appToken, "1", PubnativeLayouts.LayoutType.SMALL);

        verify(listener).onPubnativeLayoutLoadFailed(eq(assetLayout), any(Exception.class));
    }

    @Test
    public void load_withEmptyZoneId_callOnPubnativeLayoutLoadFail() {

        String zoneId = "";

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        assetLayout.load(RuntimeEnvironment.application.getApplicationContext(), "123456", zoneId, PubnativeLayouts.LayoutType.SMALL);

        verify(listener).onPubnativeLayoutLoadFailed(eq(assetLayout), any(Exception.class));
    }

    @Test
    public void load_whenassetLayoutReady_callOnPubnativeLayoutLoadFinish() {

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        assetLayout.mHandler = new Handler();
        when(assetLayout.isReady()).thenReturn(true);
        assetLayout.load(RuntimeEnvironment.application.getApplicationContext(), "123456", "1", PubnativeLayouts.LayoutType.SMALL);

        verify(listener).onPubnativeLayoutLoadFinish(assetLayout);
    }

    @Test
    public void load_withNullHandler_createNewHandler(){

        PubnativeLayouts assetLayout = spy(PubnativeLayouts.class);
        PubnativeLayouts.Listener listener = mock(PubnativeLayouts.Listener.class);
        assetLayout.mListener = listener;
        when(assetLayout.isReady()).thenReturn(true);
        assetLayout.load(RuntimeEnvironment.application.getApplicationContext(), "123456", "1", PubnativeLayouts.LayoutType.SMALL);

        assertThat(assetLayout.mHandler).isNotNull();
    }
}