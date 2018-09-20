package com.mobitechs.chopadi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobitechs.chopadi.Bill_History;
import com.mobitechs.chopadi.R;
import com.mobitechs.chopadi.instance.Customer_Instance;
import com.mobitechs.chopadi.model.Bill_History_Items;

import java.util.List;

public class Bill_History_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Bill_History_Items> filteredListItems;
    List<Bill_History_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    RecyclerView recyclerView;
    Customer_Instance customer_Instance;

    public Bill_History_Adapter(Bill_History bill_History, List<Bill_History_Items> filteredListItems, RecyclerView recyclerView) {
        this.filteredListItems = filteredListItems;
        this.listItems = filteredListItems;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.last_paid_bill_details, viewGroup, false);
        viewHolder = new Bill_History_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Bill_History_Adapter.ViewHolder) {
            Bill_History_Adapter.ViewHolder vHolder = (Bill_History_Adapter.ViewHolder) viewHolder;
            Bill_History_Items itemOflist = filteredListItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
        return filteredListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblBillTitle,txtLastBillId,txtLastBillFromDate,txtLastBillToDate,txtLastBillBillAmount,txtLastBillPaidAmount,txtLastBillBalanceAmount,txtLastBillPaidDate;
        public View cardView;

        Bill_History_Items filteredListItems = new Bill_History_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            lblBillTitle = (TextView) itemView.findViewById(R.id.lblBillTitle);
            txtLastBillId = (TextView) itemView.findViewById(R.id.txtLastBillId);
            txtLastBillFromDate = (TextView) itemView.findViewById(R.id.txtLastBillFromDate);
            txtLastBillToDate = (TextView) itemView.findViewById(R.id.txtLastBillToDate);
            txtLastBillBillAmount = (TextView) itemView.findViewById(R.id.txtLastBillBillAmount);
            txtLastBillPaidAmount = (TextView) itemView.findViewById(R.id.txtLastBillPaidAmount);
            txtLastBillBalanceAmount = (TextView) itemView.findViewById(R.id.txtLastBillBalanceAmount);
            txtLastBillPaidDate = (TextView) itemView.findViewById(R.id.txtLastBillPaidDate);

            txtLastBillId.setVisibility(View.VISIBLE);
            lblBillTitle.setVisibility(View.GONE);

            cardView = itemView;
        }

        public void bindListDetails(final Bill_History_Items filteredListItems) {
            this.filteredListItems = filteredListItems;

            txtLastBillId.setText("Bill Id: "+filteredListItems.getbillId());
            txtLastBillFromDate.setText(filteredListItems.getfromDate());
            txtLastBillToDate.setText(filteredListItems.gettoDate());
            txtLastBillBillAmount.setText(filteredListItems.getbillAmount());
            txtLastBillPaidAmount.setText(filteredListItems.getpaidAmount());
            txtLastBillBalanceAmount.setText(filteredListItems.getbalanceAmount());
            txtLastBillPaidDate.setText(filteredListItems.getentryDate());
        }
    }
    
}