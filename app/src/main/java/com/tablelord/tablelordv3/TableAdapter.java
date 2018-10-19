package com.tablelord.tablelordv3;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends FirestoreRecyclerAdapter<Table,TableAdapter.TableHolder> {

    public TableAdapter(@NonNull FirestoreRecyclerOptions<Table> options) {
        super(options);
    }

    private OnItemCLickListener onItemCLickListener;
    private int numberOfTables;
    private boolean redLight = true;


    public boolean isRedLight() {
        return redLight;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.numberOfTables = numberOfTables;
    }

    @Override
    protected void onBindViewHolder(@NonNull TableHolder holder, int position, @NonNull Table model) {
        holder.textViewTableNumber.setText(model.getTableNumber());
        holder.textViewTableSeats.setText(model.getTableSeats());
        // holder.textViewTableOccupied.setText(String.valueOf(model.isTableOccupied()));
//change recyclerview view color based on occupied status
        if (model.isTableOccupied()== true){ holder.itemView.setBackgroundColor(Color.parseColor("#FF635C")); }
        else { holder.itemView.setBackgroundColor(Color.parseColor("#8bd7e0"));}


    }

    @NonNull
    @Override
    public TableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_table,parent,false);
        return new TableHolder(v);

    }

    class TableHolder extends RecyclerView.ViewHolder{

        TextView textViewTableNumber;
        TextView textViewTableSeats;
        //TextView textViewTableOccupied;

        public TableHolder(@NonNull View itemView) {
            super(itemView);
            textViewTableNumber = itemView.findViewById(R.id.text_view_table_number);
            textViewTableSeats = itemView.findViewById(R.id.text_view_table_seats);
           // textViewTableOccupied = itemView.findViewById(R.id.text_view_table_occupied);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemCLickListener!=null){
                        onItemCLickListener.onItemClick( getSnapshots().getSnapshot(position), position );
                    }
                }
            });
        }
    }
    public interface OnItemCLickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemCLickListener onItemClickListener){
        this.onItemCLickListener = onItemClickListener;
    }
}
