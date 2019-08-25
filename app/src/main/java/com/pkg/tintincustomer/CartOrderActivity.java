package com.pkg.tintincustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartOrderActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private RecyclerView recycler_view;
    private ArrayList<MenuDataModel> menuDataArrayList;
    private CartOrderAdapter adapter;
    private BottomAppBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layoutAuth();
        fatchCart();
    }

    private void fatchCart() {
        db.collection("CustomerUsers").whereEqualTo("PhoneNo",firebaseUser.getPhoneNumber())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot qs = task.getResult();
                    List<DocumentSnapshot> list = qs.getDocuments();
                    loadCartData(list.get(0).getId());
                }
            }
        });
    }

    private void loadCartData(String id) {
        final Query query  = db.collection("CustomerUsers").document(id).collection("OrderCart");
         query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            MenuDataModel menuData = new MenuDataModel(  documentSnapshot.getString("Type"),documentSnapshot.getString("Menu"),
                                    documentSnapshot.getString("cost"));
                            menuDataArrayList.add(menuData);
                            adapter = new CartOrderAdapter(CartOrderActivity.this, menuDataArrayList);
                            adapter.notifyDataSetChanged();
                            recycler_view.setAdapter(adapter);
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fail to Load", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void layoutAuth() {
        menuDataArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.recyclerview_cartorder);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
    }
}
