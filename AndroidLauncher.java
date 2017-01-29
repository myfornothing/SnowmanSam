package com.fornothing.snowmansam;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdSize;
import com.amazon.device.ads.AdTargetingOptions;
import com.amazon.device.ads.DefaultAdListener;
import com.amazon.device.ads.InterstitialAd;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.fornothing.snowmansam.utilities.AdsController;

public class AndroidLauncher extends AndroidApplication implements AdsController {

    private AdLayout bannerAd;
    private static final String BANNER_APP_KEY = "";
    private static final String BANNER_LOG_TAG = "BannerAmazonAd";

    public InterstitialAd interstitialAd;
    private static final String INTERSTITIAL_AD_KEY = "";
    private static final String INTERSTITIAL_LOG_TAG = "InterstitialAmazonAd";

    @Override
	protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View gameView = initializeForView(new MainClass(this), config);
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView);

        //TODO TURN THESE OFF WHEN LIVE
        AdRegistration.enableLogging(true);
        AdRegistration.enableTesting(true);

        bannerAd = new AdLayout(this, AdSize.SIZE_320x50);
        bannerAd.setListener(new AmazonAdListener());

        try {
            AdRegistration.setAppKey(BANNER_APP_KEY);
        } catch (final IllegalArgumentException e) {
            Log.e(BANNER_LOG_TAG, "IllegalArgumentException thrown: " + e.toString());
            return;
        }

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout.addView(bannerAd, adParams);
        setContentView(layout);
        loadBannerAd();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setListener(new AmazonAdListener());

        try {
            AdRegistration.setAppKey(INTERSTITIAL_AD_KEY);
        } catch (final IllegalArgumentException e) {
            Log.e(INTERSTITIAL_LOG_TAG, "IllegalArgumentException thrown: " + e.toString());
            return;
        }
        loadInstAd();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.bannerAd.destroy();
    }

    public void loadBannerAd() {
            final AdTargetingOptions adOptions = new AdTargetingOptions();
            adOptions.enableGeoLocation(true);
            this.bannerAd.loadAd(adOptions);
    }

    public void loadInstAd() {
        final AdTargetingOptions adOptions = new AdTargetingOptions();
        adOptions.enableGeoLocation(true);
        this.interstitialAd.loadAd(adOptions);
    }

    public void showBannerAd() {
        if (!this.bannerAd.showAd()) {
            Log.w(BANNER_LOG_TAG, "The BannerAd was not shown... showBannerAd() ");
        }
    }

    public void showInstAd() {
        if (!this.interstitialAd.showAd()) {
            Log.w(INTERSTITIAL_LOG_TAG, "The InterstitialAd was not shown... showInstAd() ");
        }
        this.interstitialAd.showAd();
    }

    // Added for AdsController Interface
    @Override
    public void showBannerAds() { showBannerAd(); }
    @Override
    public void hideBannerAds() { loadBannerAd(); }
    @Override
    public final void showInterstitialAd() { showInstAd(); }
    @Override
    public void hideInterstitialAd() { loadInstAd(); }

    //This class extends DefaultAdListener, so you can override only the methods that you need.
    class AmazonAdListener extends DefaultAdListener {

        @Override
        public void onAdLoaded(final Ad ad, final AdProperties adProperties) {
            Log.i(BANNER_LOG_TAG, adProperties.getAdType().toString()
                    + " BannerAd loaded successfully.");
            Log.i(INTERSTITIAL_LOG_TAG, adProperties.getAdType().toString()
                    + " InterstitialAd loaded successfully.");
        }
        @Override
        public void onAdFailedToLoad(final Ad ad, final AdError error) {
            Log.w(BANNER_LOG_TAG, "BannerAd failed to load. Code: " + error.getCode()
                    + ", Message: " + error.getMessage());
            Log.w(INTERSTITIAL_LOG_TAG, "InterstitialAd failed to load. Code: " + error.getCode()
                    + ", Message: " + error.getMessage());
        }
        @Override
        public void onAdExpanded(final Ad ad) {
            Log.i(BANNER_LOG_TAG, "Ad expanded.");
            Log.i(INTERSTITIAL_LOG_TAG, "Ad expanded.");
            showInstAd();
            // You may want to pause your activity here.
        }
        @Override
        public void onAdCollapsed(final Ad ad) {
            Log.i(BANNER_LOG_TAG, "Ad collapsed.");
            Log.i(INTERSTITIAL_LOG_TAG, "Ad collapsed.");
            loadInstAd();
            // Resume your activity here, if it was paused in onAdExpanded.
        }
    }
}