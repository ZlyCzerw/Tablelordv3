package com.tablelord.tablelordv3;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class PatioActivity extends AppCompatActivity {

    private FABColorMaker fabColorMaker;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference patioReference = db.collection("patio");
    private TableAdapter tableAdapter;

    private List<String> occupiedMarkerPatio = new ArrayList<>();
    private List<String> occupiedMarkerHarley = new ArrayList<>();
    private List<String> occupiedMarkerBasement = new ArrayList<>();
    private List<String> occupiedMarkerLevel0 = new ArrayList<>();

    private boolean redLight;
    private int patioSize = 9;
    private int basementSize = 20;
    private int level0Size = 15;
    private int harleySize = 11;

    private CollectionReference harleyFull = db.collection("harley");
    private CollectionReference basementFull = db.collection("basement");
    private CollectionReference level0Full = db.collection("level0");
    private CollectionReference patioFull = db.collection("patio");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patio);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Comfortaa.ttf", true);
//START WITH FAB
        patioFull
                .whereEqualTo("tableOccupied", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                occupiedMarkerPatio.add(document.getId());
                                Log.d(getClass().getName(), document.getId() + " => " + document.getData());
                                FloatingActionButton patio = findViewById(R.id.button_level_patio);
                                //change FAB color

                                if (occupiedMarkerPatio.size() == patioSize) {
                                    patio.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#BF360C")));

                                } else {
                                    patio.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#2EC117")));
                                }
                            }
                        } else {
                            Log.d(getClass().getName(), "Error getting documents: ", task.getException());
                        }

                    }
                });

        harleyFull
                .whereEqualTo("tableOccupied", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                occupiedMarkerHarley.add(document.getId());
                                Log.d(getClass().getName(), document.getId() + " => " + document.getData());
                                FloatingActionButton harley = findViewById(R.id.button_level_harley);
                                //change FAB color
                                if (occupiedMarkerHarley.size() == harleySize) {
                                    harley.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#BF360C")));

                                } else {
                                    harley.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#2EC117")));
                                }
                            }
                        } else {
                            Log.d(getClass().getName(), "Error getting documents: ", task.getException());
                        }

                    }
                });
        basementFull
                .whereEqualTo("tableOccupied", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                occupiedMarkerBasement.add(document.getId());
                                Log.d(getClass().getName(), document.getId() + " => " + document.getData());
                                FloatingActionButton basement = findViewById(R.id.button_basement);
                                //change FAB color
                                if (occupiedMarkerBasement.size() == basementSize) {
                                    basement.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#BF360C")));

                                } else {
                                    basement.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#2EC117")));
                                }
                            }
                        } else {
                            Log.d(getClass().getName(), "Error getting documents: ", task.getException());
                        }

                    }
                });
        level0Full
                .whereEqualTo("tableOccupied", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                occupiedMarkerLevel0.add(document.getId());
                                Log.d(getClass().getName(), document.getId() + " => " + document.getData());
                                FloatingActionButton patio = findViewById(R.id.button_level_zero);
                                //change FAB color
                                if (occupiedMarkerLevel0.size() == level0Size) {
                                    patio.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#BF360C")));

                                } else {
                                    patio.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#2EC117")));
                                }
                            }
                        } else {
                            Log.d(getClass().getName(), "Error getting documents: ", task.getException());
                        }

                    }
                });


        FloatingActionButton basement = findViewById(R.id.button_basement);
        basement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatioActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton level0 = findViewById(R.id.button_level_zero);
        level0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatioActivity.this, Level0Activity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton harley = findViewById(R.id.button_level_harley);
        harley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatioActivity.this, HarleyActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton patio = findViewById(R.id.button_level_patio);
        patio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatioActivity.this, PatioActivity.class);
                startActivity(intent);
            }
        });
//END WITH FAB


        setUpRecyclerView();
        redLight = tableAdapter.isRedLight();
    }
    private void setUpRecyclerView(){
        Query query = patioReference.orderBy("tableNumber",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Table> options = new FirestoreRecyclerOptions.Builder<Table>()
                .setQuery(query, Table.class)
                .build();

        tableAdapter = new TableAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tableAdapter);
        tableAdapter.setNumberOfTables(8);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,0/*ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT*/) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        }).attachToRecyclerView(recyclerView);

        tableAdapter.setOnItemClickListener(new TableAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                Table table = documentSnapshot.toObject(Table.class);

                if(table.isTableOccupied()){
                    patioReference.document(documentSnapshot.getId()).update("tableOccupied",false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "stolik wolny", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " nope (table initially occupied) ", Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                    });

                }else {
                    patioReference.document(documentSnapshot.getId()).update("tableOccupied",true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "stolik zarezerwowany", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " nope (table initially free) ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        tableAdapter.startListening();
    }


}

