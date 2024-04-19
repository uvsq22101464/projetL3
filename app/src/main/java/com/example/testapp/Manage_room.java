package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
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
                        case "Lampes":
                            ToggleButton buttonL = findViewById(R.id.lightToggle);
                            buttonL.setChecked((Boolean) value);
                            break;
                        case "Volets":
                            ToggleButton buttonV = findViewById(R.id.voletValue);
                            buttonV.setChecked((Boolean) value);
                            break;
                        case "Détecteur de fumée":
                            TextView textDF = findViewById(R.id.fumeeValue);
                            if ((boolean) value) {
                                textDF.setText(getString(R.string.fumee_on));
                                //
                                // envoyer notif ici ?
                                //
                            } else {
                                textDF.setText(getString(R.string.fumee_off));
                            }
                            break;
                        case "Détecteur de mouvement":
                            TextView textDM = findViewById(R.id.mouvValue);
                            if ((boolean) value) {
                                textDM.setText(getString(R.string.mouv_on));
                                //
                                // envoyer notif ici ?
                                //
                            } else {
                                textDM.setText(getString(R.string.mouv_off));
                            }
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
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/");
            switch (keys) {
                case "Lampes":
                    ToggleButton buttonL = new ToggleButton(context);
                    buttonL.setId(R.id.lightToggle);
                    buttonL.setChecked((Boolean) map.get(keys));
                    buttonL.setTextOff(getString(R.string.lampes_off));
                    buttonL.setTextOn(getString(R.string.lampes_on));
                    buttonL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference(roomType + "/" + name + "/Lampes");
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data", task.getException());
                                    }
                                    Object data = task.getResult().getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        databaseRef.setValue(!value);
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
                    buttonV.setTextOff(getString(R.string.volets_off));
                    buttonV.setTextOn(getString(R.string.volets_on));
                    buttonV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference(roomType + "/" + name + "/Volets");
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data Volets", task.getException());
                                    }
                                    Object data = task.getResult().getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        databaseRef.setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });

                        }
                    });
                    table.addView(buttonV);
                    break;
                case "Détecteur de fumée":
                    TextView textDF = new TextView(context);
                    textDF.setId(R.id.fumeeValue);
                    DatabaseReference fumee = database.getReference(roomType + "/" + name + "/Détecteur de fumée");
                    fumee.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("Texte Fumée", "error retrieving data Fumée", task.getException());
                            }
                            Object data = task.getResult().getValue();
                            if (data instanceof Boolean) {
                                if ((boolean) data) {
                                    textDF.setText(getString(R.string.fumee_on));
                                    //
                                    // déclencher une notif ?
                                } else {
                                    textDF.setText(getString(R.string.fumee_off));
                                }
                            } else {
                                Log.e("Texte Fumée", "unexpected value type : " + data.getClass().getSimpleName());
                            }
                        }
                    });
                    table.addView(textDF);
                    break;
                case "Détecteur de mouvement":
                    TextView textDM = new TextView(context);
                    textDM.setId(R.id.mouvValue);
                    DatabaseReference mouv = database.getReference(roomType + "/" + name + "/Détecteur de mouvement");
                    mouv.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("Texte Mouvement", "error retrieving data Mouvement", task.getException());
                            }
                            Object data = task.getResult().getValue();
                            if (data instanceof Boolean) {
                                if ((boolean) data) {
                                    textDM.setText(getString(R.string.mouv_on));
                                    //
                                    // déclencher une notif ?
                                } else {
                                    textDM.setText(getString(R.string.mouv_off));
                                }
                            } else {
                                Log.e("Texte Mouvement", "unexpected value type : " + data.getClass().getSimpleName());
                            }
                        }
                    });
                    table.addView(textDM);
                    break;
            }
        }
    }

    public void delete(View v) {
        database.removeValue();
        Toast.makeText(this, name + ", suppression réussi", Toast.LENGTH_SHORT).show();
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
