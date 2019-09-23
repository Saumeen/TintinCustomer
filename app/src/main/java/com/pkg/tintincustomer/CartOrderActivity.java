package com.pkg.tintincustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**Here is some problem in it ,because i used bundle to take name from intent,and its not feasible*/

public class CartOrderActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private RecyclerView recycler_view;
    private ArrayList<MenuDataModel> menuDataArrayList;
    private FirestoreRecyclerAdapter<MenuDataModel,MenuDataListViewHolder> adapter;
    private BottomAppBar toolbar;
    private TextView Totaltextview;
    private int Total=0;
    private Button placeorder;
    private Bundle bundle;
    private HashMap<String,Object> datamap,datamap2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar_cart);
        Totaltextview = findViewById(R.id.Totalbill_cartorder);
        placeorder = findViewById(R.id.placeorder_button);
        setSupportActionBar(toolbar);
        bundle = getIntent().getExtras();
        Log.d("Bundle",bundle.getString("Name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        datamap = new HashMap<>();
        datamap2 = new HashMap<>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layoutAuth();
        fatchCart();
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOrder();
            }
        });
    }
    private void makeOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartOrderActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Your Total Is "+Total).setTitle("Place Order");
        builder.setPositiveButton("COD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              placeorder();
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

    private void placeorder() {
        db.collection("SupplierUsers").whereEqualTo("Name",bundle.getString("Name")).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot qs= task.getResult();
                        List<DocumentSnapshot> ds = qs.getDocuments();
                        setMenudata(ds.get(0).getId());

                    }
                }
        );

    }

    private void setMenudata(final String sid) {
        db.collection("CustomerUsers").whereEqualTo("PhoneNo",firebaseUser.getPhoneNumber())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot qs = task.getResult();
                    List<DocumentSnapshot> list = qs.getDocuments();
                    setInMap(list.get(0).getId());
                }

            }


                private void setInMap(final String cid) {
                    db.collection("CustomerUsers").document(cid).collection("OrderCart").get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot qs = task.getResult();
                                    List<DocumentSnapshot> ds = qs.getDocuments();
                                    for(DocumentSnapshot x:ds){
                                        fatchdata(x.getId());
                                    }
                                }

                                private void fatchdata(String id) {
                                    db.collection("CustomerUsers").document(cid).collection("OrderCart").document(id).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    DocumentSnapshot ds = task.getResult();
                                                    datamap.put("Menu",ds.getString("Menu"));
                                                    datamap.put("Type",ds.getString("Type"));
                                                    datamap.put("cost",ds.getString("cost"));
                                                    datamap.put("CutomerName",bundle.getString("Name"));
                                                    datamap.put("MobileNO",firebaseUser.getPhoneNumber());

                                                    db.collection("SupplierUsers").document(sid).collection("TiffinOrder").add(datamap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                 Toast.makeText(getApplicationContext(),"Order Placed",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                    datamap2.put("Menu",ds.getString("Menu"));
                                                    datamap2.put("Type",ds.getString("Type"));
                                                    datamap2.put("cost",ds.getString("cost"));
                                                    db.collection("CustomerUsers").document(cid).collection("History").add(datamap2).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        }
                                                    });
                                                }
                                            });
                                }
                            }
                    );

            }
        });

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
        final Query query  = db.collection("CustomerUsers").document(id).collection("OrderCart");
        FirestoreRecyclerOptions<MenuDataModel> options = new FirestoreRecyclerOptions.Builder<MenuDataModel>()
                .setQuery(query, MenuDataModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<MenuDataModel, MenuDataListViewHolder>(options) {
            @NonNull
            @Override
            public MenuDataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_order, parent, false);
                return new MenuDataListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuDataListViewHolder dataViewHolder, final int i, @NonNull final MenuDataModel data) {
                dataViewHolder.setMenu(data.getMenu());
                dataViewHolder.setCost(data.getCost());
                dataViewHolder.setType(data.getType());
                dataViewHolder.setCookName(data.getCookName());

                dataViewHolder.removecartdata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeToCartData(v, i, data);
                    }
                });
            }
        };

        recycler_view.setAdapter(adapter);
        adapter.startListening();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot qs = task.getResult();
                List<DocumentSnapshot> ds = qs.getDocuments();
                for (DocumentSnapshot x:ds){
                    Total = Total + Integer.parseInt(x.get("cost").toString());
                }
                Totaltextview.setText("Total : "+Total);
            }
        });

    }
    private void removeToCartData(final View v, final int position, final MenuDataModel data) {
        db.collection("CustomerUsers").whereEqualTo("PhoneNo", firebaseUser.getPhoneNumber()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot qs = task.getResult();
                            List<DocumentSnapshot> list = qs.getDocuments();
                            addData(list.get(0).getId());
                        }
                    }
                    private void addData(String id) {
                        final CollectionReference collectionReference = db.collection("CustomerUsers").document(id).collection("OrderCart");
                        Query query = collectionReference.whereEqualTo("Menu", data.getMenu())
                                .whereEqualTo("cost", data.getCost());
                        Log.d("In--", data.getType());
                        query.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("In--", "ONcomplate" + task.getResult().getDocuments());
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                Log.d("In--", "Before delete");
                                                try {
                                                    collectionReference.document(documentSnapshot.getId()).delete();
                                                    Toast.makeText(v.getContext(), "Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                                                }catch(Exception x){
                                                    Toast.makeText(v.getContext(), "Already clear", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Failer----", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    private void layoutAuth() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.recyclerview_cartorder);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
    }
}

