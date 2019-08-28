package com.pkg.tintincustomer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivityViewAdapter extends RecyclerView.Adapter<SearchActivityViewHolder> {
    private ArrayList<SearchDataListModel> dataModelArrayList;
    private View view;

    public SearchActivityViewAdapter(ArrayList<SearchDataListModel> dataModelArrayList) {
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public SearchActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.search_card,parent,false);
        return new SearchActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchActivityViewHolder holder, int position) {

       // holder.setmenu(dataModelArrayList.get(position).getDocumentReference());
        holder.setCookname(dataModelArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
}
