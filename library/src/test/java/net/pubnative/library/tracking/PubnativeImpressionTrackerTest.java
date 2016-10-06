package net.pubnative.library.tracking;

import android.content.Context;
import android.view.View;

import net.pubnative.library.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.eq;
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
        View adView = spy(new View(applicationContext));
        PubnativeImpressionTracker impressionTracker = spy(new PubnativeImpressionTracker(adView, listener));

        impressionTracker.invokeOnTrackerImpression();
        verify(listener, times(1)).onImpressionDetected(eq(adView));
    }

    @Test
    public void testWithNullListener() {

        View adView = spy(new View(applicationContext));
        PubnativeImpressionTracker impressionTracker = spy(new PubnativeImpressionTracker(adView, null));

        impressionTracker.invokeOnTrackerImpression();
    }
}
