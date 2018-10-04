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
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PatioActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference patioReference = db.collection("patio");
    private TableAdapter tableAdapter;
    private boolean redLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patio);

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
        //change FAB color
//        if( tableAdapter.isRedLight()){
//
//            patio.setBackgroundTintList(ColorStateList.valueOf(Color
//                    .parseColor("#BF360C")));
//       }else {
//            patio.setBackgroundTintList(ColorStateList.valueOf(Color
//                    .parseColor("8BC34A")));
//          }
        patio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatioActivity.this, PatioActivity.class);
                startActivity(intent);
            }
        });

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