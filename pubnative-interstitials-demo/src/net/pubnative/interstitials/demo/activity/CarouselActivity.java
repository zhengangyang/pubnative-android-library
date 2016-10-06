package net.pubnative.interstitials.demo.activity;

import net.pubnative.interstitials.widget.AdCarouselView;
import net.pubnative.library.model.holder.NativeAdHolder;
import android.os.Bundle;

public class CarouselActivity extends
        AbstractSingleHolderListActivity<AdCarouselView> implements
        AdCarouselView.Listener
{
    private NativeAdHolder[] holders;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        show();
    }

    @Override
    protected AdCarouselView makeView()
    {
        AdCarouselView carouselView = new AdCarouselView(this);
        carouselView.setListener(this);
        holders = carouselView.createAndAddHolders(adCount);
        return carouselView;
    }

    @Override
    protected NativeAdHolder[] getAdHolders()
    {
        return holders;
    }

    @Override
    public void didClick(NativeAdHolder holder)
    {
        showInPlayStore(holder.ad);
    }
}
