package com.mobitechs.chopadi;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Customer_Add extends AppCompatActivity implements View.OnClickListener {

    EditText txtCustomerName,txtCustomerNo;
    String customerName,customerNo,customerId,imFrom;
    Button btnSubmit;
    ProgressBar progressBar;
    private DataBaseHelper mydb;

    private AdView adView, adView1,adView2,adView3;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_add);

        ShowBannerAds();
        mInterstitialAd = new InterstitialAd(this);

        mydb = new DataBaseHelper(this);
        txtCustomerName = (EditText) findViewById(R.id.txtCustomerName);
        txtCustomerNo = (EditText) findViewById(R.id.txtCustomerNo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressBar.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(this);

        Intent intent = getIntent();
        if (null != intent) {
            customerName = intent.getStringExtra("name");
            customerNo = intent.getStringExtra("no");
            customerId = intent.getStringExtra("id");
            imFrom = intent.getStringExtra("imFrom");
        }
        if(customerName == null){
            customerName = "";
        }
        if(customerNo == null){
            customerNo = "";
        }
        if(imFrom == null){
            imFrom = "";
        }
        if(!customerName.equals("")){
            txtCustomerName.setText(customerName);
        }
        if(!customerNo.equals("")){
            txtCustomerNo.setText(customerNo);
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

    public boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            customerName = txtCustomerName.getText().toString();
            customerNo = txtCustomerNo.getText().toString();

            if (customerName.equals("")) {
                Toast.makeText(this, "Please Enter Customer Name.", Toast.LENGTH_SHORT).show();
            }
//            else if (customerNo.equals("")) {
//                Toast.makeText(this, "Please Enter Customer Mobile No.", Toast.LENGTH_SHORT).show();
//            }
            else if(!isValidPhone(txtCustomerNo.getText().toString())){

                Toast.makeText(getApplicationContext(),"Phone number is not valid",Toast.LENGTH_SHORT).show();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);

                if(imFrom.equals("Edit")){
                    boolean isInserted;
                    isInserted = mydb.Customer_Edit(customerId,customerName,customerNo);
                    if (isInserted == true) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Customer Edited", Toast.LENGTH_SHORT).show();
                        Intent gotoList = new Intent(this, Customer_List.class);
                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotoList);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Customer Already Exist", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    boolean isInserted;
                    isInserted = mydb.Customer_Add(customerName,customerNo);
                    if (isInserted == true) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Customer Added", Toast.LENGTH_SHORT).show();
                        Intent gotoList = new Intent(this, Customer_List.class);
                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotoList);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Customer Already Exist", Toast.LENGTH_SHORT).show();
                    }
                }
                ShowInterstitialAd();
                txtCustomerName.setText("");

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
