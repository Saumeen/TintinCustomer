package com.pkg.tintincustomer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    MainActivity mainActivity;
    ArrayList<MenuDataModel> menuDataModelArrayList;

    public HomeRecyclerViewAdapter(MainActivity mainActivity, ArrayList<MenuDataModel> menuDataModelArrayList) {
        this.mainActivity = mainActivity;
        this.menuDataModelArrayList = menuDataModelArrayList;
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(mainActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.home_card,parent,false);
        return new HomeRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {
        holder.setSupplierName(menuDataModelArrayList.get(position).getSuppliername());
        holder.setMenu(menuDataModelArrayList.get(position).getMenu());
        holder.setCost(menuDataModelArrayList.get(position).getCost());
    }

    @Override
    public int getItemCount() {
       return menuDataModelArrayList.size();
    }
}
