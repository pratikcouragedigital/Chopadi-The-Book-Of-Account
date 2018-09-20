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
import android.view.View;
import android.widget.Toast;

import com.mobitechs.chopadi.adapter.CustomerList_Adapter;
import com.mobitechs.chopadi.adapter.ProductList_Adapter;
import com.mobitechs.chopadi.model.Product_Items;
import com.mobitechs.chopadi.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class Product_List extends AppCompatActivity implements View.OnClickListener{

    Activity activity;
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    SwipeController swipeController = null;
    JSONArray listArray = null;
    private DataBaseHelper mydb;
    FloatingActionButton btnFab;

    public List<Product_Items> listItems = new ArrayList<Product_Items>();
    private FancyShowCaseQueue fancyShowCaseQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        activity = Product_List.this;

        mydb = new DataBaseHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        btnFab = (FloatingActionButton) findViewById(R.id.btnFab);
        btnFab.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new ProductList_Adapter(this, listItems, recyclerView,activity,btnFab);
        recyclerView.setAdapter(reviewAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);

                String id = listItems.get(position).getproductId();
                String name = listItems.get(position).getproductName();
                String price = listItems.get(position).getprice();
                String imFrom="Edit";
                Intent i = new Intent(Product_List.this, Product_Add.class);
                i.putExtra("id", id);
                i.putExtra("name", name);
                i.putExtra("price", price);
                i.putExtra("imFrom", imFrom);
                startActivity(i);

            }

            @Override
            public void onRightClicked(int position) {

                final String productId = listItems.get(position).getproductId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Product_List.this);
                builder.setTitle("Confirmation (पुष्टीकरण)");
                builder.setMessage("Do you really want to delete this Product? \n क्या आप वास्तव में इस वस्तु को मिटाना चाहते हैं?");
                builder.setPositiveButton("Ok (ठीक)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean isDeleted = false;
                        try {
                            isDeleted = mydb.Product_Delete(productId);

                            if (isDeleted == true) {
                                Toast.makeText(Product_List.this, "Product Deleted.", Toast.LENGTH_SHORT).show();
                                reviewAdapter.notifyDataSetChanged();
                                Intent gotoList = new Intent(Product_List.this, Product_List.class);
                                gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(gotoList);
                            }else{
                                Toast.makeText(Product_List.this, "Please Try Again Deleted.", Toast.LENGTH_SHORT).show();
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
        boolean addProductShowCase = wmbPreference.getBoolean("addProductShowCase", true);
        if (addProductShowCase) {
            // Code to run once
            final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
                    .title("Click here to add product. \n\n\n  बिप्रोडक्ट ऐड करने के लिए यहाँ क्लिक करे.")
                    .focusOn(btnFab)
                    .build();

            fancyShowCaseQueue = new FancyShowCaseQueue()
                    .add(fancyShowCaseView1);

            fancyShowCaseQueue.show();

            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("addProductShowCase", false);
            editor.commit();
        }

        getList();
    }


    private void getList() {
        try {
            listArray = mydb.Product_List();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {

            Toast.makeText(this, "Product Not Available", Toast.LENGTH_SHORT).show();
        } else {

            listItems.clear();

            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);

                    Product_Items expense_Items = new Product_Items();
                    expense_Items.setproductId(obj.getString("productId"));
                    expense_Items.setproductName(obj.getString("productName"));
                    expense_Items.setprice(obj.getString("productPrice"));


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
            Intent gotoAddPage = new Intent(this,Product_Add.class);
            gotoAddPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(gotoAddPage);
        }
    }
}