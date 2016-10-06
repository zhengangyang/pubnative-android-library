package net.pubnative.interstitials.demo.misc;

import org.droidparts.net.image.AbstractImageReshaper;
import org.droidparts.util.ui.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;

public class ResizingRoundingReshaper extends AbstractImageReshaper
{
    private final int maxSide;
    private final int radiusPx;

    public ResizingRoundingReshaper(Context ctx, int sidePx, int radiusPx)
    {
        this.radiusPx = radiusPx;
        this.maxSide = sidePx;
    }

    @Override
    public String getCacheId()
    {
        return "resize-" + maxSide + "-round-" + radiusPx;
    }

    @Override
    public Pair<Bitmap.CompressFormat, Integer> getCacheFormat(String contentType)
    {
        return AbstractImageReshaper.PNG;
    }

    @Override
    public int getImageWidthHint()
    {
        return maxSide;
    }

    @Override
    public int getImageHeightHint()
    {
        return maxSide;
    }

    @Override
    public Bitmap reshape(Bitmap bm)
    {
        Bitmap tmp = BitmapUtils.getScaled(bm, maxSide, false);
        return BitmapUtils.getRounded(tmp, radiusPx);
    }
}
