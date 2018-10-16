package com.tablelord.tablelordv3;

import android.content.Intent;
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

import me.anwarshahriar.calligrapher.Calligrapher;

public class Level0Activity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference level0Reference = db.collection("level0");
    private TableAdapter tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level0);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Comfortaa.ttf", true);

        FloatingActionButton basement = findViewById(R.id.button_basement);
        basement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Level0Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton level0 = findViewById(R.id.button_level_zero);
        level0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Level0Activity.this, Level0Activity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton harley = findViewById(R.id.button_level_harley);
        harley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Level0Activity.this, HarleyActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton patio = findViewById(R.id.button_level_patio);
        patio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Level0Activity.this, PatioActivity.class);
                startActivity(intent);
            }
        });

        setUpRecyclerView();
    }
    private void setUpRecyclerView(){
        Query query = level0Reference.orderBy("tableNumber",Query.Direction.ASCENDING);
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
                    level0Reference.document(documentSnapshot.getId()).update("tableOccupied",false).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    level0Reference.document(documentSnapshot.getId()).update("tableOccupied",true).addOnCompleteListener(new OnCompleteListener<Void>() {
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
