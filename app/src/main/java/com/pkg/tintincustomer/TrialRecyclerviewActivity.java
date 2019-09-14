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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Collection;

public class TrialRecyclerviewActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<TrialDataModel,TrialViewHolder> adapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_recyclerview);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.trial_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CollectionReference collectionReference = db.collection("CustomerUsers");
        query = collectionReference.orderBy("Name",Query.Direction.ASCENDING);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirestoreRecyclerOptions<TrialDataModel> options = new FirestoreRecyclerOptions.Builder<TrialDataModel>()
                .setQuery(query, TrialDataModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<TrialDataModel, TrialViewHolder>(options) {
            @NonNull
            @Override
            public TrialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trial_cart, parent, false);
                return new TrialViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TrialViewHolder dataViewHolder, int i, @NonNull TrialDataModel data) {
                dataViewHolder.setName(data.getName());
                dataViewHolder.setPhone(data.getPhoneNo());
                dataViewHolder.setCity(data.getCity());
            }


        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }
}
