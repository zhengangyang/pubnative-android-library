package net.pubnative.library.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public abstract class InFeedActivity extends Activity implements InFeedAdapter.Callback {

    private static final String TAG = InFeedActivity.class.getSimpleName();

    View     mLoaderView;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mLoaderView = findViewById(R.id.activity_feed_loader);
        mListView = (ListView) findViewById(R.id.activity_feed_list);
        mListView.setAdapter(new InFeedAdapter(this, this));
    }

    public void onRequestClick(View view) {

        request();
    }

    protected void startLoading() {

        mLoaderView.setVisibility(View.VISIBLE);
    }

    protected void stopLoading() {

        mLoaderView.setVisibility(View.INVISIBLE);
    }

    public abstract void request();
    public abstract boolean isReady();
    public abstract void show(ViewGroup container);

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    // InFeedAdapter.Callback
    //----------------------------------------------------------------------------------------------

    @Override
    public boolean isAdReady() {
        return isReady();
    }

    @Override
    public void showAd(ViewGroup container) {

        show(container);
    }
}
