package net.pubnative.interstitials.demo.activity;

import net.pubnative.interstitials.demo.R;
import net.pubnative.interstitials.demo.adapter.BriefAdapter;
import net.pubnative.interstitials.demo.adapter.FullAdapter;
import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.holder.NativeAdHolder;

import org.droidparts.util.ui.ViewUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListItemBriefActivity extends AbstractDemoActivity implements
        OnItemClickListener
{
    private ListView         listView;
    private NativeAdHolder[] holders;
    private FullAdapter      adapter;

    @Override
    public void onPreInject()
    {
        setContentView(R.layout.activity_list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        listView = ViewUtils.findViewById(this, R.id.view_list);
        adapter = createAdapter(this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        createHolders();
        show();
    }

    private void createHolders()
    {
        holders = new NativeAdHolder[adCount];
        for (int i = 0; i < adCount; i++)
        {
            holders[i] = adapter.makeAndAddHolder();
        }
    }

    @Override
    protected AdHolder<?>[] getAdHolders()
    {
        return holders;
    }

    protected FullAdapter createAdapter(Context ctx)
    {
        return new BriefAdapter(ctx);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        showInPlayStore(adapter.getItem(position).ad);
    }
}
