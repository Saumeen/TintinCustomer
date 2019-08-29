package com.pkg.tintincustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.renderscript.BaseObj;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.tabs.TabLayout;
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

public class SearchActivity extends AppCompatActivity {
    private BottomAppBar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private TabLayout tabLayout;
    private SearchView searchView;
    private RecyclerView recycler_view;
    private ArrayList<SearchDataListModel> dataModelArrayList;
    private SearchActivityViewAdapter adapter;
    private String cookname;
    private Map<String,Object> searchlist;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar_search);
        searchlist = new HashMap<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layoutAuth();
             searchView = findViewById(R.id.search_searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });




    }

    private void searchData(final String query) {

        db.collection("SearchListData").document("data").get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
                       ArrayList<SearchDataListModel> mx = (ArrayList<SearchDataListModel>) ds.get(query);
                        Log.d("String----",mx.get(0)+"") ;
//                        for(SearchDataListModel g:mx){
//                            SearchDataListModel searchDataModel = new SearchDataListModel(g.getDocumentReference(),query);
//                            dataModelArrayList.add(searchDataModel);
//                            adapter = new SearchActivityViewAdapter(dataModelArrayList);
//                            recycler_view.setAdapter(adapter);
//                        }

                    }
                }
        );
    }


//    private void searchList(String query) {
//        clearSearchlistFirebase();
//        db.collection("SupplierUsers").get().addOnCompleteListener(
//                new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        QuerySnapshot qs = task.getResult();
//                        List<DocumentSnapshot> ds = qs.getDocuments();
//                        for(final DocumentSnapshot x :ds) {
//                            db.collection("SupplierUsers").document(x.getId()).get().addOnCompleteListener(
//                                    new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                            DocumentSnapshot ds = task.getResult();
//                                            cookname = ds.getString("Name");
//                                            passcookname(cookname);
//                                        }
//
//                                        private void passcookname(final String cookname) {
//                                            db.collection("SupplierUsers").document(x.getId()).collection("Menu").get().addOnCompleteListener(
//                                                    new OnCompleteListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                            QuerySnapshot qs2 = task.getResult();
//                                                            List<DocumentSnapshot> ds2 = qs2.getDocuments();
//                                                            for(DocumentSnapshot x2:ds2){
//                                                                db.collection("SupplierUsers").document(x.getId()).collection("Menu").document(x2.getId()).get().addOnCompleteListener(
//                                                                        new OnCompleteListener<DocumentSnapshot>() {
//                                                                            @Override
//                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                                Log.d("Innner Loop","in loop");
//                                                                                DocumentSnapshot ds3 = task.getResult();
//                                                                                SearchDataModel searchDataModel = new SearchDataModel(cookname,ds3.getString("Menu"));
//                                                                                   db.collection("SearchList").add(searchDataModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                                                                          @Override
//                                                                                          public void onComplete(@NonNull Task<DocumentReference> task) {
//                                                                                              Toast.makeText(getApplicationContext(),"Complate",Toast.LENGTH_LONG).show();
//
//                                                                                          }
//                                                                                      }
//                                                                                   );
//
////                                                                                SearchDataModel searchDataModel = new SearchDataModel(cookname,ds3.getString("Menu")
////                                                                                        );
////                                                                                dataModelArrayList.add(searchDataModel);
////                                                                                adapter = new SearchActivityViewAdapter(dataModelArrayList);
////                                                                                recycler_view.setAdapter(adapter);
//
//                                                                            }
//                                                                        }
//                                                                );
//                                                            }
//                                                        }
//                                                    }
//                                            );
//                                        }
//                                    }
//                            );
//
//                        }
//
//                    }
//                }
//        );
//
//    }
//
//    private void clearSearchlistFirebase() {
//        db.collection("SearchList").get().addOnCompleteListener(
//                new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        QuerySnapshot qs= task.getResult();
//                        List<DocumentSnapshot> ds = qs.getDocuments();
//                        for(DocumentSnapshot dx:ds){
//                            db.collection("SearchList").document(dx.getId()).delete().addOnCompleteListener(
//                                    new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                        }
//                                    }
//                            );
//                        }
//                    }
//                }
//        );
//    }

    private void layoutAuth() {
        dataModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.recyclerview_searchcard);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
    }


//    private void searchMenu(String query) {
////        if (query.length() > 0)
////            query = query.substring(0, 1).toUpperCase() + query.substring(1).toLowerCase();
//        final String finalQuery = query;
//
//        db.collection("SupplierUsers").get().addOnCompleteListener(
//                new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        QuerySnapshot qs=task.getResult();
//                        List<DocumentSnapshot> ds = qs.getDocuments();
//                        for(DocumentSnapshot d:ds){
//                            findMenu(d.getId());
//                            Log.d("INloop",d.getId());
//                        }
//                    }
//
//                    private void findMenu(final String supid) {
//                        Log.d("INloop","start"+finalQuery);
//
//                        db.collection("SupplierUsers").document(supid).collection("Menu")
//                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                QuerySnapshot qsn = task.getResult();
//                                List<DocumentSnapshot> dsn = qsn.getDocuments();
//                                for(DocumentSnapshot dx:dsn){
//                                    findData(dx.getId());
//                                    Log.d("INloop-2",dx.getId());
//                                }
//
//                            }
//
//                            private void findData(String menuid) {
//                                db.collection("SupplierUsers").document(supid).get().addOnCompleteListener(
//                                        new OnCompleteListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                DocumentSnapshot dxx= task.getResult();
//                                                cookname =  dxx.getString("Name");
//                                                Log.d("INloop-3",cookname);
//                                            }
//                                        }
//                                );
//                                db.collection("SupplierUsers").document(supid).collection("Menu").document(menuid).get()
//                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                DocumentSnapshot ds = task.getResult();
//                                                Log.d("INloop-4",ds.getId());
//                                                SearchDataModel searchDataModel = new SearchDataModel(ds.getString("Menu"),
//                                                        cookname);
//                                                dataModelArrayList.add(searchDataModel);
//                                                adapter = new SearchActivityViewAdapter(dataModelArrayList);
//                                                recycler_view.setAdapter(adapter);
//                                            }
//                                        });
//                            }
//                        });
//                    }
//                }
//        );
//    }
//



}
