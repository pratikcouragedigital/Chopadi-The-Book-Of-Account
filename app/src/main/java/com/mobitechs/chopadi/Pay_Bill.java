package com.mobitechs.chopadi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.adapter.Bill_Report_Adapter_Monthly;
import com.mobitechs.chopadi.adapter.Pay_Bill_List_Adapter;
import com.mobitechs.chopadi.adapter.Std_Div_Filter_Adapter;
import com.mobitechs.chopadi.instance.Bill_Amount_instance;
import com.mobitechs.chopadi.model.Bill_Items;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class Pay_Bill extends AppCompatActivity  implements View.OnClickListener{


    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    String customerName;
    String customerId;

    Button btnPayBill;
    EditText txtPaidAmount;
    private DataBaseHelper mydb;
    String currentDate = "";

    static String lastBillRecordId="";
    static String fromDate="";
    static String toDate="";
    String billEntryDate;

    static int grandTotal=0;
    int billAmount=0;
    int paidAmount=0;
    int balanceAmount=0;

    List<Bill_Items> listItems = new ArrayList<Bill_Items>();
    Pay_Bill_List_Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    TextView txtTotal,txtBalance,txtGrandTotal,txtFromDate,txtToDate;
    LinearLayout totalLayout,balanceLayout, grandTotalLayout,titleLayout;
    Bill_Amount_instance bill_Amount_instance;
    RelativeLayout fromDateToDateLayout,payBillLayout;

    LinearLayout lastBillLayout,billDetailsLayout;
    TextView txtLastBillFromDate,txtLastBillToDate,txtLastBillBillAmount,txtLastBillPaidAmount,txtLastBillBalanceAmount,txtLastBillPaidDate;
    int billAvailable = 0;
    FloatingActionButton btnFabBillHistory;
    InterstitialAd mInterstitialAd;

    private FancyShowCaseQueue fancyShowCaseQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_bill);
        mInterstitialAd = new InterstitialAd(this);

//        ShowInterstitialAd();
        Intent intent = getIntent();
        if (null != intent) {
            customerId = intent.getStringExtra("customerId");
            customerName = intent.getStringExtra("customerName");
        }
        bill_Amount_instance = new Bill_Amount_instance();
        mydb = new DataBaseHelper(this);

        lastBillLayout = (LinearLayout) findViewById(R.id.lastBillLayout);
        billDetailsLayout = (LinearLayout) findViewById(R.id.billDetailsLayout);

        txtLastBillFromDate = (TextView) findViewById(R.id.txtLastBillFromDate);
        txtLastBillToDate = (TextView) findViewById(R.id.txtLastBillToDate);
        txtLastBillBillAmount = (TextView) findViewById(R.id.txtLastBillBillAmount);
        txtLastBillPaidAmount = (TextView) findViewById(R.id.txtLastBillPaidAmount);
        txtLastBillBalanceAmount = (TextView) findViewById(R.id.txtLastBillBalanceAmount);
        txtLastBillPaidDate = (TextView) findViewById(R.id.txtLastBillPaidDate);

        lastBillLayout.setVisibility(View.GONE);
        billDetailsLayout.setVisibility(View.GONE);

        titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        balanceLayout = (LinearLayout) findViewById(R.id.balanceLayout);
        grandTotalLayout = (LinearLayout) findViewById(R.id.grandTotalLayout);
        fromDateToDateLayout = (RelativeLayout) findViewById(R.id.fromDateToDateLayout);
        payBillLayout = (RelativeLayout) findViewById(R.id.payBillLayout);

        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtBalance = (TextView) findViewById(R.id.txtBalance);
        txtGrandTotal = (TextView) findViewById(R.id.txtGrandTotal);
        txtFromDate = (TextView) findViewById(R.id.txtFromDate);
        txtToDate = (TextView) findViewById(R.id.txtToDate);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtPaidAmount = (EditText) findViewById(R.id.txtPaidAmount);
        btnPayBill = (Button) findViewById(R.id.btnPayBill);

        btnFabBillHistory = (FloatingActionButton) findViewById(R.id.btnFabBillHistory);
        btnFabBillHistory.setVisibility(View.GONE);
        btnFabBillHistory.setOnClickListener(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String currMonth = String.valueOf(month + 1);
        currentDate = day + "-" + currMonth + "-" + year;

        btnPayBill.setOnClickListener(this);

        GetBillDetails();
    }

    private void GetBillDetails() {
        JSONArray billListArray = null;
        try {

            billListArray = mydb.Customer_BillDetails(customerId);

            if (!billListArray.getJSONObject(0).has("toDate")) {
                //Toast.makeText(this, "Bill Not Available", Toast.LENGTH_SHORT).show();
                lastBillLayout.setVisibility(View.GONE);
                billDetailsLayout.setVisibility(View.VISIBLE);
                toDate="";
                lastBillRecordId="";
                GetFromToCurrentDateWiseBill();
                billAvailable=1;
            }
            else {
                billAvailable = 0;
                //get last paid bill details

                String fd="",td="",lbr="";
                int ba=0,pa=0,bala=0;
                for (int i = 0; i < billListArray.length(); i++) {
                    try {
                        JSONObject obj = billListArray.getJSONObject(i);
                        fd = obj.getString("fromDate");
                        td = obj.getString("toDate");
                        ba  = Integer.parseInt(obj.getString("amount"));
                        pa  = Integer.parseInt(obj.getString("paid"));
                        bala  = Integer.parseInt(obj.getString("balance"));
                        billEntryDate = obj.getString("entryDate");
                        lbr  = obj.getString("lastBillRecordId");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                fromDate = fd;
                toDate = td;
                billAmount = ba;
                paidAmount = pa;
                balanceAmount = bala;
                lastBillRecordId = lbr;
                grandTotal = bala;

                if(balanceAmount == 0){
                    payBillLayout.setVisibility(View.GONE);
                }
                else{
                    payBillLayout.setVisibility(View.VISIBLE);
                }

                btnFabBillHistory.setVisibility(View.VISIBLE);
                lastBillLayout.setVisibility(View.VISIBLE);
                billDetailsLayout.setVisibility(View.VISIBLE);
                txtLastBillFromDate.setText(fromDate);
                txtLastBillToDate.setText(toDate);
                txtLastBillBillAmount.setText(billAmount+"");
                txtLastBillPaidAmount.setText(paidAmount+"");
                txtLastBillBalanceAmount.setText(balanceAmount+"");
                txtLastBillPaidDate.setText(billEntryDate);

                GetFromToCurrentDateWiseBill();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetFromToCurrentDateWiseBill() {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.smoothScrollToPosition(0);
//        adapter = new Pay_Bill_List_Adapter(listItems, txtTotal,txtBalance,txtGrandTotal,txtFromDate,txtToDate,billAmount,paidAmount,balanceAmount,fromDate,toDate,billEntryDate);
        adapter = new Pay_Bill_List_Adapter(listItems, txtTotal,txtBalance,txtGrandTotal,txtFromDate,txtToDate,balanceAmount,payBillLayout);
        recyclerView.setAdapter(adapter);

        listItems.clear();

        JSONArray listArray = null;
        try{
            listArray = mydb.Customer_BillDetails_FromDate(customerId,toDate,lastBillRecordId,currentDate);

            if (listArray.length() == 0) {
                fromDateToDateLayout.setVisibility(View.GONE);
                titleLayout.setVisibility(View.GONE);
                totalLayout.setVisibility(View.GONE);
                balanceLayout.setVisibility(View.GONE);
                grandTotalLayout.setVisibility(View.GONE);
                Toast.makeText(this, "Details Not Available", Toast.LENGTH_SHORT).show();
            }
            else {
                fromDateToDateLayout.setVisibility(View.VISIBLE);
                titleLayout.setVisibility(View.VISIBLE);
                totalLayout.setVisibility(View.VISIBLE);
                balanceLayout.setVisibility(View.VISIBLE);
                grandTotalLayout.setVisibility(View.VISIBLE);
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
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPayBill) {
            String userAmt = txtPaidAmount.getText().toString();

            if (customerName == null || customerName.isEmpty()) {
                Toast.makeText(this, "Please Select Customer.", Toast.LENGTH_SHORT).show();
            }
            else if (userAmt.equals("")) {
                Toast.makeText(this, "Please Enter Amount.", Toast.LENGTH_SHORT).show();
            }
            else {
                String amt="",balAmt="",paidAmt="";

//                if(billAvailable == 1){
//                    lastBillRecordId = bill_Amount_instance.getLastBillRecordId();
//                    fromDate = bill_Amount_instance.getFromDate();
//                    toDate = bill_Amount_instance.getToDate();
//                    grandTotal = bill_Amount_instance.getGrandTotal();
//
//                    paidAmount = Integer.parseInt(userAmt);
//                    balanceAmount = grandTotal - paidAmount;
//
//                    amt = String.valueOf(grandTotal);
//                    paidAmt = String.valueOf(paidAmount);
//                    balAmt = String.valueOf(balanceAmount);
//                }
//                else {
//
//                    billAmount = balanceAmount;
//                    paidAmount = Integer.parseInt(userAmt);
//                    balanceAmount = billAmount - paidAmount;
//
//                    amt = String.valueOf(billAmount);
//                    paidAmt = String.valueOf(paidAmount);
//                    balAmt = String.valueOf(balanceAmount);
//                }
                paidAmount = Integer.parseInt(userAmt);
                if(paidAmount > grandTotal){
                    Toast.makeText(this, "Amount is greater than total amount.", Toast.LENGTH_SHORT).show();
                }
                else{

                    balanceAmount = grandTotal - paidAmount;

                    amt = String.valueOf(grandTotal);
                    paidAmt = String.valueOf(paidAmount);
                    balAmt = String.valueOf(balanceAmount);

                    boolean isInserted;
                    isInserted = mydb.User_Bill_Add(customerId, customerName,lastBillRecordId, fromDate,toDate,amt,paidAmt,balAmt,currentDate);

                    if (isInserted == true) {
//                    progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Amount  Details Added.", Toast.LENGTH_SHORT).show();
                        Intent gotoList = new Intent(this, MainActivity.class);
                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotoList);
                    }
                    else {
                        Toast.makeText(this, "Try Again.", Toast.LENGTH_SHORT).show();
                    }
                    ShowInterstitialAd();
                }

            }
        }
        if (v.getId() == R.id.btnFabBillHistory) {
            Intent gotoHistory = new Intent(this,Bill_History.class);
            gotoHistory.putExtra("customerId",customerId);
            gotoHistory.putExtra("customerName",customerName);
            startActivity(gotoHistory);
        }
    }

    public static void UpdatePrice(String fd, String td, int total, int gTotal, String lastRecordId) {

        fromDate = fd;
        toDate = td;
        lastBillRecordId = lastRecordId;
        grandTotal = gTotal;
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
