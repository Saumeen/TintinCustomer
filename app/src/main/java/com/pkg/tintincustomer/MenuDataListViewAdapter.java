package com.pkg.tintincustomer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuDataListViewAdapter extends RecyclerView.Adapter<MenuDataListViewHolder> {

    private ArrayList<MenuDataModel> dataModelArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private Map<String,Object> datamap;

    public MenuDataListViewAdapter(ArrayList<MenuDataModel> dataModelArrayList) {

        this.dataModelArrayList = dataModelArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        datamap = new HashMap<>();
    }

    @NonNull
    @Override
    public MenuDataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.menudata_card,parent,false);
        return new MenuDataListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDataListViewHolder holder, final int position) {
        holder.setType(dataModelArrayList.get(position).getType());
        holder.setMenu(dataModelArrayList.get(position).getMenu());
        holder.setCost(dataModelArrayList.get(position).getCost());
        holder.addmenudata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartData(v,position);
//
//                Intent intent = new Intent(v.getContext(),CartOrderActivity.class);
//                intent.putExtra("MenuData",dataModelArrayList);
//                intent.putExtra("Position",position);
//                v.getContext().startActivity(intent);
            }
        });
    }

    private void addToCartData(final View v, final int position) {
        db.collection("CustomerUsers").whereEqualTo("PhoneNo",firebaseUser.getPhoneNumber()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot qs = task.getResult();
                            List<DocumentSnapshot> list = qs.getDocuments();
                            addData(list.get(0).getId());
                        }
                    }

                    private void addData(String id) {
                        datamap.put("Menu",dataModelArrayList.get(position).getMenu());
                        datamap.put("Type",dataModelArrayList.get(position).getType());
                        datamap.put("cost",dataModelArrayList.get(position).getCost());
                        db.collection("CustomerUsers").document(id).collection("OrderCart").add(datamap).addOnCompleteListener(
                                new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(v.getContext(),"Added Successfully",Toast.LENGTH_LONG).show();
                                    }
                                }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(),"Something Wrong in Add",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(v.getContext(),"Something Wrong in Add",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    }
