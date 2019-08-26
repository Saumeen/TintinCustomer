package com.pkg.tintincustomer;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuDataListViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public Button addmenudata,removecartdata;


    public MenuDataListViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        addmenudata = view.findViewById(R.id.menudatacard_add);
        removecartdata =view.findViewById(R.id.menudatacard_remove);

    }

    public void setType(String type){
        TextView textView = view.findViewById(R.id.menudatacard_type);
        textView.setText("Type :"+ type);
    }
    public void setMenu(String menu){
        TextView textView = view.findViewById(R.id.menudatacard_menu);
        textView.setText("Menu : "+menu);
    }
    public void setCost(String cost){
        TextView textView = view.findViewById(R.id.menudatacard_cost);
        textView.setText("Cost : "+cost);
    }
}
