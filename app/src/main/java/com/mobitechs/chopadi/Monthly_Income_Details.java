package com.mobitechs.chopadi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.adapter.Bill_Report_Adapter_Monthly;
import com.mobitechs.chopadi.model.Bill_Items;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Monthly_Income_Details extends AppCompatActivity {


    List<Bill_Items> listItems = new ArrayList<Bill_Items>();
    Bill_Report_Adapter_Monthly adapter;
    RecyclerView recyclerView;
    private DataBaseHelper mydb;
    JSONArray listArray = null;
    LinearLayoutManager linearLayoutManager;
    String month,year;
    TextView txtGrandTotal;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_income_details);

        mInterstitialAd = new InterstitialAd(this);
//        ShowInterstitialAd();

        Intent intent = getIntent();
        if (null != intent) {
            month = intent.getStringExtra("month");
            year = intent.getStringExtra("year");
        }

        mydb = new DataBaseHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtGrandTotal = (TextView) findViewById(R.id.txtGrandTotal);

        getList();
    }

    private void getList() {
        listItems.clear();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.smoothScrollToPosition(0);
        adapter = new Bill_Report_Adapter_Monthly(listItems,txtGrandTotal);
        recyclerView.setAdapter(adapter);


        try {
            listArray = mydb.Income_Details(month,year);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Details Not Available", Toast.LENGTH_SHORT).show();
        } else {

            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);

                    Bill_Items expense_Items = new Bill_Items();

                    expense_Items.setbillId(obj.getString("billId"));
                    expense_Items.setentryDate(obj.getString("entryDate"));
                    expense_Items.setbillDate(obj.getString("date"));
                    expense_Items.setproductName(obj.getString("productName"));
                    expense_Items.setproductPrice(obj.getString("productPrice"));
                    expense_Items.setproductQty(obj.getString("qty"));
                    expense_Items.settotalPrice(obj.getString("totalPrice"));

                    listItems.add(expense_Items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ShowInterstitialAd();
    }
}
