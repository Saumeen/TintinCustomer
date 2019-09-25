package com.pkg.tintincustomer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<HomeDataModel> homeDataModelArrayList;
    private RecyclerView recycler_view;
    private FirestoreRecyclerAdapter<HomeDataModel,HomeRecyclerViewHolder> adapter1;

    private Spinner searchspin;
    private TextView navaddress;
    private TextView user_name;
    private TextView user_email;
    private ImageView city_image;
    private View header;

    String[] citydata={"Nadiad","Godhra","Vadodara","Ahmedabad","Gandhinagar","Surat","Anand"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        BottomAppBar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this,TrialRecyclerviewActivity.class);
               Intent intent = new Intent(MainActivity.this,SearchActivity.class);
               startActivity(intent);
            }
        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        searchspin = findViewById(R.id.searchspiner);
        user_email = header.findViewById(R.id.user_email);
        user_name = header.findViewById(R.id.user_name);
        city_image = findViewById(R.id.city_image);
        city_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchspin.performClick();
            }
        });
        authentication();
        layoutauthentication();
        addItemOnSpinner();
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


        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater layoutInflater =LayoutInflater.from(getApplicationContext());
                final View view = layoutInflater.inflate(R.layout.change_name,null);

                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Enter Name Please");
                alertDialog.setCancelable(false);
                final EditText changename = view.findViewById(R.id.changename);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = changename.getText().toString();
                        Toast.makeText(getApplicationContext(),"Name Changed",Toast.LENGTH_LONG).show();
                        changeName(name);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();
            }
        });
    }

    private void changeName(final String name) {
        db.collection("CustomerUsers").whereEqualTo("PhoneNo", firebaseUser.getPhoneNumber()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                    changeNamecustomer(documentSnapshot.getId());
                }
            }

            private void changeNamecustomer(String id) {
                HashMap<String,Object> datamap = new HashMap<>();
                datamap.put("Name",name);
                db.collection("CustomerUsers").document(id).update(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
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
                        user_name.setText(documentSnapshot.getString("Name"));
                        user_email.setText((documentSnapshot.getString("PhoneNo")));
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
                R.layout.spinner_text, citydata);
        dataAdapter.setDropDownViewResource(R.layout.spinner_text);
        searchspin.setAdapter(dataAdapter);
        Toast.makeText(getApplicationContext(),String.valueOf(searchspin.getSelectedItem()),Toast.LENGTH_LONG).show();

    }

    private void layoutauthentication() {

        homeDataModelArrayList = new ArrayList<>();
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

        Query query = db.collection("SupplierUsers").whereEqualTo("City",searchspin.getSelectedItem());
        FirestoreRecyclerOptions<HomeDataModel> options = new FirestoreRecyclerOptions.Builder<HomeDataModel>()
                .setQuery(query, HomeDataModel.class)
                .build();

        adapter1 = new FirestoreRecyclerAdapter<HomeDataModel, HomeRecyclerViewHolder>(options) {
            @NonNull
            @Override
            public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card, parent, false);
                return new HomeRecyclerViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HomeRecyclerViewHolder dataViewHolder, final int i, @NonNull final HomeDataModel data) {
                dataViewHolder.setSupplierName(data.getName());
                dataViewHolder.setSupplierAddress(data.getHouseFlatNo()+","+data.getLandmark()+""+data.getCity());

                dataViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),i+"",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(v.getContext(),MenuDataListActivity.class);
                        intent.putExtra("Name",data.getName());
                        intent.putExtra("Address",data.getHouseFlatNo()+","+data.getLandmark()+""+data.getCity());
                        v.getContext().startActivity(intent);

                    }
                });

            }
        };
        recycler_view.setAdapter(adapter1);
        adapter1.startListening();
    }


    @Override
    protected void onStart() {
        super.onStart();
        fatchMenuData();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(adapter1 != null){
            adapter1.stopListening();
        }
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
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
