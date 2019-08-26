package com.pkg.tintincustomer;

import android.view.View;
import android.widget.Button;
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

    public void setSupplierAddress(String address){
        TextView textView = view.findViewById(R.id.homecard_supplieraddres);
        textView.setText("Address : "+address);
    }

}
