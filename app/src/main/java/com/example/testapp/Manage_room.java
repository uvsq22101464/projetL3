package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Manage_room extends AppCompatActivity {

    Context context = this;
    String name;
    String roomType;
    ArrayList<String> roomCaptor;
    ArrayList<?> roomCaptorData;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_room);
        Log.d("Layout selected", "manage_room");

        name = getIntent().getStringExtra("name");
        TextView text = findViewById(R.id.name);
        text.setText(name);
        text.setTextSize(25);
        roomType = getIntent().getStringExtra("type");
        roomCaptor = getIntent().getStringArrayListExtra("roomCaptor");
        roomCaptorData = (ArrayList<?>) getIntent().getSerializableExtra("roomCaptorData");
        database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference(roomType + "/" + name);
        HashMap<String, Object> captorData = new HashMap<String, Object>();
        for (int i = 0; i < roomCaptor.size(); i++) {
            captorData.put(roomCaptor.get(i), roomCaptorData.get(i));
        }

        TableLayout table = (TableLayout) findViewById(R.id.table);
        initialize(captorData, table);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String captor : roomCaptor) {
                    Object value = snapshot.child(captor).getValue();
                    switch (captor) {
                        case "Light":
                            ToggleButton buttonL = findViewById(R.id.lightToggle);
                            buttonL.setChecked((Boolean) value);
                            break;
                        case "Volets":
                            ToggleButton buttonV = findViewById(R.id.voletValue);
                            buttonV.setChecked((Boolean) value);
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error", "Failed to read value.", error.toException());
            }
        });
    }


    private void initialize(HashMap<String, Object> map, TableLayout table) {
        // met les données de base dans la table
        TableRow.LayoutParams parameter = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        for (String keys : map.keySet()) {
            TextView text = new TextView(context);
            text.setText(keys);
            text.setLayoutParams(parameter);
            TableRow row = new TableRow(context);
            row.addView(text, parameter);
            table.addView(row, parameter);
            switch (keys) {
                case "Light":
                    ToggleButton buttonL = new ToggleButton(context);
                    buttonL.setId(R.id.lightToggle);
                    buttonL.setChecked((Boolean) map.get(keys));
                    buttonL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference(roomType + "/" + name + "/Light");
                            database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data", task.getException());
                                    }
                                    Object data = task.getResult().getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        database.setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });

                        }
                    });
                    table.addView(buttonL);
                    break;
                case "Volets":
                    ToggleButton buttonV = new ToggleButton(context);
                    buttonV.setId(R.id.voletValue);
                    buttonV.setChecked((Boolean) map.get(keys));
                    buttonV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference(roomType + "/" + name + "/Volets");
                            database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data Volets", task.getException());
                                    }
                                    Object data = task.getResult().getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        database.setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });

                        }
                    });
                    table.addView(buttonV);
                    break;
            }
        }
    }

    public void reload(View v) {
        Intent ia = new Intent(this, MainActivity.class);
        startActivity(ia);
    }

    public void modify(View v) {
        Intent ia = new Intent(this, AddRoom.class);
        ia.putExtra("name", name);
        ia.putExtra("roomType", roomType);
        startActivity(ia);
    }

    //toolbar
    public void home(View v) {
        Intent ia = new Intent(this, MainActivity.class);
        startActivity(ia);
    }

    public void scene(View v) {
        Intent ia = new Intent(this, Scene.class);
        startActivity(ia);
    }

    public void planning(View v) {
        Intent ia = new Intent(this, Planning.class);
        startActivity(ia);
    }

    public void display_data(View v) {
        Intent ia = new Intent(this, DisplayTemperature.class);
        startActivity(ia);
    }

}
