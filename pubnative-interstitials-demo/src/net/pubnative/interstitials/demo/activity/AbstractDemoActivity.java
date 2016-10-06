package net.pubnative.interstitials.demo.activity;

import net.pubnative.interstitials.demo.Contract;
import net.pubnative.library.PubNative;
import net.pubnative.library.PubNativeListener;
import net.pubnative.library.model.APIEndpoint;
import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.NativeAd;

import org.droidparts.activity.Activity;
import org.droidparts.annotation.inject.InjectBundleExtra;
import org.droidparts.util.L;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class AbstractDemoActivity extends Activity implements
        PubNativeListener
{
    public static Intent getIntent(Context ctx, Class<? extends AbstractDemoActivity> cls, int adCount)
    {
        Intent in = new Intent(ctx, cls);
        in.putExtra(COUNT, adCount);
        return in;
    }

    static final String COUNT = "count";
    @InjectBundleExtra(key = COUNT)
    protected int       adCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        PubNative.setListener(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        PubNative.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        PubNative.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PubNative.setImageReshaper(null);
        PubNative.onDestroy();
    }

    //
    protected final void show()
    {
        AdRequest req = new AdRequest(Contract.APP_TOKEN, getAdFormat());
        req.fillInDefaults(this);
        req.setAdCount(adCount);
        PubNative.showAd(req, getAdHolders());
    }

    protected APIEndpoint getAdFormat()
    {
        return APIEndpoint.NATIVE;
    }

    protected abstract AdHolder<?>[] getAdHolders();

    //
    public static boolean backgroundRedirectEnabled = true;

    protected final void showInPlayStore(NativeAd ad)
    {
        if (backgroundRedirectEnabled)
        {
            PubNative.showInPlayStoreViaDialog(this, ad);
        }
        else
        {
            PubNative.showInPlayStoreViaBrowser(this, ad);
        }
    }

    //
    @Override
    public void onLoaded()
    {
        L.i("Loaded.");
    }

    @Override
    public void onError(Exception ex)
    {
        L.e(ex);
    }
}
