package com.pkg.tintincustomer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    private View view;
    public HomeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setSupplierName(String name){
        TextView  textView = view.findViewById(R.id.homecard_suppliername);
        textView.setText("Cook Name : "+name);
    }
    public void setMenu(String menu){
        TextView textView = view.findViewById(R.id.homecard_menu);
        textView.setText("Menu : "+menu);
    }
    public void setCost(String cost){
        TextView textView = view.findViewById(R.id.homecard_cost);
        textView.setText("Price : "+cost);
    }
}
