//package com.pkg.tintincustomer;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class FinalBillActivity extends AppCompatActivity {
//    private FirebaseAuth firebaseAuth;
//    private FirebaseUser firebaseUser;
//    private FirebaseFirestore db;
//    private Button cod,cancel;
//    private RecyclerView recycler_view;
//    private FirestoreRecyclerAdapter<MenuDataModel,MenuDataListViewHolder> adapter;
//    private int Total=0;
//    private HashMap<String,Object> datamap = new HashMap<>();
//    private  String menu;
//    private String type,cost;
//    private Bundle bundle;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_final_bill);
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//        db = FirebaseFirestore.getInstance();
//        cod = findViewById(R.id.cashondilivery_button);
//        cancel = findViewById(R.id.cancel_button);
//        layoutAuth();
//        fatchCart();
//        cod.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                makeOrder();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FinalBillActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void makeOrder() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(FinalBillActivity.this);
//        // 2. Chain together various setter methods to set the dialog characteristics
//        builder.setMessage("Your Total Is "+Total).setTitle("Place Order");
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                placeorder();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//    }
//
//    private void placeorder() {
//        db.collection("CustomerUsers").whereEqualTo("PhoneNo",firebaseUser.getPhoneNumber())
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    QuerySnapshot qs = task.getResult();
//                    List<DocumentSnapshot> list = qs.getDocuments();
//                    placeorderdata(list.get(0).getId());
//                }
//            }
//        });
//    }
//
//    private void placeorderdata(final String id) {
//    db.collection("CustomerUsers").document(id).collection("OrderCart").get().addOnCompleteListener(
//            new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    QuerySnapshot qs = task.getResult();
//                    List<DocumentSnapshot> ds= qs.getDocuments();
//                    addOrderToDatabase(id,ds.get(0).getId());
//                }
//            }
//    );
//
//    }
//
//    private void addOrderToDatabase(final String cusid, String id) {
//        db.collection("CustomerUsers").document(cusid).collection("OrderCart").document(id).get().addOnCompleteListener(
//                new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        DocumentSnapshot ds = task.getResult();
//                        menu = ds.getString("Menu");
//                        type = ds.getString("Type");
//                        cost = ds.getString("cost");
//                        datamap.put("Menu",menu);
//                        datamap.put("Type",type);
//                        datamap.put("cost",cost);
//                        db.collection("CustomerUsers").document(cusid).collection("History").add(datamap).addOnCompleteListener(
//                                new OnCompleteListener<DocumentReference>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                                    }
//                                }
//                        );
//                    }
//                }
//        );
//
//    }
//
//    private void fatchCart(){
//        db.collection("CustomerUsers").whereEqualTo("PhoneNo",firebaseUser.getPhoneNumber())
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    QuerySnapshot qs = task.getResult();
//                    List<DocumentSnapshot> list = qs.getDocuments();
//                    loadCartData(list.get(0).getId());
//                }
//            }
//        });
//    }
//
//    private void loadCartData(String id) {
//        final Query query  = db.collection("CustomerUsers").document(id).collection("OrderCart");
//        FirestoreRecyclerOptions<MenuDataModel> options = new FirestoreRecyclerOptions.Builder<MenuDataModel>()
//                .setQuery(query, MenuDataModel.class)
//                .build();
//        adapter = new FirestoreRecyclerAdapter<MenuDataModel, MenuDataListViewHolder>(options) {
//            @NonNull
//            @Override
//            public MenuDataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
//                return new MenuDataListViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull MenuDataListViewHolder dataViewHolder, final int i, @NonNull final MenuDataModel data) {
//                dataViewHolder.setMenu(data.getMenu());
//                dataViewHolder.setCost(data.getCost());
//                dataViewHolder.setType(data.getType());
//            }
//        };
//
//        recycler_view.setAdapter(adapter);
//        adapter.startListening();
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                QuerySnapshot qs = task.getResult();
//                List<DocumentSnapshot> ds = qs.getDocuments();
//                for (DocumentSnapshot x:ds){
//                    Total = Total + Integer.parseInt(x.get("cost").toString());
//                }
//            }
//        });
//
//
//    }
//    private void layoutAuth() {
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recycler_view = findViewById(R.id.recyclerview_finalbill);
//        recycler_view.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.setReverseLayout(false);
//        linearLayoutManager.setSmoothScrollbarEnabled(true);
//    }
//}
