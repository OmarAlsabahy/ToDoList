package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    DatabaseHelper db;
    List<Note> notes;
    Intent intent;
    FloatingActionButton btn_add;
    LinearLayoutManager layoutManager;
    FloatingActionButton btn_delete;
    SearchView search;
    List<Note> searchNotes;
    Intent intent2;
    FloatingActionButton btn_deleteAll;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("onCreate" , "this is onCreate");
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.Recycler_View);
        intent = new Intent(this , AddNote.class);
        btn_add = findViewById(R.id.floatingActionButton2);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        search = findViewById(R.id.search_view);
        intent2 = new Intent(this , MainActivity.class);
        btn_deleteAll = findViewById(R.id.btn_deleteAll);
        alertBuilder = new AlertDialog.Builder(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
            notes = db.getAllNotes();

            recyclerViewAdapter = new RecyclerViewAdapter(notes, this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setHasFixedSize(true);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(s.length()<1)
                {
                    recyclerViewAdapter.changeItems(notes);
                }else {
                    searchNotes = db.selectItems(s);
                    recyclerViewAdapter.changeItems(searchNotes);
                }

                return false;
            }
        });


        btn_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(notes.size()>0) {
                    alertBuilder.setTitle("Alert!!")
                            .setMessage("Dou You Want To Delete All Notes")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    recyclerViewAdapter.deleteAllItems();

                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "There Are No Notes To Be Deleted", Toast.LENGTH_LONG).show();
                }




            }
        });




    }

    public void finishMe()
    {
        finish();

    }
}