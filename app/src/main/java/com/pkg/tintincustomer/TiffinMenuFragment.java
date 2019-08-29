package com.pkg.tintincustomer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class TiffinMenuFragment  extends Fragment {
    public RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private ArrayList<MenuDataModel> dataModelArrayList;
    private TiffinMenuDataListViewAdapter adapter;
    private Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View v =  inflater.inflate(R.layout.fragment_tiffin_menu, viewGroup, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db= FirebaseFirestore.getInstance();
        dataModelArrayList = new ArrayList<>();
        recyclerView =v.findViewById(R.id.menudata_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        bundle = getArguments();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG--",bundle.getString("Name"));
        db.collection("SupplierUsers").whereEqualTo("Name",bundle.getString("Name")).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot qs =task.getResult();
                            List<DocumentSnapshot> list = qs.getDocuments();
                            fatchMiniTiffinData(list.get(0).getId());
                            fatchFullTiffindata(list.get(0).getId());
                        }
                    }
                    private void fatchFullTiffindata(String id) {
                        dataModelArrayList.clear();
                        db.collection("SupplierUsers").document(id).collection("Menu").whereEqualTo("Type","Full Tiffin").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){

                                            MenuDataModel menuDataModel = new MenuDataModel(documentSnapshot.getString("Type"),
                                                    documentSnapshot.getString("Menu"),documentSnapshot.getString("Cost"));

                                            dataModelArrayList.add(menuDataModel);

                                        }
                                        adapter = new TiffinMenuDataListViewAdapter( dataModelArrayList);
                                        recyclerView.setAdapter(adapter);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    private void fatchMiniTiffinData(String id) {
                        dataModelArrayList.clear();
                        db.collection("SupplierUsers").document(id).collection("Menu").whereEqualTo("Type","Mini Tiffin").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                            MenuDataModel menuDataModel = new MenuDataModel(documentSnapshot.getString("Type"),
                                                    documentSnapshot.getString("Menu"),documentSnapshot.getString("Cost"));
                                            dataModelArrayList.add(menuDataModel);
                                        }
                                        adapter = new TiffinMenuDataListViewAdapter(dataModelArrayList);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();
            }
        });
    }
}