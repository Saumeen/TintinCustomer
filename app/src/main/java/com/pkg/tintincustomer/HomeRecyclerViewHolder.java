package com.pkg.tintincustomer;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    private View view;

    public HomeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;

    }

    public void setSupplierName(String name){
        TextView  textView = view.findViewById(R.id.homecard_suppliername);
        textView.setText(name);
    }

    public void setSupplierAddress(String address){
        TextView textView = view.findViewById(R.id.homecard_supplieraddres);
        textView.setText(address);
    }
    public void setSupplierImage(URL url)
    {
        ImageView iv = view.findViewById(R.id.cookImage);
    }

}
