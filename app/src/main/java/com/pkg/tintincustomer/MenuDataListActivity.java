package com.pkg.tintincustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuDataListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private TextView cookname,cookaddress,cookphoneno;
    private RecyclerView recycler_View;
    private Bundle bundle;
    private Intent intent;
    private RecyclerView recycler_view;
    private ArrayList<MenuDataModel> dataModelArrayList;
    private MenuDataListViewAdapter adapter;
    private Button cartdata;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_data_list);
        cookaddress=findViewById(R.id.menudata_address);
        cookname = findViewById(R.id.menudata_suppliername);
        cookphoneno = findViewById(R.id.menudata_phoneno);
        recycler_View = findViewById(R.id.menudata_recyclerview);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        bundle = intent.getExtras();
        BottomAppBar toolbar = findViewById(R.id.toolbar_menudata);
        setSupportActionBar(toolbar);
//        setRecyclerLayout();



        final ViewPager viewPager =findViewById(R.id.viewpager_menudata);
        final TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(),bundle);
        viewPager.setAdapter(tabsAdapter);

        tabLayout =  findViewById(R.id.tablayout_menudata);
        tabLayout.setupWithViewPager(viewPager);


        //        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                Toast.makeText(getApplicationContext(),tab.getPosition()+"",Toast.LENGTH_LONG).show();
//                if(tab.getPosition()==0){
//                    setTiffinViewData(bundle);
//                }
//                if(tab.getPosition() == 1) {
//                    setHomeViewData(bundle);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                recycler_View.setAdapter(null);
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });





        FloatingActionButton fab = findViewById(R.id.fabcart_menudata);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuDataListActivity.this,CartOrderActivity.class);
                startActivity(intent);
            }
        });






    }




    private void setRecyclerLayout() {

        dataModelArrayList = new ArrayList<>();
        recycler_view = findViewById(R.id.menudata_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

    }


    private void setTiffinViewData(Bundle bundle) {
        cookname.setText("Cook Name : "+bundle.getString("Name"));
        cookaddress.setText("Cook Address : "+bundle.getString("Address"));
        db.collection("SupplierUsers").whereEqualTo("Name",bundle.getString("Name")).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot qs =task.getResult();
                            List<DocumentSnapshot> list = qs.getDocuments();
                            fatchMiniTiffinData(list.get(0).getId());
                            fatchFullTiffindata(list.get(0).getId());
                            cookphoneno.setText("Mobile No : "+list.get(0).getString("MobileNo"));
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
                                        adapter = new MenuDataListViewAdapter( dataModelArrayList);
                                        recycler_view.setAdapter(adapter);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();
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
                                        adapter = new MenuDataListViewAdapter( dataModelArrayList);
                                        recycler_view.setAdapter(adapter);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setHomeViewData(Bundle bundle){
        cookname.setText("Cook Name : "+bundle.getString("Name"));
        cookaddress.setText("Cook Address : "+bundle.getString("Address"));
        db.collection("SupplierUsers").whereEqualTo("Name",bundle.getString("Name")).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot qs =task.getResult();
                            List<DocumentSnapshot> list = qs.getDocuments();
                            fatchLunchData(list.get(0).getId());
                            fatchDinnerData(list.get(0).getId());
                            cookphoneno.setText("Mobile No : "+list.get(0).getString("MobileNo"));
                        }
                    }

                    private void fatchLunchData(String id) {
                        dataModelArrayList.clear();
                        db.collection("SupplierUsers").document(id).collection("Menu").whereEqualTo("Type","Lunch").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){

                                            MenuDataModel menuDataModel = new MenuDataModel(documentSnapshot.getString("Type"),
                                                    documentSnapshot.getString("Menu"),documentSnapshot.getString("Cost"));

                                            dataModelArrayList.add(menuDataModel);

                                        }
                                        adapter = new MenuDataListViewAdapter( dataModelArrayList);
                                        recycler_view.setAdapter(adapter);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();
                            }
                        });


                    }




                    private void fatchDinnerData(String id) {
                        dataModelArrayList.clear();
                        db.collection("SupplierUsers").document(id).collection("Menu").whereEqualTo("Type","Dinner").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){

                                            MenuDataModel menuDataModel = new MenuDataModel(documentSnapshot.getString("Type"),
                                                    documentSnapshot.getString("Menu"),documentSnapshot.getString("Cost"));

                                            dataModelArrayList.add(menuDataModel);

                                        }
                                        adapter = new MenuDataListViewAdapter( dataModelArrayList);
                                        recycler_view.setAdapter(adapter);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Wrong in menudatalist activity",Toast.LENGTH_LONG).show();

            }
        });
    }
    }

