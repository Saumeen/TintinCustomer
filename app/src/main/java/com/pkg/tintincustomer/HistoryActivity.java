package com.pkg.tintincustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private RecyclerView recycler_view;
    private FirestoreRecyclerAdapter<MenuDataModel,MenuDataListViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        layoutAuth();
        fatchCart();

    }
    private void layoutAuth() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.recyclerview_history);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
    }

    private void fatchCart(){
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
        final Query query  = db.collection("CustomerUsers").document(id).collection("History");
        FirestoreRecyclerOptions<MenuDataModel> options = new FirestoreRecyclerOptions.Builder<MenuDataModel>()
                .setQuery(query, MenuDataModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<MenuDataModel, MenuDataListViewHolder>(options) {
            @NonNull
            @Override
            public MenuDataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
                return new MenuDataListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuDataListViewHolder dataViewHolder, final int i, @NonNull final MenuDataModel data) {
                dataViewHolder.setMenu(data.getMenu());
                dataViewHolder.setCost(data.getCost());
                dataViewHolder.setType(data.getType());
            }
        };
        recycler_view.setAdapter(adapter);
        adapter.startListening();

    }


}
