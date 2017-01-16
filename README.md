![ScreenShot](PNLogo.png)

![Circle CI](https://circleci.com/gh/pubnative/pubnative-android-library-private.svg?style=shield) ![License](https://img.shields.io/badge/license-MIT-lightgrey.svg)

<!-- [![Coverage Status](https://coveralls.io/repos/github/pubnative/pubnative-android-library/badge.svg?branch=code_coverage_configuration)](https://coveralls.io/github/pubnative/pubnative-android-library?branch=code_coverage_configuration) -->

PubNative is an API-based publisher platform dedicated to native advertising which does not require the integration of an Library.

Through PubNative, publishers can request over 20 parameters to enrich their ads and thereby create any number of combinations for unique and truly native ad units.

#pubnative-android-library

pubnative-android-library is a collection of Open Source tools to implement API based native ads in Android.

##Contents

* [Requirements](#requirements)
* [Install](#install)
    * [Gradle](#install_gradle)
    * [Manual](#install_manual)
* [Usage](#usage)
    * [Native](#usage_native)
        * [Request](#usage_native_request)
        * [Track](#usage_native_track)
    * [Predefined](#usage_predefined)
        * [Interstitial](#usage_predefined_interstitial)
        * [Banner](#usage_predefined_banner)
        * [Video](#usage_predefined_video)
        * [In-Feed Banner](#usage_predefined_feed_banner)
        * [In-Feed Video](#usage_predefined_feed_video)
* [Misc](#misc)
    * [Proguard](#misc_proguard)
    * [Dependencies](#misc_dependencies)
    * [License](#misc_license)
    * [Contributing](#misc_contributing)

<a name="requirements"></a>
# Requirements

* Android API 10
* An App Token provided in PubNative Dashboard.
* A Zone ID provided in PubNative Dashboard.

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

<a name="install_gradle"></a>
### Gradle

Add the following line to your module dependencies

```
compile 'net.pubnative:library:2.3.9'
```

<a name="install_manual"></a>
### Manual

Clone the repository and import the `:library` module into your project

<a name="usage"></a>
# Usage

PubNative library is a lean yet complete library that allows you to request and show ads.

<a name="usage_native"></a>
## Native

Basic integration steps are:

1. [Request](#usage_native_request): Using `PubnativeRequest`
2. [Track](#usage_native_track): Using `PubnativeAdModel` builtin `startTracking` and `stopTracking`

<a name="usage_native_request"></a>
### 1) Request

You will need to create a `PubnativeRequest`, add all the required parameters to it and start it with a listener for the results specifying which endpoint you want to request to. Right now only `NATIVE` is available.

For simplier usage we're providing an interface `PubnativeRequest.Parameters` that contains all valid parameters for a request.

```java
PubnativeRequest request = new PubnativeRequest();
request.setParameter(PubnativeRequest.Parameters.APP_TOKEN, "----YOUR_APP_TOKEN_HERE---");
request.setParameter(PubnativeRequest.Parameters.ZONE_ID, "----YOUR_ZONE_ID_HERE---");
request.start(CONTEXT, new PubnativeRequest.Listener() {
    @Override
    public void onPubnativeRequestSuccess(PubnativeRequest request, List<PubnativeAdModel> ads) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPubnativeRequestFailed(PubnativeRequest request, Exception ex) {
        // TODO Auto-generated method stub
    }
});
```

##### Asset filtering

In order to avoid some traffic, you can also specify which assets do you want to receive within the request response assets, for that reason we've created a class `PubnativeAssets` that contains all the possible assets.

Once created a request object, you can specify the assets by using the `setParameterArray` method from the request.

```java
request.setParameterArray(PubnativeRequest.Parameters.ASSET_FIELDS, new String[]{ PubnativeAsset.TITLE, PubnativeAsset.ICON, <ASSETS> });
```

##### Testing Mode

If you're testing your app, we've enabled a test mode so you can test your app without affecting your reports, simply call the `setTestMode` method before doing the request and set the value to true.

```java
request.setTestMode(<boolean>);
```

##### Timeout

If you want, you can set a timeout to avoid long waits in case of slow connections, simply use the request `setTimeout` method and specify the milliseconds a request can run before timing out. The default timeout is 4000ms

```java
request.setTimeout(<timeoutinmillis>);
```

##### COPPA

If you want to enable COPPA mode to be COPPA compliant, you can set it using the following method.

```java
request.setCoppaMode(<boolean>);
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

If your ad is removed from the screen or you're exiting the activity, you'll need to stop the tracking process with  the following method:

```java
ad.stopTracking();
```

<a name="usage_predefined"></a>
## Predefined

In short, to integrate one of the standard units from PubNative, there are 3 main steps.

1. Add the module, by adding the standard unit dependency to your project dependencies
2. Load the ad, commonly, using a method `load`, if you want you can track the load process with callbacks using `setListener(<YOUR_LISTENER>)` method
3. Show the ad, using a method `show`, again, if you use the `setListener(<YOUR_LISTENER>)` method before, you will be able to track user interactions and ad behaviour.

There are 2 more methods to mention here:

* `isReady()` will tell you if the ad is prepared to be shown.
* `destroy()` will destroy any cached data and will remove the ad from the screen.

<a name="usage_predefined_interstitial"></a>
### Interstitial

Add the following line to your module dependencies
```
compile 'net.pubnative:library.interstitial:2.3.9'
```
Sample usage
```
PubnativeInterstitial interstitial = new PubnativeInterstitial();
interstitial.setListener(this);
interstitial.load(<CONTEXT>, <YOUR_APP_TOKEN_HERE>, <YOUR_ZONE_ID_HERE>);

// Once the ad is loaded ......
interstitial.show();
```

<a name="usage_predefined_banner"></a>
### Banner

Add the following line to your module dependencies
```
compile 'net.pubnative:library.banner:2.3.9'
```
Sample usage
```
PubnativeBanner banner = new PubnativeBanner();
banner.setListener(this);
banner.load(<CONTEXT>, <YOUR_APP_TOKEN_HERE>, <YOUR_ZONE_ID_HERE>, <BANNER_SIZE>, <BANNER_POSITION>);

// Once the ad is loaded ......
banner.show();
```
Banner sizes available are:
* `PubnativeBanner.Size.BANNER_50`: The resulting banner will have 50 pixels height
* `PubnativeBanner.Size.BANNER_90`: The resulting banner will have 90 pixels height, this is specially recommended for tablets devices

Banner positions available are
* `PubnativeBanner.Position.TOP`: This will render the banner on the top side of the screen
* `PubnativeBanner.Position.BOTTOM`: This will render the banner on the bottom side of the screen

<a name="usage_predefined_feed_banner"></a>
### In-Feed Banner

Add the following line to your module dependencies
```
compile 'net.pubnative:library.feed.banner:2.3.8'
```
Sample usage
```
PubnativeFeedBanner feedBanner = new PubnativeFeedBanner();
feedBanner.setListener(this);
feedBanner.load(<CONTEXT>, <YOUR_APP_TOKEN_HERE>, <YOUR_ZONE_ID_HERE>);

// Once the ad is loaded ......
feedBanner.getView();
```
As you can see, you will get a fully created view and you can place it in any parent view.

<a name="usage_predefined_video"></a>
### Video

Add the following line to your module dependencies
```
compile 'net.pubnative:library.video:2.3.9'
```
Sample usage
```
PubnativeVideo video = new PubnativeVideo();
video.setListener(this);
video.load(<CONTEXT>, <YOUR_APP_TOKEN_HERE>, <YOUR_ZONE_ID_HERE>);

// Once the ad is loaded ......
video.show();
```
Video will be shown fullscreen.

<a name="usage_predefined_feed_video"></a>
### In-Feed Video

Add the following line to your module dependencies
```
compile 'net.pubnative:library.feed.video:2.3.9'
```
Sample usage
```
PubnativeFeedVideo feedVideo = new PubnativeFeedVideo();
feedVideo.setListener(this);
feedVideo.load(<CONTEXT>, <YOUR_APP_TOKEN_HERE>, <YOUR_ZONE_ID_HERE>);

// Once the ad is loaded ......
feedVideo.getView();
```
As you can see, you will get a fully created view and you can place it in any parent view.

<a name="misc"></a>
# Misc

<a name="misc_custom_loading"></a>
### Custom loading

The `PubnativeAdModel` class comes with a default loading view that overlaps the entire current actvity view in order to have the easiest integration, however, you might want to create your own loading view while the click redirection is calculated.

To do this, simply disable the click loader an develop our own using the following line **before the onPubnativeAdModelClick method ends**

```java
ad.setUseClickLoader(false);
```

There are also some people that don't like the idea of having a background click redirection, for this case, you can deactivate using the following method

```java
ad.setUseBackgroundClick(false)
```

<a name="misc_proguard"></a>
### Proguard

If you are using Proguard, add these lines to your Proguard file
```
-keepattributes Signature
-keep class net.pubnative.** { *; }
```

<a name="misc_dependencies"></a>
### Dependencies

This repository holds the following dependencies

* [GSON](https://github.com/google/gson): for json parsing
* [url_driller](https://github.com/pubnative/url-driller): to follow clicks redirections on background
* [advertising_id_client](https://github.com/pubnative/advertising-id-client): to retrieve [android advertising id](http://developer.android.com/intl/es/google/play-services/id.html) without using google ads library

<a name="misc_license"></a>
### License

This code is distributed under the terms and conditions of the MIT license.

<a name="misc_contributing"></a>
### Contributing

**NB!** If you fix a bug you discovered or have development ideas, feel free to make a pull request.
