package com.mobitechs.chopadi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mobitechs.chopadi.adapter.CustomerList_Adapter;
import com.mobitechs.chopadi.model.Customer_Items;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnViewInflateListener;

public class Customer_List extends AppCompatActivity implements View.OnClickListener{

    Activity activity;
    RecyclerView recyclerView;
    CustomerList_Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    SwipeController swipeController = null;
    JSONArray listArray = null;
    private DataBaseHelper mydb;

    public List<Customer_Items> listItems = new ArrayList<Customer_Items>();
    FloatingActionButton btnFab;
    EditText txtSearch;
    String searchText;

    InterstitialAd mInterstitialAd;

    private FancyShowCaseQueue fancyShowCaseQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_list);

        activity = Customer_List.this;

        mInterstitialAd = new InterstitialAd(this);
        ShowInterstitialAd();

        mydb = new DataBaseHelper(this);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        btnFab = (FloatingActionButton) findViewById(R.id.btnFab);
        btnFab.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new CustomerList_Adapter(this, listItems, recyclerView,activity);
        recyclerView.setAdapter(reviewAdapter);

        txtSearch.setOnClickListener(this);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                searchText = String.valueOf(s);
//                Toast.makeText(v.getContext(), ""+searchText, Toast.LENGTH_SHORT).show();
                reviewAdapter.getFilter().filter(searchText);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

        });

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                String id = listItems.get(position).getcustId();
                String name = listItems.get(position).getcustName();
                String no = listItems.get(position).getcustNo();
                String imFrom = "Edit";

                Intent i = new Intent(Customer_List.this, Customer_Add.class);
                i.putExtra("id", id);
                i.putExtra("name", name);
                i.putExtra("no", no);
                i.putExtra("imFrom", imFrom);
                startActivity(i);

            }

            @Override
            public void onRightClicked(int position) {

                final String custId = listItems.get(position).getcustId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Customer_List.this);
                builder.setTitle("Confirmation (पुष्टीकरण)");
                builder.setMessage("Do you really want to delete this Customer? \n क्या आप वास्तव में इस ग्राहक को हटाना चाहते हैं?");
                builder.setPositiveButton("Ok (ठीक)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean isDeleted = false;
                        try {
                            isDeleted = mydb.Customer_Delete(custId);

                            if (isDeleted == true) {
                                Toast.makeText(Customer_List.this, "Customer Deleted. \n ग्राहक हटा दिया गया", Toast.LENGTH_SHORT).show();
                                reviewAdapter.notifyDataSetChanged();
                                Intent gotoList = new Intent(Customer_List.this, Customer_List.class);
                                gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(gotoList);

                            }else{
                                Toast.makeText(Customer_List.this, "Please Try Again Deleted.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel (रद्द करना)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean customerAddShowCase = wmbPreference.getBoolean("customerAddShowCase", true);
        if (customerAddShowCase) {
            // Code to run once
            final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
                    .title("Click here to add customer. \n\n\n  कस्टमर ऐड करने के लिए यहाँ क्लिक करे.")
                    .focusOn(btnFab)
                    .build();

            fancyShowCaseQueue = new FancyShowCaseQueue()
                    .add(fancyShowCaseView1);

            fancyShowCaseQueue.show();

            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("customerAddShowCase", false);
            editor.commit();
        }
        txtSearch.setFocusable(false);
        getList();
    }

    private void getList() {
        try {
            listArray = mydb.Customer_List();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();
        if (dataCount == 0) {

            Toast.makeText(this, "Customer Not Available \n ग्राहक उपलब्ध नहीं है", Toast.LENGTH_SHORT).show();
        }
        else {
            listItems.clear();
            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);

                    Customer_Items expense_Items = new Customer_Items();
                    expense_Items.setcustId(obj.getString("customerId"));
                    expense_Items.setcustName(obj.getString("customerName"));
                    expense_Items.setcustNo(obj.getString("customerNo"));

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
        if(v.getId() == R.id.btnFab){
            Intent gotoAddPage = new Intent(this,Customer_Add.class);
            gotoAddPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(gotoAddPage);
        }
        else if(v.getId() == R.id.txtSearch){
            txtSearch.setFocusable(true);
            txtSearch.setFocusableInTouchMode(true);
            txtSearch.requestFocus();
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
