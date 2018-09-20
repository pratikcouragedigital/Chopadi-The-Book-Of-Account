package com.mobitechs.chopadi;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

public class Bill_Edit extends AppCompatActivity implements View.OnClickListener{

    TextView txtCustomerName,txtDate,txtProductName;
    EditText txtQty;
    Button btnUpdate;

    String billId,customerName,date,productName,productPrice,qty;
    private DataBaseHelper mydb;
    private AdView adView, adView1;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_edit);

        ShowBannerAds();

        mydb = new DataBaseHelper(this);
        mInterstitialAd = new InterstitialAd(this);

        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtQty = (EditText) findViewById(R.id.txtQty);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        Intent intent = getIntent();
        if (null != intent) {
            billId = intent.getStringExtra("billId");
            customerName = intent.getStringExtra("customerName");
            date = intent.getStringExtra("date");
            productName = intent.getStringExtra("productName");
            productPrice = intent.getStringExtra("productPrice");
            qty = intent.getStringExtra("qty");
        }
        
        txtCustomerName.setText(customerName);
        txtDate.setText(date);
        txtProductName.setText(productName);
        txtQty.setText(qty);
    }

    private void ShowBannerAds() {
        String deviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        adView = (AdView) findViewById(R.id.adView);
        adView1 = (AdView) findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder()
                //   .addTestDevice(deviceId)
                .build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);

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
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnUpdate){

            qty = txtQty.getText().toString();
            if(qty.equals("") || qty.isEmpty()){
                Toast.makeText(this, "Please Enter Qty.", Toast.LENGTH_SHORT).show();
            }else{

                int pQty = Integer.parseInt(qty);
                int pPrice = Integer.parseInt(productPrice);
                int total = pQty * pPrice;
                String pTotal = String.valueOf(total);

                boolean isInserted;
                isInserted = mydb.Bill_Edit(billId, qty,pTotal, date);

                if (isInserted == true) {
//                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Bill Details Edited.", Toast.LENGTH_SHORT).show();
                    Intent gotoList = new Intent(this, MainActivity.class);
                    gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotoList);

                } else {
                    Toast.makeText(this, "Try Again.", Toast.LENGTH_SHORT).show();
                }
                ShowInterstitialAd();
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
