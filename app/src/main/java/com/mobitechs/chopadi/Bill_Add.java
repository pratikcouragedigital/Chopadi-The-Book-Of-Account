package com.mobitechs.chopadi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.adapter.Std_Div_Filter_Adapter;
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

public class Bill_Add extends AppCompatActivity implements View.OnClickListener {

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day, currentDay,currentmonth,currentYear;
    String selectedDate;

    Spinner spinnerCustomer;
    String customerName;
    String customerId;
    private String[] customerArrayLis;
    private Std_Div_Filter_Adapter spinnerAdapter;
    private List<String> customerIdList = new ArrayList<String>();
    private List<String> customerNameList = new ArrayList<String>();

    Spinner spinnerForProduct;
    String productName;
    String productPrice;
    String productId;
    private String[] productArrayLis;
    private Std_Div_Filter_Adapter productSpinnerAdapter;
    private List<String> productIdList = new ArrayList<String>();
    private List<String> productNameList = new ArrayList<String>();
    private List<String> productPriceList = new ArrayList<String>();

    Button btnAdd;
    EditText txtDate, txtQty;
    private DataBaseHelper mydb;
    String entryDate = "";
    String imFrom;

    int qty = 0;
    private AdView adView, adView1;

    InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_add);

        mInterstitialAd = new InterstitialAd(this);
        ShowBannerAds();

        mydb = new DataBaseHelper(this);

        Intent intent = getIntent();
        if (null != intent) {
            customerId = intent.getStringExtra("customerId");
            customerName = intent.getStringExtra("customerName");
            imFrom = intent.getStringExtra("imFrom");
        }

        spinnerForProduct = (Spinner) findViewById(R.id.spinnerForProduct);
        spinnerCustomer = (Spinner) findViewById(R.id.spinnerCustomer);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtQty = (EditText) findViewById(R.id.txtQty);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        currentDay = day;
        currentmonth = month;
        currentYear = year;

        String currMonth = String.valueOf(month + 1);
        selectedDate = day + "-" + currMonth + "-" + year;
        entryDate = day + "-" + currMonth + "-" + year;
        txtDate.setText(entryDate);

        btnAdd.setOnClickListener(this);
        txtDate.setOnClickListener(this);

        if(imFrom == null){
            imFrom = "";
        }

        if(imFrom.equals("customerList")){
            spinnerCustomer.setVisibility(View.GONE);
        }
        else{
            spinnerCustomer.setVisibility(View.VISIBLE);
            getCustomerDetails();
        }

        getProductPrice();
    }

    private void ShowBannerAds() {
        String deviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        adView = (AdView) findViewById(R.id.adView);
        adView1 = (AdView) findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder()
//                //   .addTestDevice(deviceId)
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

    private void getProductPrice() {

        productArrayLis = new String[]{
                "Select Product"
        };
        productNameList = new ArrayList<>(Arrays.asList(productArrayLis));
        productSpinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, productNameList);
        getListOfProduct();
        productSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForProduct.setAdapter(productSpinnerAdapter);
        spinnerForProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    productName = parent.getItemAtPosition(position).toString();
                    productId = productIdList.get(position);
                    productPrice = productPriceList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfProduct() {
        JSONArray productListArray = null;
        try {

            productListArray = mydb.Product_List();

            if (productListArray.length() == 0) {
                Toast.makeText(this, "Product Not Available", Toast.LENGTH_SHORT).show();
            } else {

                productIdList.clear();
                productNameList.clear();
                productPriceList.clear();
                productSpinnerAdapter.notifyDataSetChanged();

                productNameList.add("Select Product");
                productIdList.add("0");
                productPriceList.add("0");

                for (int i = 0; i < productListArray.length(); i++) {
                    try {
                        JSONObject obj = productListArray.getJSONObject(i);

                        productIdList.add(obj.getString("productId"));
                        productNameList.add(obj.getString("productName"));
                        productPriceList.add(obj.getString("productPrice"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                productSpinnerAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getCustomerDetails() {

        customerArrayLis = new String[]{
                "Select Customer"
        };
        customerNameList = new ArrayList<>(Arrays.asList(customerArrayLis));
        spinnerAdapter = new Std_Div_Filter_Adapter(this, R.layout.spinner_item, customerNameList);
        getListOfCustomer();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCustomer.setAdapter(spinnerAdapter);
        spinnerCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    customerName = parent.getItemAtPosition(position).toString();
                    customerId = customerIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfCustomer() {

        JSONArray userListArray = null;
        try {

            userListArray = mydb.Customer_List();

            if (userListArray.length() == 0) {
                Toast.makeText(this, "Customer Not Available", Toast.LENGTH_SHORT).show();
            } else {
                customerIdList.clear();
                customerNameList.clear();
                spinnerAdapter.notifyDataSetChanged();
                customerNameList.add("Select Customer");
                customerIdList.add("0");
                for (int i = 0; i < userListArray.length(); i++) {
                    try {
                        JSONObject obj = userListArray.getJSONObject(i);
                        customerIdList.add(obj.getString("customerId"));
                        customerNameList.add(obj.getString("customerName"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                spinnerAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.btnAdd) {
            String productQty = txtQty.getText().toString();
            if (selectedDate == "") {
                Toast.makeText(this, "Please Select Date.", Toast.LENGTH_SHORT).show();
            } else if (customerName == null || customerName.isEmpty()) {
                Toast.makeText(this, "Please Select Customer.", Toast.LENGTH_SHORT).show();
            } else if (productName == null || productName.isEmpty()) {
                Toast.makeText(this, "Please Select Product.", Toast.LENGTH_SHORT).show();
            } else if (productQty.equals("")) {
                Toast.makeText(this, "Please Enter Qty.", Toast.LENGTH_SHORT).show();
            } else {
                qty = Integer.parseInt(productQty);
                int pPrice = Integer.parseInt(productPrice);
                int total = qty * pPrice;
                String pTotal = String.valueOf(total);

                String aYear = String.valueOf(year);
                String aMonth = String.valueOf(month + 1);
                String aDay = String.valueOf(day);

                boolean isInserted;
                isInserted = mydb.Bill_Generate(customerId, customerName, entryDate, selectedDate, productName, productPrice, productQty, pTotal, aDay, aMonth, aYear);

                if (isInserted == true) {
//                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Product Details Added.", Toast.LENGTH_SHORT).show();

                    if(imFrom.equals("customerList")){
                        Intent gotoList = new Intent(this, Customer_List.class);
                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotoList);
                    }
                    else{
                        Intent gotoList = new Intent(this, MainActivity.class);
                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotoList);
                    }
                } else {
                    Toast.makeText(this, "Try Again.", Toast.LENGTH_SHORT).show();
                }

            }
        }
        else if (v.getId() == R.id.txtDate) {

            DatePickerDialog mDatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    selectedDate = String.valueOf(selectedday) + "-" + String.valueOf(selectedmonth + 1) + "-" + String.valueOf(selectedyear);
                    //entryDate = String.valueOf(selectedday) + "-" + String.valueOf(selectedmonth + 1) + "-" + String.valueOf(selectedyear);

                    //selectedDate = DateFormatter.ChangeDateFormat(selectedDate);

                    day = selectedday;
                    month = selectedmonth;
                    year = selectedyear;

                    // do what u want
                }
            }, year, month, day);

//            if(year > currentYear){
//                Toast.makeText( v.getContext(), "Please select Valid Year", Toast.LENGTH_SHORT).show();
//            }
//            else if(year == currentYear){
//
//                if(month > currentmonth){
//                    Toast.makeText( v.getContext(), "Please select Valid Month", Toast.LENGTH_SHORT).show();
//                }
//                else if(month == currentmonth){
//                    if(day > currentDay){
//                        Toast.makeText( v.getContext(), "Please select Valid Day", Toast.LENGTH_SHORT).show();
//                    }else{
//                        txtDate.setText(selectedDate);
//                    }
//                }
//                else{
//                    txtDate.setText(selectedDate);
//                }
//            }
//            else{
//                txtDate.setText(selectedDate);
//
//            }
            mDatePicker.show();
        }
    }
}
