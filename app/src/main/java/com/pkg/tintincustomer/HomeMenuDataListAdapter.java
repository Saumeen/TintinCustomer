package com.pkg.tintincustomer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class HomeMenuDataListAdapter extends RecyclerView.Adapter<HomeMenuDataListHolder> {

    private ArrayList<MenuDataModel> dataModelArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private Map<String,Object> datamap,datamap2;
    private Bundle bundle;
    private View view;
    private String custphoneno,custname;
    boolean flag=false;

    public HomeMenuDataListAdapter(ArrayList<MenuDataModel> dataModelArrayList, Bundle bundle) {
        this.bundle=bundle;
        this.dataModelArrayList = dataModelArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        datamap = new HashMap<>();
        datamap2 = new HashMap<>();

    }

    @NonNull
    @Override
    public HomeMenuDataListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.homemenudata_cart,parent,false);
        return new HomeMenuDataListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeMenuDataListHolder holder, final int position) {

        holder.setType(dataModelArrayList.get(position).getType());
        holder.setMenu(dataModelArrayList.get(position).getMenu());
        holder.setCost(dataModelArrayList.get(position).getCost());
        holder.comingdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"coming",Toast.LENGTH_LONG).show();
                confirmation(v,position);



            }
        });
        if(bundle.getBoolean("flag")){
            holder.comingdata.setEnabled(false);
        }
        else
        {
            holder.comingdata.setEnabled(true);
        }

    }

    private void setIsOrderd() {
        db.collection("CustomerUsers").whereEqualTo("PhoneNo",firebaseUser.getPhoneNumber()).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot qs = task.getResult();
                        List<DocumentSnapshot> list = qs.getDocuments();
                        addOrder(list.get(0).getId());
                    }
                }
        );
    }

    private void confirmation(final View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure ?").setTitle("Coming");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                supplierHomeMenuRequest(position);
                flag = true;
                setIsOrderd();
                Intent intent =new Intent(v.getContext(),MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void supplierHomeMenuRequest(final int position) {
        db.collection("SupplierUsers").whereEqualTo("Name",bundle.getString("Name")).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot qs = task.getResult();
                        List<DocumentSnapshot> list = qs.getDocuments();
                        addRequest(list.get(0).getId(),position);
                    }
                }
        );
    }

    private void addOrder(String id) {
        datamap2.put("isOrdered",flag);
        db.collection("CustomerUsers").document(id).update(datamap2).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(view.getContext(),"flag set : "+flag,Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void addRequest(final String supid, final int position) {

        db.collection("CustomerUsers").whereEqualTo("PhoneNo",firebaseUser.getPhoneNumber()).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot qs = task.getResult();
                        List<DocumentSnapshot> ls = qs.getDocuments();
                        fatchname(ls.get(0).getId());
                    }

                    private void fatchname(String id) {
                        db.collection("CustomerUsers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot ds = task.getResult();
                            Log.d("In---", ds.getString("Name"));
                                assignNAme(ds.getString("Name"));
                            }
                        });
                    }
                    private void assignNAme(String name) {
                        custname = name;
                        custphoneno = firebaseUser.getPhoneNumber();
                        String menudata=dataModelArrayList.get(position).getMenu();
                        String typedata=dataModelArrayList.get(position).getType();
                        String costdata=dataModelArrayList.get(position).getCost();
                        datamap.put("Menu",menudata);
                        datamap.put("Type",typedata);
                        datamap.put("Cost",costdata);
                        datamap.put("CustName", custname);
                        datamap.put("CustPhoneNo",custphoneno);
                        db.collection("SupplierUsers").document(supid).collection("RequestOrder").add(datamap).addOnCompleteListener(
                                new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(view.getContext(),"Request Succcesfully Placed",Toast.LENGTH_LONG).show();
                                    }
                                }
                        );

                    }

                }
        );





        //




    }


    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

}

