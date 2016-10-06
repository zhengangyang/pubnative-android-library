![ScreenShot](PNLogo.png)

PubNative is an API-based publisher platform dedicated to native advertising which does not require the integration of an Library.

Through PubNative, publishers can request over 20 parameters to enrich their ads and thereby create any number of combinations for unique and truly native ad units.

#pubnative-android-library

pubnative-android-library is a collection of Open Source tools to implement API based native ads in Android.

##Contents

* [Requirements](#requirements)
* [Install](#install)
* [Usage](#usage)
* [Misc](#misc)
  * [License](#misc_license)
  * [Contributing](#misc_contributing)

<a name="requirements"></a>
# Requirements

* Android API 10
* An App Token provided in PubNative Dashboard.

Add the following permissions to your application `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

Optionally but not necessary to improve user targeting:

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

<a name="install"></a>
# Install

### Gradle

Add the following line to your module dependencies

```java
compile 'net.pubnative:library:2.0.1'
```

### Manual

Clone the repository and import the `:library` module into your project

<a name="usage"></a>
# Usage

PubNative library is a lean yet complete library that allows you to request and show ads.

Basic integration steps are:

1. [Request](#usage_native_request): Using `PubnativeRequest`
3. [Track](#usage_native_track): Using `PubnativeAdModel` builtin `startTracking` and `stopTracking`

<a name="usage_native_request"></a>
### 1) Request

You will need to create a `PubnativeRequest`, add all the required parameters to it and start it with a listener for the results specifying which endpoint you want to request to. Right now only `NATIVE` is available.

For simplier usage we're providing an interface `PubnativeRequest.Parameters` that contains all valid parameters for a request.

```java
PubnativeRequest request = new PubnativeRequest();
request.setParameter(PubnativeRequest.Parameters.APP_TOKEN, "----YOUR_APP_TOKEN_HERE---");
request.start(CONTEXT, PubnativeRequest.Endpoint.NATIVE, new PubnativeRequest.Listener()
{
    @Override
    public void onPubnativeRequestSuccess(PubnativeRequest request, List<PubnativeAdModel> ads)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPubnativeRequestFailed(PubnativeRequest request, Exception ex)
    {
        // TODO Auto-generated method stub
    }
});
```

<a name="usage_native_track"></a>
### 2) Track

For confirming impressions, and track clicks, call `ad.startTracking` and provide a valid listener if you want to be in track of what's going on with the ad tracking process

```java
ad.startTracking(visibleView, new PubnativeAdModel.Listener() {

    @Override
    public void onPubnativeAdModelImpression(PubnativeAdModel pubnativeAdModel, View view) {
        // Called whenever the impression is confirmed
    }
    
    @Override
    public void onPubnativeAdModelClick(PubnativeAdModel pubnativeAdModel, View view) {
        // Called when the ad was clicked
    }
    
    @Override
    public void onPubnativeAdModelOpenOffer(PubnativeAdModel pubnativeAdModel) {
        // Called when the leaving the application because the offer is being opened
    }
});
```

If at some point you want to stop the view and click tracking, just call the ad `stopTracking()` method.

<a name="misc"></a>
# Misc

<a name="misc_license"></a>
### License

This code is distributed under the terms and conditions of the MIT license.

<a name="misc_contributing"></a>
### Contributing

**NB!** If you fix a bug you discovered or have development ideas, feel free to make a pull request.
