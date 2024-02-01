package com.example.shop_app.model;

import android.widget.Filter;

import com.example.shop_app.adapter.OrderUserAdapter;


import java.util.ArrayList;
import java.util.List;

public class FilterOrderUser extends Filter {

    private OrderUserAdapter adapter;
    private ArrayList<Order> filterList;

    public FilterOrderUser(OrderUserAdapter adapter, ArrayList<Order> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if (charSequence != null && charSequence.length() > 0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Order> filterModels = new ArrayList<>();
            for (int i = 0; i<filterList.size(); i++){
               if (filterList.get(i).getOrderStatus().toUpperCase().contains(charSequence)){
                   filterModels.add(filterList.get(i));
               }
            }
            results.count = filterModels.size();
            results.values = filterModels;

        }
        else   {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.orderList = (List<Order>) filterResults.values;
        adapter.notifyDataSetChanged();
    }
}
