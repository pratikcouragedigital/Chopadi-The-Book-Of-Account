package com.mobitechs.chopadi.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.mobitechs.chopadi.Bill_Add;
import com.mobitechs.chopadi.Customer_List;
import com.mobitechs.chopadi.Monthly_Income;
import com.mobitechs.chopadi.Bill_List_DateWise;
import com.mobitechs.chopadi.MainActivity;
import com.mobitechs.chopadi.Product_List;
import com.mobitechs.chopadi.R;
import com.mobitechs.chopadi.model.Home_Items;

import java.util.List;

public class Home_Menu_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Home_Items> menuItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    MainActivity homeMenuActivity;


    public Home_Menu_Adapter(List<Home_Items> items, MainActivity activity) {
        this.menuItems = items;
        this.homeMenuActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_menu_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Home_Items itemOflist = menuItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView menuImage;
        public TextView title;
        public View cardView;

        Home_Items menuItems = new Home_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            menuImage = (ImageView) itemView.findViewById(R.id.menuImage);
            title = (TextView) itemView.findViewById(R.id.title);
            cardView = itemView;
            cardView.setOnClickListener(this);

        }

        public void bindListDetails(Home_Items menuItems) {
            this.menuItems = menuItems;

            title.setText(menuItems.gettitle());
            menuImage.setImageResource(menuItems.getFirstImagePath());
//            menuImage.setImageDrawable(homeMenuActivity.getResources().getDrawable(menuItems.getFirstImagePath()));

        }

        @Override
        public void onClick(View v) {

            int position = menuItems.getgridId();
            String gridName = menuItems.gettitle();

            if (gridName.equals("Customer List \n (ग्राहक सूची)")) {
                Intent gotoDashboard = new Intent(v.getContext(), Customer_List.class);
                v.getContext().startActivity(gotoDashboard);
            }
            else if (gridName.equals("Product List \n (वस्तुओं की सूची)")) {
                Intent gotoMachineDetails = new Intent(v.getContext(), Product_List.class);
                v.getContext().startActivity(gotoMachineDetails);
            }
            else if (gridName.equals("Add Daily Bill \n (रोज खर्ची)")) {
                Intent gotoOwnAttendance = new Intent(v.getContext(), Bill_Add.class);
                v.getContext().startActivity(gotoOwnAttendance);
            }
            else if (gridName.equals("Daily Bill Report \n (बिल विस्तार)")) {
                Intent gotoSMS = new Intent(v.getContext(), Bill_List_DateWise.class);
                v.getContext().startActivity(gotoSMS);
            }
            else if (gridName.equals("My Income \n (कमाई)")) {
                Intent gotoincome = new Intent(v.getContext(), Monthly_Income.class);
                v.getContext().startActivity(gotoincome);
            }
//            else if (gridName.equals("Add Customer")) {
//                Intent gotoNews = new Intent(v.getContext(), Customer_Add.class);
//                v.getContext().startActivity(gotoNews);
//            }
//            else if (gridName.equals("Add Product")) {
//                Intent gotoNotice = new Intent(v.getContext(), Product_Add.class);
//                v.getContext().startActivity(gotoNotice);
//            }
        }
    }
}
