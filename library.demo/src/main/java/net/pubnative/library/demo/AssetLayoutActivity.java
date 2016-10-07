package net.pubnative.library.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.pubnative.library.demo.utils.Settings;
import net.pubnative.library.layouts.PubnativeLayouts;

public class AssetLayoutActivity extends Activity implements PubnativeLayouts.Listener {

    private static final String TAG     = AssetLayoutActivity.class.getName();

    private PubnativeLayouts mAssetLayouts;
    private RelativeLayout   mLoaderContainer;
    private RelativeLayout   mAdContainer;
    private RadioGroup       mSizeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_layout);
        mLoaderContainer = (RelativeLayout) findViewById(R.id.activity_native_container_loader);
        mAdContainer = (RelativeLayout) findViewById(R.id.activity_asset_ad_container);
        mSizeGroup = (RadioGroup) findViewById(R.id.rg_layout_type);
    }

    public void onRequestClick(View v) {

        Log.v(TAG, "onRequestClick");
        mLoaderContainer.setVisibility(View.VISIBLE);
        mAdContainer.setVisibility(View.GONE);
        mAdContainer.removeAllViews();

        int selectedSizeId = mSizeGroup.getCheckedRadioButtonId();

        mAssetLayouts = new PubnativeLayouts();
        mAssetLayouts.setListener(this);
        mAssetLayouts.load(this,
                Settings.getAppToken(),
                Settings.getZoneId(),
                getLayoutSize(selectedSizeId));
    }

    public void show() {

        Log.v(TAG, "show");
        mLoaderContainer.setVisibility(View.GONE);
        mAdContainer.setVisibility(View.VISIBLE);
        mAssetLayouts.show(mAdContainer);
    }

    protected PubnativeLayouts.LayoutType getLayoutSize(int selectedSizeId) {

        PubnativeLayouts.LayoutType layoutType;
        switch (selectedSizeId) {
            case R.id.rd_layout_small:
                layoutType = PubnativeLayouts.LayoutType.SMALL;
                break;
            case R.id.rd_layout_medium:
                layoutType = PubnativeLayouts.LayoutType.MEDIUM;
                break;
            case R.id.rd_layout_large:
                layoutType = PubnativeLayouts.LayoutType.LARGE;
                break;
            default:
                layoutType = PubnativeLayouts.LayoutType.SMALL;
                break;
        }

        return layoutType;
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    //----------------------------------------------------------------------------------------------
    // PubnativeLayouts.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeLayoutLoadFinish(PubnativeLayouts layout) {
        Log.v(TAG, "onPubnativeLayoutLoadFinish");
        show();
        Toast.makeText(this, "Ad loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPubnativeLayoutLoadFailed(PubnativeLayouts assetLayout, Exception exception) {
        Log.v(TAG, "onPubnativeLayoutsLoadFailed");
        mLoaderContainer.setVisibility(View.GONE);
        Toast.makeText(this, "Ad loading failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPubnativeLayoutShow(PubnativeLayouts layoutType) {
        Log.v(TAG, "onPubnativeLayoutShow");
        Toast.makeText(this, "Ad shown", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPubnativeLayoutImpressionConfirmed(PubnativeLayouts layout) {
        Log.v(TAG, "onPubnativeLayoutImpressionConfirmed");
        Toast.makeText(this, "Ad impression confirmed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPubnativeLayoutClick(PubnativeLayouts layout) {
        Log.v(TAG, "onPubnativeLayoutClick");
        Toast.makeText(this, "Ad clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPubnativeLayoutHide(PubnativeLayouts layoutType) {
        Log.v(TAG, "onPubnativeLayoutHide");
        Toast.makeText(this, "Ad hide", Toast.LENGTH_SHORT).show();
    }
}
