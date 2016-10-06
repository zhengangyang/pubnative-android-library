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
public class LargeLayoutWidgetTest {

    @Test
    public void invokeLoadFinish_withValidListener_shouldCallLoadFinish() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        largeLayoutWidget.mListener = listener;

        largeLayoutWidget.invokeLoadFinish();

        verify(listener).onPubnativeBaseLayoutLoadFinish();
    }

    @Test
    public void invokeLoadFinish_withNullListener_shouldPass() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        largeLayoutWidget.mListener = null;

        largeLayoutWidget.invokeLoadFinish();
    }

    @Test
    public void invokeLoadFail_withValidListener_shouldCallOnLoadFail() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        largeLayoutWidget.mListener = listener;

        Exception ex = mock(Exception.class);
        largeLayoutWidget.invokeLoadFail(ex);

        verify(listener).onPubnativeBaseLayoutLoadFail(eq(ex));
    }

    @Test
    public void invokeLoadFail_withNullListener_shouldPass() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        largeLayoutWidget.mListener = null;

        Exception ex = mock(Exception.class);
        largeLayoutWidget.invokeLoadFail(ex);
    }

    @Test
    public void invokeClick_withValidListener_shouldCallClick() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        largeLayoutWidget.mListener = listener;

        largeLayoutWidget.invokeClick();

        verify(listener).onPubnativeBaseLayoutClick();
    }

    @Test
    public void invokeClick_withNullListener_shouldPass() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        largeLayoutWidget.mListener = null;

        largeLayoutWidget.invokeClick();
    }

    @Test
    public void invokeHide_withValidListener_shouldCallHide() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        largeLayoutWidget.mListener = listener;

        largeLayoutWidget.invokeHide();

        verify(listener).onPubnativeBaseLayoutHide();
    }

    @Test
    public void invokeHide_withNullListener_shouldPass() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        largeLayoutWidget.mListener = null;

        largeLayoutWidget.invokeHide();
    }

    @Test
    public void invokeImpressionConfirmed_withValidListener_shouldCallImpressionConfirmed() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        largeLayoutWidget.mListener = listener;

        largeLayoutWidget.invokeImpressionConfirmed();

        verify(listener).onPubnativeBaseLayoutImpressionConfirmed();
    }

    @Test
    public void invokeImpressionConfirmed_withNullListener_shouldPass() {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        largeLayoutWidget.mListener = null;

        largeLayoutWidget.invokeImpressionConfirmed();
    }

    @Test
    public void hide_withValidListener_shouldCallHide()  {

        LargeLayoutWidget largeLayoutWidget = spy(new LargeLayoutWidget(RuntimeEnvironment.application.getApplicationContext()));
        BaseLayoutWidget.Listener listener = mock(BaseLayoutWidget.Listener.class);
        largeLayoutWidget.mListener = listener;

        PubnativeAdModel adModel = mock(PubnativeAdModel.class);
        largeLayoutWidget.setModel(adModel);
        largeLayoutWidget.hide();

        verify(listener).onPubnativeBaseLayoutHide();
    }
}
