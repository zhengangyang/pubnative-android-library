package net.pubnative.library.tracking;

import android.content.Context;
import android.os.Handler;

import net.pubnative.library.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 21)
public class PubnativeImpressionTrackerTest {

    Context applicationContext;

    @Before
    public void setUp() {

        this.applicationContext = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void testWithValidListener() {

        PubnativeImpressionTracker.Listener listener = spy(PubnativeImpressionTracker.Listener.class);
        PubnativeImpressionTracker impressionTracker = spy(PubnativeImpressionTracker.class);
        impressionTracker.mHandler = new Handler();
        impressionTracker.mListener = listener;
        impressionTracker.invokeOnTrackerImpression();
        verify(listener, times(1)).onImpressionDetected(null);
    }

    @Test
    public void testWithNullListener() {

        PubnativeImpressionTracker impressionTracker = spy(PubnativeImpressionTracker.class);
        impressionTracker.mHandler = new Handler();
        impressionTracker.invokeOnTrackerImpression();
    }
}
