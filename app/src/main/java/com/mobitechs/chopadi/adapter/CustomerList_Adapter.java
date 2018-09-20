package com.mobitechs.chopadi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobitechs.chopadi.Bill_Add;
import com.mobitechs.chopadi.Bill_Report_Tab;
import com.mobitechs.chopadi.Customer_List;
import com.mobitechs.chopadi.Pay_Bill;
import com.mobitechs.chopadi.R;
import com.mobitechs.chopadi.instance.Customer_Instance;
import com.mobitechs.chopadi.model.Customer_Items;

import java.util.ArrayList;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class CustomerList_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    List<Customer_Items> filteredListItems;
    List<Customer_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    RecyclerView recyclerView;
    Customer_Instance customer_Instance;

    SharedPreferences wmbPreference;
    private FancyShowCaseQueue fancyShowCaseQueue;

    public CustomerList_Adapter(Customer_List customer_list, List<Customer_Items> filteredListItems, RecyclerView recyclerView, Activity activity) {
        this.activity = activity;
        this.filteredListItems = filteredListItems;
        this.listItems = filteredListItems;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_list_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Customer_Items itemOflist = filteredListItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        return filteredListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView custName,custNo;
        public View cardView;
        LinearLayout billReportLayout,billDetailsLayout,billAddLayout;

        Customer_Items filteredListItems = new Customer_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            customer_Instance = new Customer_Instance();
            custName = (TextView) itemView.findViewById(R.id.custName);
            custNo = (TextView) itemView.findViewById(R.id.custNo);
            billReportLayout = (LinearLayout) itemView.findViewById(R.id.billReportLayout);
            billDetailsLayout = (LinearLayout) itemView.findViewById(R.id.billDetailsLayout);
            billAddLayout = (LinearLayout) itemView.findViewById(R.id.billAddLayout);

            cardView = itemView;
            billReportLayout.setOnClickListener(this);
            billDetailsLayout.setOnClickListener(this);
            billAddLayout.setOnClickListener(this);
            custNo.setOnClickListener(this);

            wmbPreference = PreferenceManager.getDefaultSharedPreferences(activity);

            boolean custBillReportShowCase = wmbPreference.getBoolean("custBillReportShowCase", true);

            if (custBillReportShowCase) {
                // Code to run once
                final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(activity)
                        .title("\n\n\n Click here to add Bill. \n\n\n  बिल ऐड करने के लिए यहाँ क्लिक करे.")
                        .focusOn(billAddLayout)
                        .build();
                final FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(activity)
                        .title("\n\n\n Click here to check bill amount. \n\n\n  बिल चेक करने के लिए यहाँ क्लिक करे.")
                        .focusOn(billDetailsLayout)
                        .build();
                final FancyShowCaseView fancyShowCaseView3 = new FancyShowCaseView.Builder(activity)
                        .title("\n\n\n Click here to check bill report. \n\n\n  बिल रिपोर्ट देखने के लिए यहाँ क्लिक करे.")
                        .focusOn(billReportLayout)
                        .build();

                final FancyShowCaseView fancyShowCaseView4 = new FancyShowCaseView.Builder(activity)
                        .title("\n\n\n Swipe Right to Edit. \n\n\n  एडिट करने के लिए बाये ओर सरकाये.")
                        .build();
                final FancyShowCaseView fancyShowCaseView5 = new FancyShowCaseView.Builder(activity)
                        .title("\n\n\n Swipe Right to Delete. \n\n\n  बडिलीट करने के लिए दाहिने ओर सरकाये.")
                        .build();

                fancyShowCaseQueue = new FancyShowCaseQueue()
                        .add(fancyShowCaseView1)
                        .add(fancyShowCaseView2)
                        .add(fancyShowCaseView3)
                        .add(fancyShowCaseView4)
                        .add(fancyShowCaseView5);

                fancyShowCaseQueue.show();
            }
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("custBillReportShowCase", false);
            editor.commit();

        }

        public void bindListDetails(final Customer_Items filteredListItems) {
            this.filteredListItems = filteredListItems;
            custName.setText(filteredListItems.getcustName());
            custNo.setText(filteredListItems.getcustNo());
        }

        @Override
        public void onClick(final View v) {

            String cId = filteredListItems.getcustId();
            customer_Instance.setCustomerId(cId);

            if(v.getId() == R.id.billReportLayout){
                Intent gotoBill = new Intent(v.getContext(), Bill_Report_Tab.class);
                gotoBill.putExtra("customerId",filteredListItems.getcustId());
                v.getContext().startActivity(gotoBill);
            }
            else if(v.getId() == R.id.custNo){
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + filteredListItems.getcustNo()));
                v.getContext().startActivity(callIntent);
            }
            else if(v.getId() == R.id.billDetailsLayout){
                Intent gotoBill = new Intent(v.getContext(), Pay_Bill.class);
                gotoBill.putExtra("customerId",filteredListItems.getcustId());
                gotoBill.putExtra("customerName",filteredListItems.getcustName());
                v.getContext().startActivity(gotoBill);
            }
            else if(v.getId() == R.id.billAddLayout){
                String imFrom ="customerList";
                Intent gotoBill = new Intent(v.getContext(), Bill_Add.class);
                gotoBill.putExtra("customerId",filteredListItems.getcustId());
                gotoBill.putExtra("customerName",filteredListItems.getcustName());
                gotoBill.putExtra("imFrom",imFrom);
                v.getContext().startActivity(gotoBill);
            }
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredListItems = listItems;
                }
                else {
                    List<Customer_Items> filteredList = new ArrayList<>();
                    for (Customer_Items row : listItems) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getcustName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    filteredListItems = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredListItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredListItems = (ArrayList<Customer_Items>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}