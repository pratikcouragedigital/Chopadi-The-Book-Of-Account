package com.mobitechs.chopadi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobitechs.chopadi.Pay_Bill;
import com.mobitechs.chopadi.R;
import com.mobitechs.chopadi.instance.Bill_Amount_instance;
import com.mobitechs.chopadi.model.Bill_Items;

import java.util.List;

public class Pay_Bill_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Bill_Items> listFilteredItem;
    List<Bill_Items> listItem;
    View v;
    TextView txtGrandTotal,txtFromDate,txtToDate,txtTotal,txtBalance;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    int counterNo = 0,listSize=0;
    int total = 0;
    int balanceAmount = 0;
    String fromDate,toDate,lastRecordId;
    Bill_Amount_instance bill_Amount_instance;
    String billAmount="",paidAmount="",balance="",billEntryDate="";
    RelativeLayout payBillLayout;

//    public Pay_Bill_List_Adapter(List<Bill_Items> items, TextView txtTotal, TextView txtBalance, TextView txtGrandTotal, TextView txtFromDate, TextView txtToDate,int billAmount,int paidAmount, int balanceAmount,String fromDate,String toDate,String billEntryDate) {
//        this.listFilteredItem = items;
//        this.listItem = items;
//        this.txtTotal = txtTotal;
//        this.txtBalance = txtBalance;
//        this.txtGrandTotal = txtGrandTotal;
//        this.txtFromDate = txtFromDate;
//        this.txtToDate = txtToDate;
//        this.balanceAmount = balanceAmount;
//        this.billEntryDate = billEntryDate;
//        this.fromDate = fromDate;
//        this.toDate = toDate;
//        this.billAmount = String.valueOf(billAmount);
//        this.paidAmount = String.valueOf(paidAmount);
//        this.balance = String.valueOf(balanceAmount);
//    }

    public Pay_Bill_List_Adapter(List<Bill_Items> listItems, TextView txtTotal, TextView txtBalance, TextView txtGrandTotal, TextView txtFromDate, TextView txtToDate, int balanceAmount, RelativeLayout payBillLayout) {
        this.listFilteredItem = listItems;
        this.listItem = listItems;
        this.txtTotal = txtTotal;
        this.txtBalance = txtBalance;
        this.txtGrandTotal = txtGrandTotal;
        this.txtFromDate = txtFromDate;
        this.txtToDate = txtToDate;
        this.balanceAmount = balanceAmount;
        this.payBillLayout = payBillLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Pay_Bill_List_Adapter.EmptyViewHolder emptyViewHolder = new Pay_Bill_List_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_report_tab_monthly_items, viewGroup, false);
        viewHolder = new Pay_Bill_List_Adapter.ViewHolder(v);
        payBillLayout.setVisibility(View.VISIBLE);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Pay_Bill_List_Adapter.ViewHolder) {
            Pay_Bill_List_Adapter.ViewHolder vHolder = (Pay_Bill_List_Adapter.ViewHolder) viewHolder;
            Bill_Items itemOflist = listFilteredItem.get(position);
            vHolder.bindListDetails(itemOflist, position);
        }
    }

    @Override
    public int getItemCount() {
        if (listFilteredItem.size() > 0) {
            listSize = listFilteredItem.size();
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

        public TextView txtSrNo, txtPDate,txtProductName, txtProductPrice, txtQty, txtProductTotal;

        public View cardView;

        Bill_Items listFilteredItem = new Bill_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            bill_Amount_instance = new Bill_Amount_instance();

            txtSrNo = (TextView) itemView.findViewById(R.id.txtSrNo);
            txtPDate = (TextView) itemView.findViewById(R.id.txtPDate);
            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtProductPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            txtQty = (TextView) itemView.findViewById(R.id.txtQty);
            txtProductTotal = (TextView) itemView.findViewById(R.id.txtTotal);
            cardView = itemView;

        }

        public void bindListDetails(Bill_Items listFilteredItem, int position) {
            this.listFilteredItem = listFilteredItem;

            counterNo = position + 1;
            total = total + Integer.parseInt(listFilteredItem.gettotalPrice());
            int grandTotal = total + balanceAmount;

            if(counterNo == 1 ){
                fromDate = listFilteredItem.getbillDate();
                bill_Amount_instance.setFromDate(fromDate);
                txtFromDate.setText(listFilteredItem.getbillDate());
            }
            if(counterNo == listSize ){
                toDate = listFilteredItem.getbillDate();
                lastRecordId = listFilteredItem.getbillId();
                bill_Amount_instance.setToDate(toDate);
                bill_Amount_instance.setLastBillRecordId(lastRecordId);
                txtToDate.setText(listFilteredItem.getbillDate());

                Pay_Bill.UpdatePrice(fromDate,toDate,total,grandTotal,lastRecordId);
            }

            txtSrNo.setText(counterNo + "");
//            txtSrNo.setText(listFilteredItem.getbillId());
            txtPDate.setText(listFilteredItem.getbillDate());
            txtProductName.setText(listFilteredItem.getproductName());
            txtProductPrice.setText(listFilteredItem.getproductPrice());
            txtQty.setText(listFilteredItem.getproductQty());
            txtProductTotal.setText(listFilteredItem.gettotalPrice());
            txtTotal.setText("Rs." + total);
            txtGrandTotal.setText("Rs." + grandTotal);
            txtBalance.setText("Rs." + balanceAmount);

            bill_Amount_instance.setGrandTotal(grandTotal);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            payBillLayout.setVisibility(View.GONE);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("New Bill details not available. \n बिल विस्तार उपलब्ध नहीं है.");
        }
    }
}

