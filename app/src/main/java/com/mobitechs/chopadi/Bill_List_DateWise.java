package com.mobitechs.chopadi;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.adapter.Bill_List_Adapter;
import com.mobitechs.chopadi.model.Bill_Items;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class Bill_List_DateWise extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    SwipeController swipeController = null;
    JSONArray listArray = null;
    private DataBaseHelper mydb;
    public List<Bill_Items> listItems = new ArrayList<Bill_Items>();

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String currentDate = "";

    FloatingActionButton btnFab;

    InterstitialAd mInterstitialAd;
    private FancyShowCaseQueue fancyShowCaseQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_list_datewise);

        mInterstitialAd = new InterstitialAd(this);
        ShowInterstitialAd();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String currMonth = String.valueOf(month + 1);
        currentDate = day + "-" + currMonth + "-" + year;

        mydb = new DataBaseHelper(this);
        btnFab = (FloatingActionButton) findViewById(R.id.btnFab);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        btnFab.setOnClickListener(this);

        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dateChangeShowCase = wmbPreference.getBoolean("dateChangeShowCase", true);
        if (dateChangeShowCase) {
            // Code to run once
            final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
                    .title("Click here to change date. \n\n\n  डेट चेंज करने के लिए यहाँ क्लिक करे.")
                    .focusOn(btnFab)
                    .build();

            fancyShowCaseQueue = new FancyShowCaseQueue()
                    .add(fancyShowCaseView1);

            fancyShowCaseQueue.show();

            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("dateChangeShowCase", false);
            editor.commit();
        }
        getList();
    }

    private void getList() {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Bill_List_Adapter(this, listItems, recyclerView);
        recyclerView.setAdapter(reviewAdapter);
        listItems.clear();


        try {
            listArray = mydb.AllCustomer_BillDetails_DateWise(currentDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        try {
            listArray = mydb.AllCustomer_BillDetails_DateWise(currentDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {

            Toast.makeText(this, "Bill Not Available", Toast.LENGTH_SHORT).show();
        } else {

            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);

                    Bill_Items expense_Items = new Bill_Items();

                    expense_Items.setcustomerId(obj.getString("customerId"));
                    expense_Items.setcustomerName(obj.getString("customerName"));
                    expense_Items.setbillDate(obj.getString("date"));
                    expense_Items.settotalPrice(obj.getString("totalPrice"));

                    listItems.add(expense_Items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            reviewAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFab) {

            DatePickerDialog mDatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    currentDate = String.valueOf(selectedday) + "-" + String.valueOf(selectedmonth + 1) + "-" + String.valueOf(selectedyear);

                    getList();

                }
            }, year, month, day);
            mDatePicker.show();
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
