/**
 * Copyright 2014 PubNative GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pubnative.interstitials.demo.activity;

import net.pubnative.interstitials.demo.Contract;
import net.pubnative.interstitials.demo.R;
import net.pubnative.library.PubNative;
import net.pubnative.library.model.APIEndpoint;
import net.pubnative.library.model.holder.NativeAdHolder;
import net.pubnative.library.model.request.AdRequest;

import org.droidparts.annotation.inject.InjectView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class IconActivity extends AbstractDemoActivity implements
        OnClickListener
{
    @InjectView(id = R.id.view_icon, click = true)
    private ImageView      iconView;
    private NativeAdHolder iconHolder;

    @Override
    public void onPreInject()
    {
        setContentView(R.layout.activity_icon);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeHolder();
        showIcon();
    }

    private void makeHolder()
    {
        iconHolder = new NativeAdHolder(findViewById(R.id.view_root));
        iconHolder.iconViewId = R.id.view_icon;
    }

    private void showIcon()
    {
        AdRequest req = new AdRequest(Contract.APP_TOKEN, APIEndpoint.NATIVE);
        req.fillInDefaults(this);
        req.setIconSize(300, 300);
        PubNative.showAd(req, iconHolder);
    }

    @Override
    public void onClick(View view)
    {
        if (view == iconView)
        {
            PubNative.showInPlayStoreViaDialog(this, iconHolder.ad);
        }
    }

    @Override
    protected NativeAdHolder[] getAdHolders()
    {
        return null;
    }
}
