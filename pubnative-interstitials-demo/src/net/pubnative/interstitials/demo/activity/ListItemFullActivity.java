package net.pubnative.interstitials.demo.activity;

import net.pubnative.interstitials.demo.adapter.FullAdapter;
import android.content.Context;

public class ListItemFullActivity extends ListItemBriefActivity
{
    @Override
    protected FullAdapter createAdapter(Context ctx)
    {
        return new FullAdapter(ctx);
    }
}
