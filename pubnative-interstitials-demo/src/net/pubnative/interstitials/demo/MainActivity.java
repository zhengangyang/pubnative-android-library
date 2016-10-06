package net.pubnative.interstitials.demo;

import net.pubnative.interstitials.PubNativeInterstitials;
import net.pubnative.interstitials.api.PubNativeInterstitialsListener;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.demo.activity.AbstractDemoActivity;
import net.pubnative.interstitials.demo.activity.BannerActivity;
import net.pubnative.interstitials.demo.activity.CarouselActivity;
import net.pubnative.interstitials.demo.activity.IconActivity;
import net.pubnative.interstitials.demo.activity.InFeedVideoActivity;
import net.pubnative.interstitials.demo.activity.ListItemBriefActivity;
import net.pubnative.interstitials.demo.activity.ListItemFullActivity;
import net.pubnative.interstitials.demo.activity.VideoBannerActivity;
import net.pubnative.interstitials.demo.misc.DialogFactory;
import net.pubnative.interstitials.demo.misc.DialogFactory.SettingsDialogListener;
import net.pubnative.interstitials.persist.InMem;
import net.pubnative.library.model.response.NativeAd;

import net.pubnative.interstitials.demo.Contract;
import net.pubnative.interstitials.demo.R;

import org.droidparts.activity.legacy.Activity;
import org.droidparts.util.L;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements
        OnClickListener,
        PubNativeInterstitialsListener
{
    private DialogFactory dialogFactory;

    @Override
    public void onPreInject()
    {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dialogFactory = new DialogFactory(this);
        PubNativeInterstitials.init(this, Contract.APP_TOKEN);
        PubNativeInterstitials.addListener(this);
        for (int id : new int[]
        { R.id.btn_settings, R.id.btn_interstitial, R.id.btn_video_interstitial, R.id.btn_banner, R.id.btn_icon, R.id.btn_list_item_brief, R.id.btn_list_item_full, R.id.btn_carousel, R.id.btn_video_banner, R.id.btn_in_feed_video })
        {
            findViewById(id).setOnClickListener(this);
        }
        InMem.appKey = Contract.APP_TOKEN;
    }

    @Override
    public void onClick(View v)
    {
        Class<? extends AbstractDemoActivity> cls = null;
        switch (v.getId())
        {
        case R.id.btn_settings:
            dialogFactory.getSettingsDialog(adCount, l).show();
        break;
        case R.id.btn_interstitial:
            PubNativeInterstitials.show(this, PubNativeInterstitialsType.INTERSTITIAL, adCount);
        break;
        case R.id.btn_video_interstitial:
            PubNativeInterstitials.show(this, PubNativeInterstitialsType.VIDEO_INTERSTITIAL, adCount);
        break;
        case R.id.btn_banner:
            cls = BannerActivity.class;
        break;
        case R.id.btn_icon:
            cls = IconActivity.class;
        break;
        case R.id.btn_list_item_brief:
            cls = ListItemBriefActivity.class;
        break;
        case R.id.btn_list_item_full:
            cls = ListItemFullActivity.class;
        break;
        case R.id.btn_carousel:
            cls = CarouselActivity.class;
        break;
        case R.id.btn_video_banner:
            cls = VideoBannerActivity.class;
        break;
        case R.id.btn_in_feed_video:
            cls = InFeedVideoActivity.class;
        break;
        }
        if (cls != null)
        {
            startActivity(AbstractDemoActivity.getIntent(this, cls, adCount));
        }
    }

    private final SettingsDialogListener l       = new SettingsDialogListener()
                                                 {
                                                     @Override
                                                     public void onAdCountChanged(int count)
                                                     {
                                                         adCount = count;
                                                     }
                                                 };
    private int                          adCount = 5;

    //
    @Override
    public void onShown(PubNativeInterstitialsType type)
    {
        L.i("Shown %s.", type);
    }

    @Override
    public void onTapped(NativeAd ad)
    {
        L.i("Tapped %s.", ad);
    }

    @Override
    public void onClosed(PubNativeInterstitialsType type)
    {
        L.i("Closed %s", type);
    }

    @Override
    public void onError(Exception ex)
    {
        L.w(ex);
    }
}
