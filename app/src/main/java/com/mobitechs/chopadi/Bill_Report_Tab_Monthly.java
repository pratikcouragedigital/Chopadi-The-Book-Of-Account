package com.mobitechs.chopadi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitechs.chopadi.adapter.Bill_Report_Adapter_Monthly;
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

public class Bill_Report_Tab_Monthly extends Fragment implements View.OnClickListener {

    List<Bill_Items> listItems = new ArrayList<Bill_Items>();
    Bill_Report_Adapter_Monthly adapter;
    RecyclerView recyclerView;

    TextView txtDate;
    FloatingActionButton btnFab;
    LinearLayoutManager linearLayoutManager;
    private View v;

    JSONArray listArray = null;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    String customerId = "";
    static TextView txtGrandTotal;
    LinearLayout totalLayout, titleLayout;

    private DataBaseHelper mydb;
    Customer_Instance customer_Instance;
    Spinner spinnerMonth;

    String monthName = "";
    int monthId;
//    String userYear = "2018";

    public String[] monthNameArray = new String[]{"Select Month", "January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    public int[] monthIdArray = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bill_report_tab_monthly, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mydb = new DataBaseHelper(v.getContext());
        customer_Instance = new Customer_Instance();

        customerId = customer_Instance.getCustomerId();
//        Toast.makeText(v.getContext(), customerId+"", Toast.LENGTH_SHORT).show();
//        spinnerMonth = (Spinner) getView().findViewById(R.id.spinnerForMonth);
        totalLayout = (LinearLayout) getView().findViewById(R.id.totalLayout);
        titleLayout = (LinearLayout) getView().findViewById(R.id.titleLayout);

        txtGrandTotal = (TextView) getView().findViewById(R.id.txtGrandTotal);
        txtDate = (TextView) getView().findViewById(R.id.txtDate);
        btnFab = (FloatingActionButton) getView().findViewById(R.id.btnFab);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        monthName = monthNameArray[month + 1].toString();
        monthId = monthIdArray[month + 1];
        txtDate.setText("Month: " + monthName);

        btnFab.setOnClickListener(this);

        getList();
    }

    private void getList() {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        adapter = new Bill_Report_Adapter_Monthly(listItems,txtGrandTotal);
        recyclerView.setAdapter(adapter);

        listItems.clear();

        try {
            listArray = mydb.Customer_BillDetails_MonthWise(customerId, String.valueOf(monthId), String.valueOf(year));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {
            titleLayout.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            Toast.makeText(v.getContext(), "Details Not Available", Toast.LENGTH_SHORT).show();
        } else {
            listItems.clear();
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

            //Dynamic Spinner
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
            int margin = (int) getResources().getDimension(R.dimen.margin);

            RelativeLayout layout = new RelativeLayout(getActivity());
            layout.setLayoutParams(new RelativeLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    CoordinatorLayout.LayoutParams.WRAP_CONTENT));

            spinnerMonth = new Spinner(getActivity());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            spinnerMonth.setLayoutParams(params);
            params.setMargins(margin, margin, margin, margin);
            spinnerMonth.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.border) );
            spinnerMonth.setId(Integer.parseInt("1"));


            layout.addView(spinnerMonth);

            alert.setTitle(Html.fromHtml("<b>" + "Select Month." + "</b>"));

            getMonthList();
           
            alert.setView(layout);

            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (monthName == null || monthName.isEmpty()) {
                        Toast.makeText(getActivity(), "Please select Month.", Toast.LENGTH_LONG).show();
                    }  else {
                        getList();
                    }
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
            alert.show();
        }
    }

    private void getMonthList() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(v.getContext(),
                android.R.layout.simple_spinner_item, monthNameArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(dataAdapter);

        for (int i = 0; i < monthNameArray.length; i++) {
            if (monthNameArray[i].contains(monthName)) {
                month = i;
            }
        }
        spinnerMonth.setSelection(month);


        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    monthName = parent.getItemAtPosition(position).toString();
                    monthId = monthIdArray[position];
                    txtDate.setText("Month: " + monthName + " Id: " + monthId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}