package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {

    TextView title;
    TextView body;
    FloatingActionButton btn_newNote;
    EditText ed_date;
    Calendar calendar;
    int year;
    int month;
    int day;
    DatabaseHelper db;
    Intent intent;
    String intenTitle;
    String intentBody;
    String intentId;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btn_save)
        {

            if(title.getText().toString().trim().length()>0) {
                if(intent.getStringExtra("title")==null) {
                    db.addNote(title.getText().toString(), body.getText().toString());

                    Log.i("testIf" , "if");

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myNotificationChannel");
                        builder.setContentTitle("TodoList");
                        builder.setContentText("New Note Added at " + day + "/" + month + "/" + year);
                        builder.setSmallIcon(R.drawable.ic_baseline_calendar_today_24);
                        builder.setAutoCancel(false);


                        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
                        manager.notify(1, builder.build());


                }
                else {
                    Log.i("testIf" , intentId + " " + intenTitle + " " + intentBody);
                    db.updateItem(intentId , title.getText().toString() , body.getText().toString());
                    Log.i("testIf" , "else");
                }







            }
            finish();
        }

        else if (item.getItemId() == R.id.btn_back)
        {
            finish();
        }


        else if(item.getItemId() == R.id.btn_copy)
        {
            if(body.getText().toString().length()>0) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copy", body.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "body is empty", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        title = findViewById(R.id.add_note_title);
        body = findViewById(R.id.add_note_body);
       // ed_date = findViewById(R.id.ed_date);
        db = new DatabaseHelper(this);
        intent = getIntent();
        intenTitle = intent.getStringExtra("title");
        intentBody = intent.getStringExtra("body");
        intentId = intent.getStringExtra("id");
        btn_newNote = findViewById(R.id.btn_new_note);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        if(intenTitle!=null)
        {
            title.setText(intenTitle);
            body.setText(intentBody);
            btn_newNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.updateItem(intentId , title.getText().toString() , body.getText().toString());
                    Log.i("testUpdate" , "update is called");
                    title.setText("");
                    body.setText("");

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(AddNote.this, "myNotificationChannel");
                    builder.setContentTitle("TodoList");
                    builder.setContentText("New Note Added at " + day + "/" + month + "/" + year);
                    builder.setSmallIcon(R.drawable.ic_baseline_calendar_today_24);
                    builder.setAutoCancel(false);


                    NotificationManagerCompat manager = NotificationManagerCompat.from(AddNote.this);
                    manager.notify(1, builder.build());

                }


            });
        }



        else {
        btn_newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().trim().length()>0)
                {
                db.addNote(title.getText().toString() ,body.getText().toString());
                title.setText("");
                body.setText("");

                NotificationCompat.Builder builder = new NotificationCompat.Builder(AddNote.this, "myNotificationChannel");
                builder.setContentTitle("TodoList");
                builder.setContentText("New Note Added at " + day + "/" + month + "/" + year);
                builder.setSmallIcon(R.drawable.ic_baseline_calendar_today_24);
                builder.setAutoCancel(false);


                NotificationManagerCompat manager = NotificationManagerCompat.from(AddNote.this);
                manager.notify(1 , builder.build());
            }else {
                  Toast.makeText(AddNote.this , "Please Enter Note" , Toast.LENGTH_LONG).show();
                }


                }
        });

       /* ed_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNote.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year = i;
                        month = i1;
                        day = i2;
                        Log.i("testDate" , ""+day);
                    }
                },year , month , day);

                datePickerDialog.show();
            }
        })*/;}




    }
}