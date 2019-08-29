package com.pkg.tintincustomer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivityViewAdapter extends RecyclerView.Adapter<SearchActivityViewHolder> {
    private ArrayList<SearchDataListModel> dataModelArrayList;
    private View view;
    private String query;

    public SearchActivityViewAdapter(ArrayList<SearchDataListModel> dataModelArrayList,String query) {
        this.dataModelArrayList = dataModelArrayList;
        this.query=query;
    }

    @NonNull
    @Override
    public SearchActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.search_card,parent,false);
        return new SearchActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchActivityViewHolder holder, final int position) {

       holder.setmenu(query);
        holder.setCookname(dataModelArrayList.get(position).getName().toString());
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),MenuDataListActivity.class);
                        intent.putExtra("Name",dataModelArrayList.get(position).getName().toString());
                        v.getContext().startActivity(intent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
}
