package com.mobitechs.chopadi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.adapter.Bill_Report_Adapter_Monthly;
import com.mobitechs.chopadi.adapter.MonthlyIncome_Adapter;
import com.mobitechs.chopadi.instance.Customer_Instance;
import com.mobitechs.chopadi.model.MonthlyIncome_Items;
import com.mobitechs.chopadi.model.MonthlyIncome_Items;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Monthly_Income extends AppCompatActivity implements View.OnClickListener{

    List<MonthlyIncome_Items> listItems = new ArrayList<MonthlyIncome_Items>();
    MonthlyIncome_Adapter adapter;
    RecyclerView recyclerView;

//    FloatingActionButton btnFab;
    LinearLayoutManager linearLayoutManager;
    private View v;

    JSONArray listArray = null;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;


    private DataBaseHelper mydb;
    Customer_Instance customer_Instance;
    Spinner spinnerMonth;

    String monthName = "";
    int monthId;
    String userYear = "2018";

    public String[] monthNameArray = new String[]{"Select Month", "January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    public int[] monthIdArray = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_income);

        mydb = new DataBaseHelper(this);
        mInterstitialAd = new InterstitialAd(this);
        ShowInterstitialAd();
//        btnFab = (FloatingActionButton) findViewById(R.id.btnFab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        monthName = monthNameArray[month + 1].toString();
        monthId = monthIdArray[month + 1];

//        btnFab.setOnClickListener(this);
        getList();

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

    private void getList() {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        adapter = new MonthlyIncome_Adapter(listItems);
        recyclerView.setAdapter(adapter);

        listItems.clear();

        try {
            listArray = mydb.All_Income_List();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Income Details Not Available", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);

                    MonthlyIncome_Items expense_Items = new MonthlyIncome_Items();

                    expense_Items.setdate(obj.getString("date"));
                    expense_Items.settotalPrice(obj.getString("totalAmount"));
                    expense_Items.setmonth(obj.getString("month"));
                    expense_Items.setyear(obj.getString("year"));

                    listItems.add(expense_Items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {

    }
}
