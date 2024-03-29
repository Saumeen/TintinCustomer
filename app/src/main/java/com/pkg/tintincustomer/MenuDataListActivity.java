package com.pkg.tintincustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private TiffinMenuDataListViewAdapter adapter;
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

        final ViewPager viewPager =findViewById(R.id.viewpager_menudata);
        final TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(),bundle);
        viewPager.setAdapter(tabsAdapter);

        tabLayout =  findViewById(R.id.tablayout_menudata);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fabcart_menudata);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuDataListActivity.this,CartOrderActivity.class);
                startActivity(intent);
            }
        });
        setCookData(bundle);
    }




//


    private void setCookData(final Bundle bundle) {
        cookname.setText("Cook Name : "+bundle.getString("Name"));
        cookaddress.setText("Cook Address : "+bundle.getString("Address"));
        db.collection("SupplierUsers").whereEqualTo("Name",bundle.getString("Name")).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot qs =task.getResult();
                            List<DocumentSnapshot> list = qs.getDocuments();
                            cookphoneno.setText("Mobile No : "+list.get(0).getString("MobileNo"));
                            bundle.putString("PhoneNo",list.get(0).getString("MobileNo"));
                        }
                    }


    });
    }

}
