package net.pubnative.library.util;

import net.pubnative.library.R;
import net.pubnative.library.inner.WorkerItem;

import org.droidparts.util.ui.ViewUtils;

import android.view.View;
import android.widget.ImageView;

public class MiscUtils
{
    public static void setMuted(WorkerItem<?> wi, ImageView muteView, boolean muted)
    {
        wi.setMuted(muted);
        int resId = muted ? R.drawable.pn_ic_unmute : R.drawable.pn_ic_mute;
        muteView.setImageResource(resId);
    }

    public static void setInvisible(boolean invisible, View... views)
    {
        for (View v : views)
        {
            if (v != null)
            {
                ViewUtils.setInvisible(invisible, v);
            }
        }
    }
}
