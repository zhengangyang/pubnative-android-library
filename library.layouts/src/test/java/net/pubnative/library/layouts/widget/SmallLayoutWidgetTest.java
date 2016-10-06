package net.pubnative.library.layouts.widget;

import android.content.Context;

import net.pubnative.library.layouts.BuildConfig;
import net.pubnative.library.request.model.PubnativeAdModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 16)
@RunWith(RobolectricTestRunner.class)
public class SmallLayoutWidgetTest {

    @Test
    public void invokeLoadFinish_withValidListener_shouldCallLoadFinish() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        smallLayoutWidget.mListener = listener;

        smallLayoutWidget.invokeLoadFinish();

        verify(listener).onPubnativeBaseLayoutLoadFinish();
    }

    @Test
    public void invokeLoadFinish_withNullListener_shouldPass() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        smallLayoutWidget.mListener = null;

        smallLayoutWidget.invokeLoadFinish();
    }

    @Test
    public void invokeLoadFail_withValidListener_shouldCallOnLoadFail() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        smallLayoutWidget.mListener = listener;

        Exception ex = mock(Exception.class);
        smallLayoutWidget.invokeLoadFail(ex);

        verify(listener).onPubnativeBaseLayoutLoadFail(eq(ex));
    }

    @Test
    public void invokeLoadFail_withNullListener_shouldPass() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        smallLayoutWidget.mListener = null;

        Exception ex = mock(Exception.class);
        smallLayoutWidget.invokeLoadFail(ex);
    }

    @Test
    public void invokeClick_withValidListener_shouldCallClick() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        smallLayoutWidget.mListener = listener;

        smallLayoutWidget.invokeClick();

        verify(listener).onPubnativeBaseLayoutClick();
    }

    @Test
    public void invokeClick_withNullListener_shouldPass() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        smallLayoutWidget.mListener = null;

        smallLayoutWidget.invokeClick();
    }

    @Test
    public void invokeHide_withValidListener_shouldCallHide() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        smallLayoutWidget.mListener = listener;

        smallLayoutWidget.invokeHide();

        verify(listener).onPubnativeBaseLayoutHide();
    }

    @Test
    public void invokeHide_withNullListener_shouldPass() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        smallLayoutWidget.mListener = null;

        smallLayoutWidget.invokeHide();
    }

    @Test
    public void invokeImpressionConfirmed_withValidListener_shouldCallImpressionConfirmed() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        smallLayoutWidget.mListener = listener;

        smallLayoutWidget.invokeImpressionConfirmed();

        verify(listener).onPubnativeBaseLayoutImpressionConfirmed();
    }

    @Test
    public void invokeImpressionConfirmed_withNullListener_shouldPass() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        smallLayoutWidget.mListener = null;

        smallLayoutWidget.invokeImpressionConfirmed();
    }

    @Test
    public void setModel_withValidListenerandAdModel_shouldCallLoadFinish() {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        smallLayoutWidget.mListener = listener;

        PubnativeAdModel adModel = mock(PubnativeAdModel.class);
        smallLayoutWidget.setModel(adModel);

        verify(listener).onPubnativeBaseLayoutLoadFinish();
    }

    @Test
    public void hide_withValidListener_shouldCallHide()  {

        SmallLayoutWidget smallLayoutWidget = spy(new SmallLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        smallLayoutWidget.mListener = listener;

        PubnativeAdModel adModel = mock(PubnativeAdModel.class);
        smallLayoutWidget.setModel(adModel);
        smallLayoutWidget.hide();

        verify(listener).onPubnativeBaseLayoutHide();
    }
}
