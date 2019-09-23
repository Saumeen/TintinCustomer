package com.pkg.tintincustomer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeMenuFragment extends Fragment {
    public RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private ArrayList<MenuDataModel> dataModelArrayList;
    private HomeMenuDataListAdapter adapter;
    private FirestoreRecyclerAdapter<MenuDataModel,HomeMenuDataListHolder> adapter1,adapter2;
    private Bundle bundle;
    private HomeMenuDataListHolder homeMenuDataListHolder;
    private Button coming;
    private Map<String,Object> datamap,datamap2;
    private String custphoneno,custname;
    boolean flag=false;
    boolean b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.fragment_home_menu, viewGroup, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db= FirebaseFirestore.getInstance();
        datamap = new HashMap<>();
        datamap2 = new HashMap<>();

        dataModelArrayList = new ArrayList<>();
        recyclerView =v.findViewById(R.id.menudatahome_recyclerview);
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
        fatch();
    }

    private void fatch() {

        Log.d("PTINT--",b+"");


        db.collection("SupplierUsers").whereEqualTo("Name",bundle.getString("Name")).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot qs = task.getResult();
                            List<DocumentSnapshot> list = qs.getDocuments();

                            fatchData(list.get(0).getId());

                        }
                    }

                    private void fatchData(String id) {
                        Query query = db.collection("SupplierUsers").document(id).collection("Menu").whereEqualTo("Type1","Home");
                        FirestoreRecyclerOptions<MenuDataModel> options = new FirestoreRecyclerOptions.Builder<MenuDataModel>()
                                .setQuery(query, MenuDataModel.class)
                                .build();
                        adapter1 = new FirestoreRecyclerAdapter<MenuDataModel,HomeMenuDataListHolder>(options) {
                            @NonNull
                            @Override
                            public HomeMenuDataListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homemenudata_cart, parent, false);
                                return new HomeMenuDataListHolder(view);
                            }

                            @Override
                            protected void onBindViewHolder(@NonNull HomeMenuDataListHolder dataViewHolder, int i, @NonNull MenuDataModel data) {
                                    dataViewHolder.setType(data.getType());
                                    dataViewHolder.setMenu(data.getMenu());
                                    dataViewHolder.setCost(data.getCost());
                                    coimingdata(dataViewHolder, i, data);
                                    isOrderChecked(dataViewHolder);
                            }
                        };
                        recyclerView.setAdapter(adapter1);
                        adapter1.startListening();

                    }


                    private void coimingdata(HomeMenuDataListHolder dataViewHolder, final int position, final MenuDataModel data) {
                        dataViewHolder.comingdata.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "coming", Toast.LENGTH_LONG).show();
                                confirmation(v, position,data);
                            }
                        });
                    }
                });


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

    private void confirmation(final View v, final int position, final MenuDataModel data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure ?").setTitle("Coming");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                supplierHomeMenuRequest(position,data);
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

    private void supplierHomeMenuRequest(final int position, final MenuDataModel data) {
        db.collection("SupplierUsers").whereEqualTo("Name",bundle.getString("Name")).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot qs = task.getResult();
                        List<DocumentSnapshot> list = qs.getDocuments();
                        addRequest(list.get(0).getId(),position,data);
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
                        Toast.makeText(getContext(),"flag set : "+flag,Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void addRequest(final String supid, final int position, final MenuDataModel data) {

        db.collection("CustomerUsers").whereEqualTo("PhoneNo", firebaseUser.getPhoneNumber()).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot qs = task.getResult();
                        List<DocumentSnapshot> ls = qs.getDocuments();
                        fatchname(ls.get(0).getId(),data);
                    }

                    private void fatchname(final String id, final MenuDataModel data) {
                        db.collection("CustomerUsers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot ds = task.getResult();
                                Log.d("In---", ds.getString("Name"));
                                assignNAme(ds.getString("Name"),data,id);
                            }
                        });
                    }

                    private void assignNAme(String name,MenuDataModel data,String Cusid) {
                        custname = name;
                        custphoneno = firebaseUser.getPhoneNumber();
                        String menudata = data.getMenu();
                        String typedata = data.getType();
                        String costdata = data.getCost();
                        datamap.put("Menu", menudata);
                        datamap.put("Type", typedata);
                        datamap.put("Cost", costdata);
                        datamap.put("CustName", custname);
                        datamap.put("CustPhoneNo", custphoneno);
                        db.collection("SupplierUsers").document(supid).collection("RequestOrder").add(datamap).addOnCompleteListener(
                                new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(getContext(), "Request Succcesfully Placed", Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                        db.collection("CustomerUsers").document(Cusid).collection("History").add(datamap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                            }
                        });

                    }

                }
        );

    }




        @Override
    public void onStart() {
        super.onStart();
        fatch();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(adapter1 != null && adapter2!=null){
            adapter1.stopListening();
            adapter2.stopListening();
        }
    }

    private void isOrderChecked(final HomeMenuDataListHolder holder) {

        db.collection("CustomerUsers").whereEqualTo("PhoneNo", firebaseUser.getPhoneNumber()).get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                QuerySnapshot qs = task.getResult();
                                List<DocumentSnapshot> list = qs.getDocuments();
                                isordercheck(list.get(0).getId());
                            }

                            private void isordercheck(String id) {
                                db.collection("CustomerUsers").document(id).get().addOnCompleteListener(
                                        new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                b = documentSnapshot.getBoolean("isOrdered");
                                                Log.d("PTINT--order", b + "");
                                                if (b) {
                                                    holder.comingdata.setEnabled(false);
                                                } else {
                                                    holder.comingdata.setEnabled(true);
                                                }
                                            }
                                        }
                                );
                            }
                        }
                );

    }


}
