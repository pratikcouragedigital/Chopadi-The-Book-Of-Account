package com.mobitechs.chopadi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitechs.chopadi.Bill_Report_Tab_Monthly;
import com.mobitechs.chopadi.R;
import com.mobitechs.chopadi.model.Bill_Items;

import java.util.List;

public class Bill_Report_Adapter_Monthly extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Bill_Items> listFilteredItem;
    List<Bill_Items> listItem;
    View v;
    TextView txtGrandTotal;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    int counterNo = 0;

    int grandTotal = 0;

    public Bill_Report_Adapter_Monthly(List<Bill_Items> items, TextView txtGrandTotal) {
        this.listFilteredItem = items;
        this.listItem = items;
        this.txtGrandTotal = txtGrandTotal;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Bill_Report_Adapter_Monthly.EmptyViewHolder emptyViewHolder = new Bill_Report_Adapter_Monthly.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_report_tab_monthly_items, viewGroup, false);
        viewHolder = new Bill_Report_Adapter_Monthly.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Bill_Report_Adapter_Monthly.ViewHolder) {
            Bill_Report_Adapter_Monthly.ViewHolder vHolder = (Bill_Report_Adapter_Monthly.ViewHolder) viewHolder;
            Bill_Items itemOflist = listFilteredItem.get(position);
            vHolder.bindListDetails(itemOflist, position);
            viewHolder.setIsRecyclable(false);
        }
    }

    @Override
    public int getItemCount() {
        if (listFilteredItem.size() > 0) {
            return listFilteredItem.size();
        } else {
            return 1;
        }
    }

    public int getItemViewType(int position) {
        if (listFilteredItem.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtSrNo, txtPDate, txtProductName, txtProductPrice, txtQty, txtTotal;

        public View cardView;

        Bill_Items listFilteredItem = new Bill_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            txtSrNo = (TextView) itemView.findViewById(R.id.txtSrNo);
            txtPDate = (TextView) itemView.findViewById(R.id.txtPDate);
            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtProductPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            txtQty = (TextView) itemView.findViewById(R.id.txtQty);
            txtTotal = (TextView) itemView.findViewById(R.id.txtTotal);
            cardView = itemView;
        }

        public void bindListDetails(Bill_Items listFilteredItem, int position) {
            this.listFilteredItem = listFilteredItem;

            grandTotal = grandTotal + Integer.parseInt(listFilteredItem.gettotalPrice());
            counterNo = position + 1;

            txtSrNo.setText(counterNo + "");
            txtPDate.setText(listFilteredItem.getbillDate());
            txtProductName.setText(listFilteredItem.getproductName());
            txtProductPrice.setText(listFilteredItem.getproductPrice());
            txtQty.setText(listFilteredItem.getproductQty());
            txtTotal.setText(listFilteredItem.gettotalPrice());
            txtGrandTotal.setText("Rs." + grandTotal);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Bill details not available of the month u selected. \n आपके द्वारा चुने गए महीने के बिल विस्तार उपलब्ध नहीं हैं.");
        }
    }

}

