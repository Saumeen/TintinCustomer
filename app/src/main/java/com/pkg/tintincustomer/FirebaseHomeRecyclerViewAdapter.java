package com.pkg.tintincustomer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FirebaseHomeRecyclerViewAdapter extends FirestoreRecyclerAdapter<MenuDataModel,HomeRecyclerViewHolder> {


    public FirebaseHomeRecyclerViewAdapter(FirestoreRecyclerOptions<MenuDataModel> options){
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int i, @NonNull MenuDataModel menuDataModel) {
        holder.setSupplierName(menuDataModel.getSuppliername());
        holder.setMenu(menuDataModel.getMenu());
        holder.setCost(menuDataModel.getCost());
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card, parent,
                false);
        return new HomeRecyclerViewHolder(view);
    }
}
