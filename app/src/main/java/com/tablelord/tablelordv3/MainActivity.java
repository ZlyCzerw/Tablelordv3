package com.tablelord.tablelordv3;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference basementReference = db.collection("basement");
    private TableAdapter tableAdapter;

    private List<String> occupiedMarkerBasement = new ArrayList<>();

    private int patioSize = 9;
    private int basementSize = 20;
    private int level0Size = 15; //nie kasuje tych rzeczy bo nie :P
    private int harleySize = 11;

    private CollectionReference buttonBasement = db.collection("buttonBasement");
    private CollectionReference buttonHarley = db.collection("buttonHarley");
    private CollectionReference buttonLevel0 = db.collection("buttonLevel0");
    private CollectionReference buttonPatio = db.collection("buttonPatio");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Comfortaa.ttf", true);

// update value of "redLight" if all tables in room are occupied

        basementReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case MODIFIED:

                            basementReference
                                    .whereEqualTo("tableOccupied", true)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                occupiedMarkerBasement.clear();
                                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                                    occupiedMarkerBasement.add(document.getId());
                                                    Log.d(getClass().getName(), document.getId() + " => " + document.getData());
                                                }
                                                    //change FAB color
                                                    //TU MYŚLĘ, ŻE JEST PROBLEM. KIEDY KILKA APLIKACJI PATRZY NA TĄ SAMĄ AKTYWNOŚĆ, WSZYSTKIE NADPISUJĄ TRUE/FALSE DO BAZY DANYCH
                                                    FloatingActionButton basement = findViewById(R.id.button_basement);
                                                    if (occupiedMarkerBasement.size() == basementSize) {
                                                        basement.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#BF360C")));

                                                        buttonBasement.document("basement").update("redLight",true);

                                                    } else {
                                                        basement.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#2EC117")));

                                                        buttonBasement.document("basement").update("redLight", false);
                                                    }

                                            } else {
                                                Log.d(getClass().getName(), "Error getting documents: ", task.getException());
                                            }

                                        }
                                    });

                            Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                    }
                }

            }
        });

//update color of floating action button of courent activity on data change
        buttonHarley.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case MODIFIED:

                            buttonHarley
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            FloatingActionButton harley = findViewById(R.id.button_level_harley);
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    boolean redLight = (Boolean) document.get("redLight");
                                                    if (redLight == true) {
                                                        harley.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#BF360C")));
                                                    } else {
                                                        harley.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#2EC117")));
                                                    }
                                                }
                                            }
                                        }
                                    });

                            Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                    }
                }

            }
        });

        buttonBasement.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case MODIFIED:

                            buttonBasement
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            FloatingActionButton basement = findViewById(R.id.button_basement);
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    boolean redLight = (Boolean) document.get("redLight");
                                                    if (redLight == true) {
                                                        basement.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#BF360C")));
                                                    } else {
                                                        basement.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#2EC117")));
                                                    }
                                                }
                                            }
                                        }
                                    });

                            Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                    }
                }

            }
        });
        buttonLevel0.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case MODIFIED:

                            buttonLevel0
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            FloatingActionButton level0 = findViewById(R.id.button_level_zero);
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    boolean redLight = (Boolean) document.get("redLight");
                                                    if (redLight == true) {
                                                        level0.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#BF360C")));
                                                    } else {
                                                        level0.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#2EC117")));
                                                    }
                                                }
                                            }
                                        }
                                    });

                            Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                    }
                }

            }
        });

        buttonPatio.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case MODIFIED:

                            buttonPatio
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            FloatingActionButton patio = findViewById(R.id.button_level_patio);
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    boolean redLight = (Boolean) document.get("redLight");
                                                    if (redLight == true) {
                                                        patio.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#BF360C")));
                                                    } else {
                                                        patio.setBackgroundTintList(ColorStateList.valueOf(Color
                                                                .parseColor("#2EC117")));
                                                    }
                                                }
                                            }
                                        }
                                    });

                            Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Table.class));
                            break;
                    }
                }

            }
        });
//  Check color of floating action buttons onCreate
        buttonPatio
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        FloatingActionButton patio = findViewById(R.id.button_level_patio);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean redLight = (Boolean) document.get("redLight");
                                if (redLight == true) {
                                    patio.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#BF360C")));
                                } else {
                                    patio.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#2EC117")));
                                }
                            }
                        }
                    }
                });


        buttonHarley
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        FloatingActionButton harley = findViewById(R.id.button_level_harley);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean redLight = (Boolean) document.get("redLight");
                                if (redLight == true) {
                                    harley.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#BF360C")));
                                } else {
                                    harley.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#2EC117")));
                                }
                            }
                        }
                    }
                });

        buttonBasement
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        FloatingActionButton basement = findViewById(R.id.button_basement);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean redLight = (Boolean) document.get("redLight");
                                if (redLight == true) {
                                    basement.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#BF360C")));
                                } else {
                                    basement.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#2EC117")));
                                }
                            }
                        }
                    }
                });

        buttonLevel0
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        FloatingActionButton level0 = findViewById(R.id.button_level_zero);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean redLight = (Boolean) document.get("redLight");
                                if (redLight == true) {
                                    level0.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#BF360C")));
                                } else {
                                    level0.setBackgroundTintList(ColorStateList.valueOf(Color
                                            .parseColor("#2EC117")));
                                }
                            }
                        }
                    }
                });

// end checking

        FloatingActionButton basement = findViewById(R.id.button_basement);
        basement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton level0 = findViewById(R.id.button_level_zero);
        level0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Level0Activity.class);
                    startActivity(intent);
                }
        });
        FloatingActionButton harley = findViewById(R.id.button_level_harley);
        harley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HarleyActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton patio = findViewById(R.id.button_level_patio);
        patio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PatioActivity.class);
                startActivity(intent);
            }
        });

        setUpRecyclerView();
    }
    private void setUpRecyclerView(){
        final Query query = basementReference.orderBy("tableNumber",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Table> options = new FirestoreRecyclerOptions.Builder<Table>()
                .setQuery(query, Table.class)
                .build();

        tableAdapter = new TableAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tableAdapter);

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
                        basementReference.document(documentSnapshot.getId()).update("tableOccupied",false).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        basementReference.document(documentSnapshot.getId()).update("tableOccupied",true).addOnCompleteListener(new OnCompleteListener<Void>() {
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
