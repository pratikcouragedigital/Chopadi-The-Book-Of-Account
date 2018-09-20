package com.mobitechs.chopadi.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitechs.chopadi.Monthly_Income_Details;
import com.mobitechs.chopadi.R;
import com.mobitechs.chopadi.model.MonthlyIncome_Items;

import java.util.List;

public class MonthlyIncome_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<MonthlyIncome_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    int counterNo = 0;

    public String[] monthNameArray = new String[]{"Select Month","January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};


    public MonthlyIncome_Adapter( List<MonthlyIncome_Items> listItems) {
        this.listItems = listItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.monthly_income_items, viewGroup, false);
        viewHolder = new MonthlyIncome_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MonthlyIncome_Adapter.ViewHolder) {
            MonthlyIncome_Adapter.ViewHolder vHolder = (MonthlyIncome_Adapter.ViewHolder) viewHolder;
            MonthlyIncome_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtMonthName,txtDate,txtIncomeAmount;
        public View cardView;

        MonthlyIncome_Items listItems = new MonthlyIncome_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            txtMonthName = (TextView) itemView.findViewById(R.id.txtMonthName);
//            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtIncomeAmount = (TextView) itemView.findViewById(R.id.txtIncomeAmount);

            cardView = itemView;
            cardView.setOnClickListener(this);
        }

        public void bindListDetails(final MonthlyIncome_Items listItems) {
            this.listItems = listItems;

            int monthNo = Integer.parseInt(listItems.getmonth());

            String monthName = monthNameArray[monthNo];

            txtMonthName.setText(monthName +" - "+ listItems.getyear());
//            txtDate.setText("From "+listItems.getfromDate() +" To;"+ listItems.gettoDate());
            txtIncomeAmount.setText("Rs. "+listItems.gettotalPrice());
        }

        @Override
        public void onClick(final View v) {

            if(this.listItems != null){

                Intent gotoDetails = new Intent(v.getContext(), Monthly_Income_Details.class);
                gotoDetails.putExtra("month", listItems.getmonth());
                gotoDetails.putExtra("year", listItems.getyear());
                v.getContext().startActivity(gotoDetails);


            }
        }
    }
}
