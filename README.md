![ScreenShot](PNLogo.png)

PubNative is an API-based publisher platform dedicated to native advertising which does not require the integration of an Library.
Through PubNative, publishers can request over 20 parameters to enrich their ads and thereby create any number of combinations for unique and truly native ad units.

PubNative Android Library simplifies getting ad images, texts and sending confirmation.\n
PubNative Interstitials provides ready formats & widgets .

##Contents

* [Requirements](#requirements)
* [Install](#install)
* [Usage](#usage)
  * [Setup](#usage_setup)
  * [Custom ads](#usage_custom)
  * [Predefined ads](#usage_predefined)
  * [ProGuard](#usage_proguard)
* [Misc](#misc)
  * [Dependencies](#misc_dependencies)
  * [License](#misc_license)
  * [Contributing](#misc_contributing)

<a name="requirements"></a>
# Requirements

* Android 4.0+.
* An App Token provided in PubNative Dashboard.
* Google Play Services (https://developer.android.com/google/play-services/index.html)
* The following permissions in AndroidManifest.xml:

```xml
<!-- REQUIRED -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<!-- OPTIONAL -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

<a name="install"></a>
# Install
Clone the repository and import the needed projects

* **pubnative-library**: This library handles the basic behaviour to request native ads and build a custom ad view.
* **pubnative-interstitials**: This library contains to use the pre-built formats.

* Edit your `AndroidManifest.xml` file to add the following activity and meta-data **inside your application tag**

``` xml
<meta-data android:name="com.google.android.gms.version"
           android:value="@integer/google_play_services_version" />

<activity android:name="net.pubnative.interstitials.PubNativeInterstitialsActivity"
          android:configChanges="keyboardHidden|orientation|screenSize"
          android:hardwareAccelerated="true"
          android:taskAffinity="net.pubnative.interstitials"
          android:theme="@style/Theme.PubNativeInterstitials" />
```

<a name="usage"></a>
# Usage

<a name="usage_setup"></a>
### Setup

Add the following inside an Activity that will show ads:
```java
@Override
protected void onPause()
{
	super.onPause();
	PubNative.onPause();
}

@Override
protected void onResume()
{
	super.onResume();
	PubNative.onResume();
}

@Override
protected void onDestroy()
{
	super.onDestroy();
	PubNative.onDestroy();
}
```

<a name="usage_custom"></a>
### Custom ads

In short these are the steps that you need to do:

* Create and configure a `NativeAdHolder`, this will be used to populate your view once the request is over.
* **(OPTIONAL)** Set up a listener for the Ad Loading workflow
* Create and configure your API request with `AdRequest`.
  * Create the request with your AppToken and selecting one kind of Endpoint (NATIVE/VIDEO) that will return a native ad or a native ad containing a VAST video inside.
  * Invoke `fillInDefaults(Context context)` method to set up basic API request data.
  * Fill params for a more specific request using `setParam(String key, String value)`. All valid parameters are described in the [Pubnative Client API Wiki](https://pubnative.atlassian.net/wiki/display/PUB/Client+API#ClientAPI-3.Request)

```java
NativeAdHolder holder = null;
public void requestAd()
{
  // Create a NativeAdHolder and set up all the item view ID's that you need
  // Keep a reference to the holder, since this will contain all the downloaded info
  holder = new NativeAdHolder(<YOUR_AD_CONTAINER_VIEW>);
  holder.titleViewId = R.id.<YOUR_TITLE_VIEW_ID>;
  holder.descriptionViewId = R.id.<YOUR_DESCRIPTION_VIEW_ID>;
  holder.iconViewId = R.id.<YOUR_ICON_VIEW_ID>;
  holder.bannerViewId = R.id.<YOUR_BANNER_VIEW_ID>;
  holder.downloadViewId = R.id.<YOUR_CTA_VIEW_ID>;


  AdRequest request = new AdRequest("<YOUR_APP_TOKEN>", APIEndpoint.<SELECTED_ENDPOINT>);
  request.fillInDefaults(this); // MANDATORY, without this, the request won't work

  // Set up your parameters if needed
  // request.setParam("<PARAMETER_KEY>", "<PARAMETER_VALUE>");

  //OPTIONAL (Set up a listener)
  PubNative.setListener(new PubNativeListener()
  {
    @Override
    public void onLoaded()
    {
        // Ad is loaded, recommended to show the container here
    }

    @Override
    public void onError(Exception ex)
    {
        // Something happened while loading the ad
    }
  });

  PubNative.showAd(request, holder);
}
```

Since the library automatically confirms the impression of ads, you will only need to open the ad when you want.

```java
PubNative.showInPlayStoreViaDialog(<YOUR_CONTEXT>, <YOUR_AD_HOLDER>.ad);
```

For more examples, check the code under **pubnative-library-tester**

<a name="usage_predefined"></a>
### Predefined ads

PubNative Library also provides with a simple method to show predefined ads just in case you don't want to mess up customizing your own ad look and feel. The class used to display interstitials is `PubNativeInterstitials`.

* **Initialize** the library caling `init(Context ctx, String appToken)` method prior to any interstitial request.
* **(OPTIONAL)** Set up a listener to follow up the ad workflow
* **Request** interstitials with `show(Activity activity, PubNativeInterstitialsType type, int adCount)` interstitial types are:
  * `PubNativeInterstitialsType.INTERSTITIAL`: Shows a full screen ad
  * `PubNativeInterstitialsType.VIDEO_INTERSTITIAL`: Shows a full screen video ad

```java
private boolean pubnativeInitialized = false;
public void requestPredefinedAd()
{
  if(!this.pubnativeInitialized)
  {
    this.pubnativeInitialized = true;
    PubNativeInterstitials.init(<YOUR_CONTEXT>, "<YOUR_APP_TOKEN>");
    PubNativeInterstitials.addListener(new PubNativeInterstitialsListener(){
      @Override
      public void onShown(PubNativeInterstitialsType type)
      {
        // The ad has appeared in the screen
      }

      @Override
      public void onTapped(NativeAd ad)
      {
        // The ad has been tapped by the user, good moment to gift the user :D
      }

      @Override
      public void onClosed(PubNativeInterstitialsType type)
      {
        // The ad has disappeared from the screen
      }

      @Override
      public void onError(Exception ex)
      {
        // Something wrong happened
      }
    });
  }

  PubNativeInterstitials.show(<YOUR_ACTIVITY>, PubNativeInterstitialsType.<YOUR_SELECTED_TYPE>, <YOUR_REQUEST_AD_COUNT>);
}
```

Please check out pubnative-interstitials-tester for code examples.

<a name="usage_proguard"></a>
### ProGuard

If using ProGuard, please include pubnative-proguard.cfg.

<a name="misc"></a>
# Misc

<a name="misc_dependencies"></a>
### Dependencies

* [Droidparts](https://github.com/yanchenko/droidparts) for making our life easier

<a name="misc_license"></a>
### License

This code is distributed under the terms and conditions of the MIT license.

<a name="misc_contributing"></a>
### Contributing

**NB!** If you fix a bug you discovered or have development ideas, feel free to make a pull request.
