package com.pkg.tintincustomer;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeMenuDataListHolder extends RecyclerView.ViewHolder {
    private View view;
    public Button comingdata;

    public HomeMenuDataListHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
       comingdata = view.findViewById(R.id.homemenudatacard_coming);
    }
    public void setType(String type){
        TextView textView = view.findViewById(R.id.homemenudatacard_type);
        textView.setText("Type :"+ type);
    }
    public void setMenu(String menu){
        TextView textView = view.findViewById(R.id.homemenudatacard_menu);
        textView.setText("Menu : "+menu);
    }
    public void setCost(String cost){
        TextView textView = view.findViewById(R.id.homemenudatacard_cost);
        textView.setText("Cost : "+cost);
    }
}
