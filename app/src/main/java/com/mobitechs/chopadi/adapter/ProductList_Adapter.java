package com.mobitechs.chopadi.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitechs.chopadi.Product_List;
import com.mobitechs.chopadi.R;
import com.mobitechs.chopadi.model.Product_Items;

import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnViewInflateListener;

public class ProductList_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    List<Product_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    RecyclerView recyclerView;

    private FancyShowCaseQueue fancyShowCaseQueue;
    SharedPreferences wmbPreference;

    FloatingActionButton btnFab;

    public ProductList_Adapter(Product_List product_list, List<Product_Items> listItems, RecyclerView recyclerView, Activity activity,FloatingActionButton btnFab) {
        this.btnFab = btnFab;
        this.activity = activity;
        this.listItems = listItems;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_items, viewGroup, false);
        viewHolder = new ProductList_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ProductList_Adapter.ViewHolder) {
            ProductList_Adapter.ViewHolder vHolder = (ProductList_Adapter.ViewHolder) viewHolder;
            Product_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title, price;
        public View cardView;

        Product_Items listItems = new Product_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);

            cardView = itemView;

            wmbPreference = PreferenceManager.getDefaultSharedPreferences(activity);
            boolean productEditDeleteShowCase = wmbPreference.getBoolean("productEditDeleteShowCase", true);

            if (productEditDeleteShowCase) {
                final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(activity)
                        .title("Click here to add product. \n\n\n  प्रोडक्ट ऐड करने के लिए यहाँ क्लिक करे.")
                        .focusOn(btnFab)
                        .build();

                final FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(activity)
                        .title("Swipe left to Edit. \n\n\n  एडिट करने के लिए बाये ओर सरकाये.")
                        .build();
                final FancyShowCaseView fancyShowCaseView3 = new FancyShowCaseView.Builder(activity)
                        .title("Swipe right to delete. \n\n\n  डिलीट करने के लिए दाहिने ओर सरकाये.")
                        .build();

                fancyShowCaseQueue = new FancyShowCaseQueue()
                        //.add(fancyShowCaseView1)
                        .add(fancyShowCaseView2)
                        .add(fancyShowCaseView3);

                fancyShowCaseQueue.show();
            }

            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("productEditDeleteShowCase", false);
            editor.commit();

        }

        public void bindListDetails(final Product_Items listItems) {
            this.listItems = listItems;
            title.setText(listItems.getproductName());
            price.setText("Rs. " + listItems.getprice());
        }

        @Override
        public void onClick(final View v) {

        }
    }
}