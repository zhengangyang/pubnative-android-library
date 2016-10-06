package net.pubnative.library.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class InFeedAdapter extends ArrayAdapter<String> {

    protected static final int CONTENT_ROWS = 15;
    protected static final int AD_ROW       = 5;

    protected static final int ROW_TYPE_CONTENT = 0;
    protected static final int ROW_TYPE_AD = 1;

    protected Callback mCallback;

    public interface Callback {

        boolean isAdReady();

        void showAd(ViewGroup container);
    }

    public InFeedAdapter(Context context, Callback callback) {
        super(context, 0, new ArrayList<String>());
        mCallback = callback;
    }

    // Overriden
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return CONTENT_ROWS + 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        int result = ROW_TYPE_CONTENT;
        if ((position + 1) == AD_ROW && mCallback.isAdReady()) {
            result = ROW_TYPE_AD;
        }
        return result;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (ROW_TYPE_AD == getItemViewType(position)) {

            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed_cell_container, parent, false);
                convertView.setTag(1);
            }
            mCallback.showAd((ViewGroup) convertView);
        } else {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed_cell_text, parent, false);
                convertView.setTag(0);
            } else if ((int)convertView.getTag() != 0) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed_cell_text, parent, false);
                convertView.setTag(0);
            }
        }

        return convertView;
    }

}
