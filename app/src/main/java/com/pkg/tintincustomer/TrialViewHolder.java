package com.pkg.tintincustomer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrialViewHolder extends RecyclerView.ViewHolder {

    private View view;
    public TrialViewHolder(@NonNull View itemView) {
        super(itemView);
        view =itemView;
    }
    public void setName(String Name){
        TextView textView = view.findViewById(R.id.trial_Name);
        textView.setText(Name);
    }
    public void setPhone(String Name){
        TextView textView = view.findViewById(R.id.trial_phoneno);
        textView.setText(Name);
    }public void setCity(String Name){
        TextView textView = view.findViewById(R.id.trial_city);
        textView.setText(Name);
    }
}
