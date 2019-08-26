package com.pkg.tintincustomer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartOrderAdapter extends RecyclerView.Adapter<MenuDataListViewHolder> {
    private CartOrderActivity cartOrderActivity;
    private ArrayList<MenuDataModel> dataModelArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private Map<String,Object> datamap;

    public CartOrderAdapter(CartOrderActivity cartOrderActivity, ArrayList<MenuDataModel> dataModelArrayList) {
        this.cartOrderActivity = cartOrderActivity;
        this.dataModelArrayList = dataModelArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        datamap = new HashMap<>();
    }

    @NonNull
    @Override
    public MenuDataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(cartOrderActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.cart_order,parent,false);
        return new MenuDataListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDataListViewHolder holder, final int position) {
        holder.setType(dataModelArrayList.get(position).getType());
        holder.setMenu(dataModelArrayList.get(position).getMenu());
        holder.setCost(dataModelArrayList.get(position).getCost());
        holder.removecartdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeToCartData(v,position);
//
//                Intent intent = new Intent(v.getContext(),CartOrderActivity.class);
//                intent.putExtra("MenuData",dataModelArrayList);
//                intent.putExtra("Position",position);
//                v.getContext().startActivity(intent);
            }
        });
    }

    private void removeToCartData(final View v, final int position) {
        db.collection("CustomerUsers").whereEqualTo("PhoneNo", firebaseUser.getPhoneNumber()).get()
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

                        final CollectionReference collectionReference = db.collection("CustomerUsers").document(id).collection("OrderCart");

                        Query query = collectionReference.whereEqualTo("Menu", dataModelArrayList.get(position).getMenu())
                                .whereEqualTo("cost", dataModelArrayList.get(position).getCost());
                        Log.d("In--", dataModelArrayList.get(position).getMenu());
                        query.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("In--", "ONcomplate"+task.getResult().getDocuments());
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                Log.d("In--", "Before delete");
                                                collectionReference.document(documentSnapshot.getId()).delete();
                                                dataModelArrayList.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, dataModelArrayList.size());
                                                Toast.makeText(v.getContext(), "Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Failer----", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
}
