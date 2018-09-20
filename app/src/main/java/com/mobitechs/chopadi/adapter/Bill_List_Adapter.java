package com.mobitechs.chopadi.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitechs.chopadi.Bill_List_DateWise;
import com.mobitechs.chopadi.Bill_Report_Tab;
import com.mobitechs.chopadi.R;
import com.mobitechs.chopadi.instance.Customer_Instance;
import com.mobitechs.chopadi.model.Bill_Items;

import java.util.List;

public class Bill_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Bill_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    RecyclerView recyclerView;
    Customer_Instance customer_Instance;


    public Bill_List_Adapter(Bill_List_DateWise bill_listDaily, List<Bill_Items> listItems, RecyclerView recyclerView) {
        this.listItems = listItems;
        this.recyclerView = recyclerView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_list_datewise_items, viewGroup, false);
        viewHolder = new Bill_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Bill_List_Adapter.ViewHolder) {
            Bill_List_Adapter.ViewHolder vHolder = (Bill_List_Adapter.ViewHolder) viewHolder;
            Bill_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title,price;
        public View cardView;

        TextView txtCustomerName,txtDate,txtTotal;

        Bill_Items listItems = new Bill_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            customer_Instance = new Customer_Instance();

            txtCustomerName = (TextView) itemView.findViewById(R.id.txtCustomerName);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtTotal = (TextView) itemView.findViewById(R.id.txtTotal);

            cardView = itemView;
            cardView.setOnClickListener(this);
        }

        public void bindListDetails(final Bill_Items listItems) {
            this.listItems = listItems;

            txtCustomerName.setText(listItems.getcustomerName());
            txtDate.setText(listItems.getbillDate());
            txtTotal.setText(listItems.gettotalPrice());
        }

        @Override
        public void onClick(final View v) {

            if (this.listItems != null) {
                String cId = listItems.getcustomerId();
                String billDate = listItems.getbillDate();
                customer_Instance.setCustomerId(cId);
                customer_Instance.setSelectedDate(billDate);

                Intent billDetails = new Intent(v.getContext(), Bill_Report_Tab.class);
                billDetails.putExtra("customerId",listItems.getcustomerId());
                billDetails.putExtra("date",listItems.getbillDate());
                v.getContext().startActivity(billDetails);
            }

        }
    }
}