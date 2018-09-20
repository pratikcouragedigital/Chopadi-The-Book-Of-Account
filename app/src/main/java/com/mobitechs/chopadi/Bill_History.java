package com.mobitechs.chopadi;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.adapter.Bill_History_Adapter;
import com.mobitechs.chopadi.adapter.CustomerList_Adapter;
import com.mobitechs.chopadi.model.Bill_History_Items;
import com.mobitechs.chopadi.model.Customer_Items;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Bill_History extends AppCompatActivity {

    RecyclerView recyclerView;
    Bill_History_Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    JSONArray listArray = null;
    private DataBaseHelper mydb;

    String customerId,customerName;
    public List<Bill_History_Items> listItems = new ArrayList<Bill_History_Items>();

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_history);

        mInterstitialAd = new InterstitialAd(this);
        ShowInterstitialAd();

        Intent intent = getIntent();
        if (null != intent) {
            customerId = intent.getStringExtra("customerId");
            customerName = intent.getStringExtra("customerName");
        }

        mydb = new DataBaseHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Bill_History_Adapter(this, listItems, recyclerView);
        recyclerView.setAdapter(reviewAdapter);

        getList();
    }

    private void getList() {
        try {
            listArray = mydb.Bill_History(customerId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();
        if (dataCount == 0) {

            Toast.makeText(this, "Bill History Not Available", Toast.LENGTH_SHORT).show();
        } else {

            listItems.clear();

            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);
                    Bill_History_Items billItems = new Bill_History_Items();

                   billItems.setfromDate(obj.getString("fromDate"));
                   billItems.settoDate(obj.getString("toDate"));
                   billItems.setbillAmount(obj.getString("amount"));
                   billItems.setpaidAmount(obj.getString("paid"));
                   billItems.setbalanceAmount(obj.getString("balance"));
                   billItems.setentryDate(obj.getString("entryDate"));
                   billItems.setbillId(obj.getString("UserBillID"));

                    listItems.add(billItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            reviewAdapter.notifyDataSetChanged();
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
