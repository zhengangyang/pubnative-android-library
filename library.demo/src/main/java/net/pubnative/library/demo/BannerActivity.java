package net.pubnative.library.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import net.pubnative.library.banner.PubnativeBanner;
import net.pubnative.library.demo.utils.Settings;

public class BannerActivity extends Activity implements PubnativeBanner.Listener {

    public static final String TAG = BannerActivity.class.getSimpleName();
    private RelativeLayout  mLoaderContainer;
    private RadioButton     mBottomPosition;
    private RadioButton     mSmallBanner;
    private RadioGroup      mSizeGroup;
    private RadioGroup      mPositionGroup;
    private PubnativeBanner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        mLoaderContainer = (RelativeLayout) findViewById(R.id.activity_native_container_loader);
        mSizeGroup = (RadioGroup) findViewById(R.id.banner_size);
        mPositionGroup = (RadioGroup) findViewById(R.id.banner_position);
        mBottomPosition = (RadioButton) findViewById(R.id.rb_bottom);
        mSmallBanner = (RadioButton) findViewById(R.id.rb_height_50);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBanner != null){
            mBanner.destroy();
        }
    }

    public void onRequestClick(View v) {
        int selectedPositionId = mPositionGroup.getCheckedRadioButtonId();
        int selectedSizeId = mSizeGroup.getCheckedRadioButtonId();

        //Remove previous banner before create new
        if (mBanner != null) {
            mBanner.destroy();
        }

        mBanner = new PubnativeBanner();
        mBanner.setListener(this);
        mBanner.load(this,
                     Settings.getAppToken(),
                     Settings.getZoneId(),
                     getBannerSize(selectedSizeId),
                     getBannerPosition(selectedPositionId)
                    );
    }

    //==============================================================================================
    // Helpers
    //==============================================================================================

    private PubnativeBanner.Position getBannerPosition(int selectedPositionId) {

        PubnativeBanner.Position position;

        if (selectedPositionId == mBottomPosition.getId()) {
            position = PubnativeBanner.Position.BOTTOM;
        } else {
            position = PubnativeBanner.Position.TOP;
        }

        return position;
    }

    private PubnativeBanner.Size getBannerSize(int selectedSizeId) {

        PubnativeBanner.Size size;

        if (selectedSizeId == mSmallBanner.getId()) {
            size = PubnativeBanner.Size.BANNER_50;
        } else {
            size = PubnativeBanner.Size.BANNER_90;
        }
        return size;
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    // PubnativeBanner.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeBannerLoadFinish(PubnativeBanner banner) {
        Log.v(TAG, "onPubnativeBannerLoadFinish");
        banner.show();
    }

    @Override
    public void onPubnativeBannerLoadFail(PubnativeBanner banner, Exception exception) {
        Log.v(TAG, "onPubnativeBannerLoadFail", exception);
        mLoaderContainer.setVisibility(View.GONE);
    }

    @Override
    public void onPubnativeBannerShow(PubnativeBanner banner) {
        Log.v(TAG, "onPubnativeBannerShow");
        mLoaderContainer.setVisibility(View.GONE);
    }

    @Override
    public void onPubnativeBannerImpressionConfirmed(PubnativeBanner banner) {
        Log.v(TAG, "onPubnativeBannerImpressionConfirmed");
    }

    @Override
    public void onPubnativeBannerClick(PubnativeBanner banner) {
        Log.v(TAG, "onPubnativeBannerClick");
    }

    @Override
    public void onPubnativeBannerHide(PubnativeBanner banner) {
        Log.v(TAG, "onPubnativeBannerHide");
    }
}
