package com.pkg.tintincustomer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<MenuDataModel> menuDataModelArrayList;
    private RecyclerView recycler_view;
    //FirebaseHomeRecyclerViewAdapter firebaseHomeRecyclerViewAdapter;
    private HomeRecyclerViewAdapter adapter;
    private Spinner searchspin;
    private TextView navaddress;
    String[] citydata={"Nadiad","Godhra","Vadodara","Ahmedabad","Gandhinagar","Surat","Anand"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        searchspin = findViewById(R.id.searchspiner);

        authentication();
        layoutauthentication();
        addItemOnSpinner();
        //fatchMenuData();
        loadNavbarData();

        searchspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),String.valueOf(searchspin.getSelectedItem()),Toast.LENGTH_LONG).show();
                fatchMenuData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Select the City",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadNavbarData() {
        db.collection("CustomerUsers").whereEqualTo("PhoneNo",firebaseUser.getPhoneNumber()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                    getDetailsOfCustomer(documentSnapshot.getId());
                }
            }

            private void getDetailsOfCustomer(String id) {
                db.collection("CustomerUsers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();

                        navaddress = findViewById(R.id.nav_address1);
                        navaddress.setText(String.format(getString(R.string.Change))
                                +documentSnapshot.getString("HomeNo")+","+documentSnapshot.getString("LandMark")+","
                                +documentSnapshot.getString("City")+","
                        +documentSnapshot.getString("State"));
                       navaddress.setPaintFlags(navaddress.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                        navaddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(MainActivity.this,MapsActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addItemOnSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citydata);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchspin.setAdapter(dataAdapter);
        Toast.makeText(getApplicationContext(),String.valueOf(searchspin.getSelectedItem()),Toast.LENGTH_LONG).show();

    }

    private void layoutauthentication() {

        menuDataModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.home_recyclerview);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

    }

    private void authentication() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    private void fatchMenuData() {
        db.collection("SupplierUsers").whereEqualTo("City",searchspin.getSelectedItem()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                Log.d("MainActivity--",task.getResult().getDocuments()+""+firebaseUser.getPhoneNumber());
                for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        loadData(documentSnapshot.getId(),documentSnapshot.getString("Name")

                        );
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadData(String id, final String suppliernamedata) {

//        Query query = db.collection("SupplierUsers").document(id).collection("Menu")
//                .orderBy("Menu");
//        Log.d("MainActivity--", query.get()+"");
//
//        FirestoreRecyclerOptions<MenuDataModel> options = new FirestoreRecyclerOptions.Builder<MenuDataModel>()
//                .setQuery(query,MenuDataModel.class).build();
//        Log.d("MainActivity--",options+"");
//        Log.d("MainActivity--",options.getSnapshots() +"");
//        firebaseHomeRecyclerViewAdapter = new FirebaseHomeRecyclerViewAdapter(options);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recycler_view = findViewById(R.id.home_recyclerview);
//        recycler_view.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.setReverseLayout(false);
//        linearLayoutManager.setSmoothScrollbarEnabled(true);
//        recycler_view.setAdapter(firebaseHomeRecyclerViewAdapter);
//        firebaseHomeRecyclerViewAdapter.startListening();


//                menuDataModelArrayList.clear();
//                db.collection("SupplierUsers").document(id).collection("Menu")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//
//                        // adapter=null;
//                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
//                            MenuDataModel menuDataModel =new MenuDataModel(suppliernamedata,
//                                        documentSnapshot.getString("Menu"),
//                                        documentSnapshot.getString("Cost"));
//                            menuDataModelArrayList.add(menuDataModel);
//                            Log.d("Log---", documentSnapshot.getString("Menu") + "");
//                        }
//                        adapter = new HomeRecyclerViewAdapter(MainActivity.this, menuDataModelArrayList);
//                        adapter.notifyDataSetChanged();
//                        recycler_view.setAdapter(adapter);
//                    }
//                });





        menuDataModelArrayList.clear();
        db.collection("SupplierUsers").document(id).collection("Menu").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                                MenuDataModel menuDataModel =new MenuDataModel(suppliernamedata,
                                        documentSnapshot.getString("Menu"),
                                        documentSnapshot.getString("Cost"));

                            menuDataModelArrayList.add(menuDataModel);
                            Log.d("Log---", documentSnapshot.getString("Menu") + "");
                        }
                        adapter = new HomeRecyclerViewAdapter(MainActivity.this, menuDataModelArrayList);
                        adapter.notifyDataSetChanged();
                        recycler_view.setAdapter(adapter);                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        //firebaseHomeRecyclerViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(MainActivity.this,SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Are you sure ?").setTitle("Logout");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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
}
