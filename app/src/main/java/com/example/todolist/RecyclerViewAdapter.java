package com.example.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Note> notes;

    private Context context;

    private Intent intent ;
    private Intent intent2;
    private Intent intent3;
    private AlertDialog.Builder alertBuilder;




    public RecyclerViewAdapter(List<Note> notes , Context context) {
        this.notes = notes;
        this.context = context;
        intent = new Intent(this.context , MainActivity.class);
        intent2 = new Intent(this.context , AddNote.class);
        intent3 = new Intent(this.context , Details.class);
        alertBuilder = new AlertDialog.Builder(context);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.noteTitle.setText(notes.get(position).getTitle());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertBuilder.setTitle("Alert!!")
                        .setMessage("Dou You Want To Delete This Note")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteItem(holder.getAdapterPosition());

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();



            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent2.putExtra("title" , notes.get(holder.getAdapterPosition()).getTitle());
                intent2.putExtra("body" , notes.get(holder.getAdapterPosition()).getBody());
                intent2.putExtra("id" , notes.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent2);

            }
        });


        holder.noteTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent3.putExtra("title" , notes.get(holder.getAdapterPosition()).getTitle());
                intent3.putExtra("body" , notes.get(holder.getAdapterPosition()).getBody());
                intent3.putExtra("id" , notes.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent3);
            }
        });



    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void deleteItem(int pos)
    {
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteItem(notes.get(pos).getId());
        notes = db.getAllNotes();
        notifyDataSetChanged();
    }

    public void changeItems(List<Note>notes)
    {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public void deleteAllItems()
    {
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteAllItems(notes);
        notes = db.getAllNotes();
        notifyDataSetChanged();
    }









   public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView noteTitle;
        public FloatingActionButton btn_delete;
        public FloatingActionButton btn_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            btn_delete = itemView.findViewById(R.id.btn_deleteItem);
            btn_edit = itemView.findViewById(R.id.btn_editItem);

        }


    }


}
