package com.mobitechs.chopadi;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

public class Product_Add extends AppCompatActivity implements View.OnClickListener{

    EditText txtProductName,txtPrice;
    String productName,price,productId,imFrom;
    Button btnSubmit;
    ProgressBar progressBar;

    private DataBaseHelper mydb;
    private AdView adView, adView1,adView2,adView3;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);

        ShowBannerAds();
        mInterstitialAd = new InterstitialAd(this);
        mydb = new DataBaseHelper(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtProductName = (EditText) findViewById(R.id.txtProductName);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressBar.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(this);


        Intent intent = getIntent();
        if (null != intent) {
            productName = intent.getStringExtra("name");
            price = intent.getStringExtra("price");
            productId = intent.getStringExtra("id");
            imFrom = intent.getStringExtra("imFrom");
        }
        if(productName == null){
            productName = "";
        }
        if(!productName.equals("")){
            txtProductName.setText(productName);
        }

        if(imFrom == null){
            imFrom = "";
        }

        if(price == null){
            price = "";
        }
        if(!price.equals("")){
            txtPrice.setText(price);
        }
    }

    private void ShowBannerAds() {
        String deviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        adView = (AdView) findViewById(R.id.adView);
        adView1 = (AdView) findViewById(R.id.adView1);
        adView2 = (AdView) findViewById(R.id.adView2);
        adView3 = (AdView) findViewById(R.id.adView3);

        AdRequest adRequest = new AdRequest.Builder()
                //   .addTestDevice(deviceId)
                .build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);
        adView2.loadAd(adRequest);
        adView3.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        adView1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        adView2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        adView3.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit){
            productName = txtProductName.getText().toString();
            price = txtPrice.getText().toString();


            if(productName.equals("")){
                Toast.makeText(this, "Please Enter Product Name.", Toast.LENGTH_SHORT).show();
            }
            else if(price.equals("")){
                Toast.makeText(this, "Please Enter Price.", Toast.LENGTH_SHORT).show();
            }
            else{
                progressBar.setVisibility(View.VISIBLE);

                if(imFrom.equals("Edit")){
                    boolean isInserted;
                    isInserted = mydb.Product_Edit(productId,productName,price);
                    if(isInserted == true){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Product Details Edited", Toast.LENGTH_SHORT).show();
                        Intent gotoList = new Intent(this, Product_List.class);
                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotoList);
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Product Already Exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    boolean isInserted;
                    isInserted = mydb.Product_Add(productName,price);
                    if(isInserted == true){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Product Details Added", Toast.LENGTH_SHORT).show();
                        Intent gotoList = new Intent(this, Product_List.class);
                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotoList);
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Product Already Exist", Toast.LENGTH_SHORT).show();
                    }
                }
                ShowInterstitialAd();
                txtProductName.setText("");
                txtPrice.setText("");

            }
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void ShowInterstitialAd() {
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdClosed() {
//                        Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                        Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
//                        Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
//                        Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}