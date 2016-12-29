package net.pubnative.library.layouts.widget;

import net.pubnative.library.layouts.BuildConfig;
import net.pubnative.library.request.model.PubnativeAdModel;

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
public class MediumLayoutWidgetTest {

    @Test
    public void invokeLoadFinish_withValidListener_shouldCallLoadFinish() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        mediumLayoutWidget.mListener = listener;

        mediumLayoutWidget.invokeLoadFinish();

        verify(listener).onPubnativeBaseLayoutLoadFinish();
    }

    @Test
    public void invokeLoadFinish_withNullListener_shouldPass() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        mediumLayoutWidget.mListener = null;

        mediumLayoutWidget.invokeLoadFinish();
    }

    @Test
    public void invokeLoadFail_withValidListener_shouldCallOnLoadFail() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        mediumLayoutWidget.mListener = listener;

        Exception ex = mock(Exception.class);
        mediumLayoutWidget.invokeLoadFail(ex);

        verify(listener).onPubnativeBaseLayoutLoadFail(eq(ex));
    }

    @Test
    public void invokeLoadFail_withNullListener_shouldPass() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        mediumLayoutWidget.mListener = null;

        Exception ex = mock(Exception.class);
        mediumLayoutWidget.invokeLoadFail(ex);
    }

    @Test
    public void invokeClick_withValidListener_shouldCallClick() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        mediumLayoutWidget.mListener = listener;

        mediumLayoutWidget.invokeClick();

        verify(listener).onPubnativeBaseLayoutClick();
    }

    @Test
    public void invokeClick_withNullListener_shouldPass() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        mediumLayoutWidget.mListener = null;

        mediumLayoutWidget.invokeClick();
    }

    @Test
     public void invokeHide_withValidListener_shouldCallHide() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        mediumLayoutWidget.mListener = listener;

        mediumLayoutWidget.invokeHide();

        verify(listener).onPubnativeBaseLayoutHide();
    }

    @Test
    public void invokeHide_withNullListener_shouldPass() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        mediumLayoutWidget.mListener = null;

        mediumLayoutWidget.invokeHide();
    }

    @Test
    public void invokeImpressionConfirmed_withValidListener_shouldCallImpressionConfirmed() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        mediumLayoutWidget.mListener = listener;

        mediumLayoutWidget.invokeImpressionConfirmed();

        verify(listener).onPubnativeBaseLayoutImpressionConfirmed();
    }

    @Test
    public void invokeImpressionConfirmed_withNullListener_shouldPass() {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        mediumLayoutWidget.mListener = null;

        mediumLayoutWidget.invokeImpressionConfirmed();
    }

    @Test
    public void hide_withValidListener_shouldCallHide()  {

        MediumLayoutWidget mediumLayoutWidget = spy(new MediumLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        mediumLayoutWidget.mListener = listener;

        PubnativeAdModel adModel = mock(PubnativeAdModel.class);
        mediumLayoutWidget.setModel(adModel);
        mediumLayoutWidget.hide();

        verify(listener).onPubnativeBaseLayoutHide();
    }
}
