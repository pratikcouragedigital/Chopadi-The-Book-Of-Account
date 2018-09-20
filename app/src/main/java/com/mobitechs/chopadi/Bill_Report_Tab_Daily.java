package com.mobitechs.chopadi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mobitechs.chopadi.adapter.Bill_Report_Adapter_Daily;
import com.mobitechs.chopadi.instance.Customer_Instance;
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

public class Bill_Report_Tab_Daily extends Fragment implements View.OnClickListener{

    Activity activity;
    List<Bill_Items> listItems = new ArrayList<Bill_Items>();
    Bill_Report_Adapter_Daily adapter;
    RecyclerView recyclerView;

    TextView txtDate;
    FloatingActionButton btnFab;
    LinearLayoutManager linearLayoutManager;
    private View v;

    JSONArray listArray = null;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String selectedDate;
    String customerId="";
    TextView txtGrandTotal;
    LinearLayout totalLayout,titleLayout;

    private DataBaseHelper mydb;
    Customer_Instance customer_Instance;
    private AdView adView, adView1;

    private FancyShowCaseQueue fancyShowCaseQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bill_report_tab_daily, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ShowBannerAds();
        mydb = new DataBaseHelper(v.getContext());


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        totalLayout = (LinearLayout) getView().findViewById(R.id.totalLayout);
        titleLayout = (LinearLayout) getView().findViewById(R.id.titleLayout);

        txtGrandTotal = (TextView) getView().findViewById(R.id.txtGrandTotal);
        txtDate = (TextView) getView().findViewById(R.id.txtDate);
        btnFab = (FloatingActionButton) getView().findViewById(R.id.btnFab);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        customer_Instance = new Customer_Instance();
        customerId = customer_Instance.getCustomerId();
        selectedDate = customer_Instance.getSelectedDate();

        if(selectedDate.equals("")){
            String currMonth = String.valueOf(month + 1);
            selectedDate = day + "-" + currMonth + "-" + year;
        }
        else if(selectedDate==null){
            String currMonth = String.valueOf(month + 1);
            selectedDate = day + "-" + currMonth + "-" + year;
        }
        Toast.makeText(v.getContext(), selectedDate+"", Toast.LENGTH_SHORT).show();
        txtDate.setText("Date: "+selectedDate);

        btnFab.setOnClickListener(this);

        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        boolean dateMonthChangeShowCase = wmbPreference.getBoolean("dateMonthChangeShowCase", true);
        if (dateMonthChangeShowCase) {
            // Code to run once
            final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder((Activity) v.getContext())
                    .title("Click here to change date or month. \n\n\n  डेट और महिना चेंज करने के लिए यहाँ क्लिक करे.")
                    .focusOn(btnFab)
                    .build();

            fancyShowCaseQueue = new FancyShowCaseQueue()
                    .add(fancyShowCaseView1);

            fancyShowCaseQueue.show();

            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("dateMonthChangeShowCase", false);
            editor.commit();
        }

        getList();

    }

    private void ShowBannerAds() {
        String deviceId = Settings.Secure.getString(v.getContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        adView = (AdView) v.findViewById(R.id.adView);
        adView1 = (AdView) v.findViewById(R.id.adView1);

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


    private void getList() {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        adapter = new Bill_Report_Adapter_Daily(listItems,txtGrandTotal);
        recyclerView.setAdapter(adapter);

        listItems.clear();

        try {
            listArray = mydb.Customer_BillDetails_DateWise(selectedDate,customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {
            titleLayout.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            Toast.makeText(v.getContext(), "Details Not Available", Toast.LENGTH_SHORT).show();
        }
        else {
            titleLayout.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);

                    Bill_Items expense_Items = new Bill_Items();

                    expense_Items.setbillId(obj.getString("billId"));
                    expense_Items.setcustomerId(obj.getString("customerId"));
                    expense_Items.setcustomerName(obj.getString("customerName"));
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


    @Override
    public void onClick(View v) {
         if (v.getId() == R.id.btnFab) {

            DatePickerDialog mDatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    selectedDate = String.valueOf(selectedday) + "-" + String.valueOf(selectedmonth + 1) + "-" + String.valueOf(selectedyear);

                    //selectedDate = DateFormatter.ChangeDateFormat(selectedDate);

                    txtDate.setText("Date: "+selectedDate);
                    // do what u want

                    getList();

                }
            }, year, month, day);
            mDatePicker.show();
        }
    }
}