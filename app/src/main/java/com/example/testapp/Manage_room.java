package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
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
        roomCaptor = getIntent().getStringArrayListExtra("roomCaptor");
        roomCaptorData = (ArrayList<?>) getIntent().getSerializableExtra("roomCaptorData");
        database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference( "Maison/" + name + "/Mesures/");
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
                    if (value != null) {
                        switch (captor) {
                            case "Lampes":
                                ToggleButton buttonL = findViewById(R.id.lightToggle);
                                buttonL.setChecked((boolean) value);
                                break;
                            case "Lampes automatiques":
                                ToggleButton buttonLA = findViewById(R.id.lightAutoToggle);
                                buttonLA.setChecked((boolean) value);
                                break;
                            case "Mode Lumière":
                                ToggleButton buttonLAMode = findViewById(R.id.lightAutoModeToggle);
                                buttonLAMode.setChecked((boolean) value);
                                break;
                            case "Volets":
                                ToggleButton buttonV = findViewById(R.id.voletValue);
                                buttonV.setChecked((boolean) value);
                                break;
                            case "Volets automatiques":
                                ToggleButton buttonVA = findViewById(R.id.voletAutoToggle);
                                buttonVA.setChecked((boolean) value);
                                break;
                            case "Mode Volets":
                                ToggleButton buttonVAMode = findViewById(R.id.voletAutoModeToggle);
                                buttonVAMode.setChecked((boolean) value);
                                break;
                            case "Température":
                                TextView textTemp = findViewById(R.id.temperatureValue);
                                textTemp.setText("Température actuelle : " + value + " °C");
                                break;
                            case "Chauffage":
                                ToggleButton heater = findViewById(R.id.buttonHeater);
                                heater.setChecked((boolean) value);
                                break;
                            case "Détecteur de flamme":
                                TextView textDF = findViewById(R.id.flammeValue);
                                if ((boolean) value) {
                                    textDF.setText(getString(R.string.flamme_on));
                                    //
                                    // envoyer notif ici ?
                                    //
                                } else {
                                    textDF.setText(getString(R.string.flamme_off));
                                }
                                break;
                        }
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
            Log.d("Affiche Keys", keys);
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
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Mesures/Lampes");
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
                case "Lampes automatiques":
                    // création du bouton pour forcer l'activation des lampes
                    ToggleButton buttonLA = new ToggleButton(context);
                    buttonLA.setId(R.id.lightAutoToggle);
                    buttonLA.setChecked((Boolean) map.get(keys));
                    buttonLA.setTextOff(getString(R.string.lampes_off));
                    buttonLA.setTextOn(getString(R.string.lampes_on));
                    buttonLA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Mesures");
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Object mode_data = task.getResult().child("Mode Lumière").getValue();
                                    if ((boolean) mode_data) {
                                        databaseRef.child("Mode Lumière").setValue(false);
                                        Toast.makeText(Manage_room.this, getString(R.string.mode_auto_off), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data", task.getException());
                                    }
                                    Object data = task.getResult().child("Lampes automatiques").getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        databaseRef.child("Lampes automatiques").setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    table.addView(buttonLA);
                    // création du bouton pour changer le mode
                    ToggleButton buttonLAMode = new ToggleButton(context);
                    buttonLAMode.setId(R.id.lightAutoModeToggle);
                    buttonLAMode.setChecked((Boolean) map.get("Mode Lumière"));
                    buttonLAMode.setTextOff(getString(R.string.mode_off));
                    buttonLAMode.setTextOn(getString(R.string.mode_on));
                    buttonLAMode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Mesures/Mode Lumière");
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
                                        Toast.makeText(Manage_room.this, getString(R.string.mode_auto_on), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    table.addView(buttonLAMode);
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
                            DatabaseReference databaseRef = database.getReference("Maison/" + name + "/Mesures/Volets");
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
                case "Volets automatiques":
                    ToggleButton buttonVA = new ToggleButton(context);
                    buttonVA.setId(R.id.voletAutoToggle);
                    buttonVA.setChecked((Boolean) map.get(keys));
                    buttonVA.setTextOff(getString(R.string.volets_off));
                    buttonVA.setTextOn(getString(R.string.volets_on));
                    buttonVA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Mesures");
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Object mode_data = task.getResult().child("Mode Volets").getValue();
                                    if ((boolean) mode_data) {
                                        databaseRef.child("Mode Volets").setValue(false);
                                        Toast.makeText(Manage_room.this, getString(R.string.mode_auto_off), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data", task.getException());
                                    }
                                    Object data = task.getResult().child("Volets automatiques").getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        databaseRef.child("Volets automatiques").setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    table.addView(buttonVA);
                    // création du bouton pour changer le mode
                    ToggleButton buttonVAMode = new ToggleButton(context);
                    buttonVAMode.setId(R.id.voletAutoModeToggle);
                    buttonVAMode.setChecked((Boolean) map.get("Mode Volets"));
                    buttonVAMode.setTextOff(getString(R.string.mode_off));
                    buttonVAMode.setTextOn(getString(R.string.mode_on));
                    buttonVAMode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Mesures/Mode Volets");
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
                                        Toast.makeText(Manage_room.this, getString(R.string.mode_auto_on), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    table.addView(buttonVAMode);

                    NumberPicker luminosity = new NumberPicker(context);
                    luminosity.setId(R.id.luminosity);
                    luminosity.setMinValue(300);
                    luminosity.setMaxValue(1000);
                    luminosity.setWrapSelectorWheel(false);
                    DatabaseReference light = database.getReference().child("Seuil Luminosité");
                    light.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("Text luminosity", "error retrieving data luminosity", task.getException());
                            }
                            Object data = task.getResult().getValue();
                            if (data instanceof Long) {
                                Long long_data = (Long) data;
                                int int_data = long_data.intValue();
                                luminosity.setValue(int_data);
                            } else {
                                Log.e("Data type", "Unexpected data type " + data.getClass().getSimpleName() + "expected Long");
                            }
                        }
                    });
                    luminosity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            light.setValue(newVal);
                        }
                    });
                    table.addView(luminosity);

                    break;
                case "Chauffage":
                    TextView textTemp = new TextView(context);
                    textTemp.setId(R.id.temperatureValue);
                    DatabaseReference temp = database.getReference("Maison/" + name + "/Mesures/Température");
                    temp.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("Texte température", "error retrieving data température", task.getException());
                            }
                            Object data = task.getResult().getValue();
                            textTemp.setText("Température actuelle : " + data.toString() + " °C");
                        }
                    });
                    table.addView(textTemp);

                    NumberPicker heater = new NumberPicker(context);
                    heater.setId(R.id.heater);
                    heater.setMinValue(0);
                    heater.setMaxValue(40);
                    heater.setWrapSelectorWheel(false);
                    DatabaseReference heat = database.getReference().child("Température de chauffage");
                    heat.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("Texte chauffage", "error retrieving data heater", task.getException());
                            }
                            Object data = task.getResult().getValue();
                            if (data instanceof Long) {
                                Long long_data = (Long) data;
                                int int_data = long_data.intValue();
                                heater.setValue(int_data);
                            } else {
                                Log.e("Data type", "Unexpected data type " + data.getClass().getSimpleName() + "expected Long");
                            }
                        }
                    });
                    heater.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            heat.setValue(newVal);
                        }
                    });
                    table.addView(heater);

                    ToggleButton buttonHeater = new ToggleButton(context);
                    buttonHeater.setId(R.id.buttonHeater);
                    buttonHeater.setChecked((Boolean) map.get(keys));
                    buttonHeater.setTextOn(getString(R.string.heater_on));
                    buttonHeater.setTextOff(getString(R.string.heater_off));
                    buttonHeater.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference heaterRef = database.getReference("Maison/" + name + "/Mesures/Chauffage");
                            heaterRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Data chauffage", "error retrieving data Chauffage");
                                    }
                                    Object data = task.getResult().getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        heaterRef.setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });

                    table.addView(buttonHeater);
                    break;
                case "Détecteur de flamme":
                    TextView textDF = new TextView(context);
                    textDF.setId(R.id.flammeValue);
                    DatabaseReference flamme = database.getReference("Maison/" + name + "/Mesures/Détecteur de flamme");
                    flamme.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("Texte flamme", "error retrieving data flamme", task.getException());
                            }
                            Object data = task.getResult().getValue();
                            if (data instanceof Boolean) {
                                if ((boolean) data) {
                                    textDF.setText(getString(R.string.flamme_on));
                                    //
                                    // déclencher une notif ?
                                } else {
                                    textDF.setText(getString(R.string.flamme_off));
                                }
                            } else {
                                Log.e("Texte flamme", "unexpected value type : " + data.getClass().getSimpleName());
                            }
                        }
                    });
                    table.addView(textDF);
                    break;
                case "Détecteur de mouvement":
                    TextView textDM = new TextView(context);
                    textDM.setId(R.id.mouvValue);
                    DatabaseReference mouv = database.getReference("Maison/" + name + "/Mesures/Détecteur de mouvement");
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
