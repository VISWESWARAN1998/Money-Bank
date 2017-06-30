package com.shiva.moneybank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class License extends AppCompatActivity {

    private WebView cpol;
    private AdView mAdView;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        cpol = (WebView)findViewById(R.id.license);
        cpol.loadUrl("file:///android_asset/CPOL.htm");
        mAdView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
