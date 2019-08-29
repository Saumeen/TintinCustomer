package com.pkg.tintincustomer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class SearchActivityViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public SearchActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setCookname(String cookname){
        TextView textView =view.findViewById(R.id.searchcard_cookname);
        textView.setText("Cook Name : "+cookname);
    }
    public void setmenu(String menu){
        TextView textView = view.findViewById(R.id.searchcard_menu);
        textView.setText("Menu : "+menu);
    }
}
