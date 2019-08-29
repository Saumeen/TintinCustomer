package com.pkg.tintincustomer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    MainActivity mainActivity;
    ArrayList<HomeDataModel> dataModelArrayList;

    public HomeRecyclerViewAdapter(MainActivity mainActivity, ArrayList<HomeDataModel> supplierDatamodelList) {
        this.mainActivity = mainActivity;
        this.dataModelArrayList = supplierDatamodelList;
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(mainActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.home_card,parent,false);
        return new HomeRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, final int position) {
        holder.setSupplierName(dataModelArrayList.get(position).getSuppliername());
        holder.setSupplierAddress(dataModelArrayList.get(position).getSupplierflatno()+", "+ dataModelArrayList.get(position).getSupplierlandmark()
             +", "+ dataModelArrayList.get(position).getSuppliercity());
        try{
            holder.setSupplierImage(null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),position+"",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(),MenuDataListActivity.class);
                intent.putExtra("Name",dataModelArrayList.get(position).getSuppliername());
                intent.putExtra("Address",dataModelArrayList.get(position).getSupplierflatno()+", "+ dataModelArrayList.get(position).getSupplierlandmark()
                        +", "+ dataModelArrayList.get(position).getSuppliercity());
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
       return dataModelArrayList.size();
    }
}
