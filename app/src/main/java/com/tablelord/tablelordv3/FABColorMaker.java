package com.tablelord.tablelordv3;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FABColorMaker {


    private Activity patioActivity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference basementReference = db.collection("basement");
    private CollectionReference harleyReference = db.collection("harley");
    private CollectionReference patioReference = db.collection("patio");
    private CollectionReference level0Reference = db.collection("level0");
    private TableAdapter tableAdapter;
    DocumentSnapshot documentSnapshot;
    private int patioSize = 9;
    private int basementSize = 20;
    private int level0Size = 15;
    private int harleySize = 11;

    List<Task<QuerySnapshot>> tablesBasement = new ArrayList<com.google.android.gms.tasks.Task<com.google.firebase.firestore.QuerySnapshot>>();
    List<Table> tablesLevel0 = new ArrayList<>();
    List<Table> tablesPatio = new ArrayList<>();
    List<Table> tablesHarley = new ArrayList<>();
    private List<String> occupiedMarker = new ArrayList<>();


    public void checkFAB() {

    }

    public int getPatioSize() {
        return patioSize;
    }

    public int getBasementSize() {
        return basementSize;
    }

    public int getLevel0Size() {
        return level0Size;
    }

    public int getHarleySize() {
        return harleySize;
    }
}
