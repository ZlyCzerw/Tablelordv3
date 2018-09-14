package com.tablelord.tablelordv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference tableReference = db.collection("Table");
    private TableAdapter tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();
    }
    private void setUpRecyclerView(){
        Query query = tableReference.orderBy("tableNumber",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Table> options = new FirestoreRecyclerOptions.Builder<Table>()
                .setQuery(query, Table.class)
                .build();

        tableAdapter = new TableAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tableAdapter);

        }

    @Override
    protected void onStart() {
        super.onStart();
        tableAdapter.startListening();
    }

}
