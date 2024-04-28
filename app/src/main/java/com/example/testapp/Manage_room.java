package com.example.testapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
        String[] dataType = {"Action/", "Détection/", "Mode/"};
        TextView text = findViewById(R.id.name);
        text.setText(name);
        text.setTextSize(25);
        roomCaptor = getIntent().getStringArrayListExtra("roomCaptor");
        roomCaptorData = (ArrayList<?>) getIntent().getSerializableExtra("roomCaptorData");
        database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Maison/" + name);
        HashMap<String, Object> captorData = new HashMap<String, Object>();
        for (int i = 0; i < roomCaptor.size(); i++) {
            captorData.put(roomCaptor.get(i), roomCaptorData.get(i));
        }

        TableLayout table = (TableLayout) findViewById(R.id.table);
        initialize(captorData, table);
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String type : dataType) {
                    for (String captor : roomCaptor) {
                        Object value = snapshot.child(type + captor).getValue();
                        if (value != null) {
                            switch (captor) {
                                case "Lampe":
                                    ToggleButton buttonL = findViewById(R.id.lightToggle);
                                    buttonL.setChecked((boolean) value);
                                    break;
                                case "Lampe automatique":
                                    ToggleButton buttonLAMode = findViewById(R.id.lightAutoModeToggle);
                                    buttonLAMode.setChecked((boolean) value);
                                    //if ((boolean) value) {
                                    //    Toast.makeText(Manage_room.this, getString(R.string.mode_auto_on), Toast.LENGTH_SHORT).show();
                                    //} else {
                                    //    Toast.makeText(Manage_room.this, getString(R.string.mode_auto_off), Toast.LENGTH_SHORT).show();
                                    //}
                                    break;
                                case "Lampe RGB":
                                    ToggleButton buttonRGB = findViewById(R.id.RGBid);
                                    if ("Rouge".equals(value)) {
                                        buttonRGB.setTextColor(ContextCompat.getColor(context, R.color.Rouge));
                                        buttonRGB.setChecked(true);
                                    } else if ("Bleu".equals(value)) {
                                        buttonRGB.setTextColor(ContextCompat.getColor(context, R.color.Bleu));
                                        buttonRGB.setChecked(true);
                                    } else if ("Vert".equals(value)) {
                                        buttonRGB.setTextColor(ContextCompat.getColor(context, R.color.Vert));
                                        buttonRGB.setChecked(true);
                                    } else if ("Jaune".equals(value)) {
                                        buttonRGB.setTextColor(ContextCompat.getColor(context, R.color.Jaune));
                                        buttonRGB.setChecked(true);
                                    } else if ("Cyan".equals(value)) {
                                        buttonRGB.setTextColor(ContextCompat.getColor(context, R.color.Cyan));
                                        buttonRGB.setChecked(true);
                                    } else if ("Magenta".equals(value)) {
                                        buttonRGB.setTextColor(ContextCompat.getColor(context, R.color.Magenta));
                                        buttonRGB.setChecked(true);
                                    } else if ("Blanc".equals(value)) {
                                        buttonRGB.setTextColor(ContextCompat.getColor(context, R.color.Blanc));
                                        buttonRGB.setChecked(true);
                                    } else if ("Off".equals(value)) {
                                        buttonRGB.setChecked(false);
                                    }
                                    break;
                                case "Alarme mouvement":
                                    ToggleButton alarm = findViewById(R.id.alarmToggle);
                                    alarm.setChecked((boolean) value);
                                    break;
                                case "ModeVoyageur":
                                    ToggleButton traveller = findViewById(R.id.travellerToggle);
                                    traveller.setChecked((boolean) value);
                                    break;
                                case "Alarme":
                                    TextView textAlarm = findViewById(R.id.flammeValue);
                                    if ((boolean) value) {
                                        textAlarm.setText("Flamme détectée !!!");
                                    } else {
                                        textAlarm.setText("Pas de flamme détectée");
                                    }
                                    break;
                                case "Volet":
                                    ToggleButton buttonV = findViewById(R.id.voletValue);
                                    buttonV.setChecked((boolean) value);
                                    break;
                                case "Volet automatique":
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
                                case "Chauffage automatique":
                                    ToggleButton heaterAuto = findViewById(R.id.heaterAuto);
                                    heaterAuto.setChecked((boolean) value);
                                    break;
                                case "Porte connectée":
                                    ToggleButton door = findViewById(R.id.door);
                                    door.setChecked((boolean) value);
                                    break;
                                case "Détecteur d'humidité":
                                    TextView humidite = findViewById(R.id.humiditeText);
                                    humidite.setText(getString(R.string.humidite) + value.toString());
                                    break;
                            }
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
        List<String> keySet = new ArrayList<>(map.keySet());
        Collections.sort(keySet, Collections.reverseOrder());
        for (String keys : keySet) {
            //TextView text = new TextView(context);
            //text.setText(keys);
            //text.setLayoutParams(parameter);
            //TableRow row = new TableRow(context);
            //row.addView(text, parameter);
            //table.addView(row, parameter);
            Log.d("Affiche Keys", keys);
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/");
            switch (keys) {
                case "Lampe automatique":
                    // création du bouton pour forcer l'activation des lampes
                    ToggleButton buttonLA = new ToggleButton(context);
                    buttonLA.setId(R.id.lightToggle);
                    buttonLA.setChecked((Boolean) map.get(keys));
                    buttonLA.setTextOff(getString(R.string.lampe_off));
                    buttonLA.setTextOn(getString(R.string.lampe_on));
                    buttonLA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name);
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                // On désactive le mode
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Object mode_data = task.getResult().child("Mode/Lampe automatique").getValue();
                                    if ((boolean) mode_data) {
                                        databaseRef.child("Mode/Lampe automatique").setValue(false);
                                        Toast.makeText(Manage_room.this, getString(R.string.mode_auto_off), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                // On change la valeur de la lampe
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data", task.getException());
                                    }
                                    Object data = task.getResult().child("Action/Lampe").getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        databaseRef.child("Action/Lampe").setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    // création du bouton pour changer le mode
                    ToggleButton buttonLAMode = new ToggleButton(context);
                    buttonLAMode.setId(R.id.lightAutoModeToggle);
                    buttonLAMode.setChecked((Boolean) map.get("Lampe automatique"));
                    buttonLAMode.setTextOff(getString(R.string.mode_off));
                    buttonLAMode.setTextOn(getString(R.string.mode_on));
                    buttonLAMode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Mode/Lampe automatique");
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

                    //
                    // changer alarme en mode voyageur
                    //
                    ToggleButton traveller = new ToggleButton(context);
                    traveller.setId(R.id.travellerToggle);
                    Log.e("TESTTEST", String.valueOf(map.get("ModeVoyageur")));
                    traveller.setChecked((boolean) map.get("ModeVoyageur"));
                    traveller.setTextOn(getString(R.string.traveller_on));
                    traveller.setTextOff(getString(R.string.traveller_off));
                    traveller.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference("Maison/" + name + "/Mode/ModeVoyageur");
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
                    ToggleButton alarmMouv = new ToggleButton(context);
                    alarmMouv.setId(R.id.alarmToggle);
                    alarmMouv.setChecked((boolean) map.get("Alarme mouvement"));
                    alarmMouv.setTextOn(getString(R.string.alarm_on));
                    alarmMouv.setTextOff(getString(R.string.alarm_off));
                    alarmMouv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference("Maison/" + name + "/Action/Alarme mouvement");
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

                    TableRow light_and_auto = new TableRow(context);
                    //light_and_auto.setId(R.id.light_and_auto);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    );
                    params.setMarginEnd(10);
                    params.setMarginStart(10);
                    buttonLA.setLayoutParams(params);
                    buttonLAMode.setLayoutParams(params);
                    light_and_auto.addView(buttonLA);
                    light_and_auto.addView(buttonLAMode);
                    TableRow travel = new TableRow(context);
                    traveller.setLayoutParams(params);
                    travel.addView(traveller);
                    travel.addView(alarmMouv);
                    table.addView(light_and_auto);
                    table.addView(travel);
                    break;
                case "Lampe":
                    if (findViewById(R.id.lightToggle) == null) {
                        ToggleButton buttonL = new ToggleButton(context);
                        buttonL.setId(R.id.lightToggle);
                        buttonL.setChecked((Boolean) map.get(keys));
                        buttonL.setTextOff(getString(R.string.lampe_off));
                        buttonL.setTextOn(getString(R.string.lampe_on));
                        buttonL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Action/Lampe");
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
                    }
                    break;
                case "Lampe RGB":
                    ToggleButton RGB = new ToggleButton(context);
                    RGB.setId(R.id.RGBid);
                    RGB.setTextOn(getString(R.string.lampe_on));
                    RGB.setTextOff(getString(R.string.lampe_off));
                    RGB.setChecked(map.get(keys) == "Off");
                    RGB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Action/Lampe RGB");
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data", task.getException());
                                    }
                                    Object data = task.getResult().getValue();
                                    if (data.equals("Off")) {
                                        showPopupMenu(v, databaseRef);
                                    } else {
                                        databaseRef.setValue("Off");
                                        RGB.setTextColor(ContextCompat.getColor(context, R.color.Black));
                                    }
                                }
                            });
                        }
                    });
                    table.addView(RGB);
                    break;
                case "Volet automatique":
                    ToggleButton buttonVA = new ToggleButton(context);
                    buttonVA.setId(R.id.voletValue);
                    buttonVA.setChecked((Boolean) map.get(keys));
                    buttonVA.setTextOff(getString(R.string.volet_off));
                    buttonVA.setTextOn(getString(R.string.volet_on));
                    buttonVA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name);
                            // On désactive le mode auto
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Object mode_data = task.getResult().child("Mode/Volet automatique").getValue();
                                    if ((boolean) mode_data) {
                                        databaseRef.child("Mode/Volet automatique").setValue(false);
                                        Toast.makeText(Manage_room.this, getString(R.string.mode_auto_off), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // On modifie la valeur des volets
                            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Toggle Button", "error retrieving data", task.getException());
                                    }
                                    Object data = task.getResult().child("Action/Volet").getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        databaseRef.child("Action/Volet").setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    table.addView(buttonVA);
                    // création du bouton pour changer le mode des volets
                    ToggleButton buttonVAMode = new ToggleButton(context);
                    buttonVAMode.setId(R.id.voletAutoModeToggle);
                    buttonVAMode.setChecked((Boolean) map.get("Volet automatique"));
                    buttonVAMode.setTextOff(getString(R.string.mode_off));
                    buttonVAMode.setTextOn(getString(R.string.mode_on));
                    buttonVAMode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseRef = database.getReference( "Maison/" + name + "/Mode/Volet automatique");
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
                    table.addView(buttonVAMode);

                    SeekBar luminosity = new SeekBar(context);
                    luminosity.setId(R.id.luminosity);
                    luminosity.setMax(1000);
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
                                luminosity.setProgress(int_data);
                            } else {
                                Log.e("Data type", "Unexpected data type " + data.getClass().getSimpleName() + "expected Long");
                            }
                        }
                    });
                    // ajout un textview pour voir la visualisation des données de la barre
                    TextView displayValue = new TextView(context);
                    luminosity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
                            // rajouter le display des valeurs
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            light.setValue(seekBar.getProgress());
                        }
                    });

                    table.addView(luminosity);

                    break;
                case "Volet":
                    if (findViewById(R.id.voletValue) == null) {
                        ToggleButton buttonV = new ToggleButton(context);
                        buttonV.setId(R.id.voletValue);
                        buttonV.setChecked((Boolean) map.get(keys));
                        buttonV.setTextOff(getString(R.string.volet_off));
                        buttonV.setTextOn(getString(R.string.volet_on));
                        buttonV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference databaseRef = database.getReference("Maison/" + name + "/Action/Volet");
                                databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("Toggle Button", "error retrieving data Volet", task.getException());
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
                    }
                    break;
                case "Chauffage":
                    TextView textTemp = new TextView(context);
                    textTemp.setId(R.id.temperatureValue);
                    DatabaseReference temp = database.getReference("Maison/" + name + "/Détection/Température");
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
                    DatabaseReference heat = database.getReference().child("Seuil Température");
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
                            DatabaseReference heaterRef = database.getReference("Maison/" + name );
                            // On désactive le mode auto
                            heaterRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Object mode_data = task.getResult().child("Mode/Chauffage automatique").getValue();
                                    if ((boolean) mode_data) {
                                        heaterRef.child("Mode/Chauffage automatique").setValue(false);
                                        Toast.makeText(Manage_room.this, getString(R.string.mode_auto_off), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            heaterRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Data chauffage", "error retrieving data Chauffage");
                                    }
                                    Object data = task.getResult().child("Action/Chauffage").getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (Boolean) data;
                                        heaterRef.child("Action/Chauffage").setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    table.addView(buttonHeater);

                    ToggleButton heaterAuto = new ToggleButton(context);
                    heaterAuto.setId(R.id.heaterAuto);
                    heaterAuto.setChecked((boolean) map.get("Chauffage automatique"));
                    heaterAuto.setTextOn(getString(R.string.mode_on));
                    heaterAuto.setTextOff(getString(R.string.mode_off));
                    heaterAuto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference heaterAutoRef = database.getReference("Maison/" + name + "/Mode/Chauffage automatique");
                            heaterAutoRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Data chauffage auto", "error retrieving data chauffage auto");
                                    }
                                    Object data = task.getResult().getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (boolean) data;
                                        heaterAutoRef.setValue(!value);
                                    } else {
                                        Log.e("Toggle Button", "unexpected valur type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    table.addView(heaterAuto);
                    break;
                case "Mouvement":
                    TextView textDM = new TextView(context);
                    textDM.setId(R.id.mouvValue);
                    DatabaseReference mouv = database.getReference("Maison/" + name + "/Détection/Mouvement");
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
                case "Flamme":
                    TextView textDF = new TextView(context);
                    textDF.setId(R.id.flammeValue);
                    DatabaseReference flamme = database.getReference("Maison/" + name + "/Détection/Flamme");
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
                case "Porte connectée":
                    ToggleButton door = new ToggleButton(context);
                    door.setId(R.id.door);
                    door.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference doorRef = database.getReference("Maison/" + name + "/Action/Porte connectée");
                            doorRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("Texte Porte", "error retrieving data Porte", task.getException());
                                    }
                                    Object data = task.getResult().getValue();
                                    if (data instanceof Boolean) {
                                        boolean value = (boolean) data;
                                        doorRef.setValue(!value);
                                    } else {
                                        Log.e("Texte Mouvement", "unexpected value type : " + data.getClass().getSimpleName());
                                    }
                                }
                            });
                        }
                    });
                    table.addView(door);
                    break;
                case "Détecteur d'humidité":
                    TextView humidite = new TextView(context);
                    humidite.setId(R.id.humiditeText);
                    DatabaseReference humiditeRef = database.getReference("Maison/" + name + "/Détection/Détecteur d'humidité");
                    humiditeRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("Texte humidité", "error retrieving data humidite", task.getException());
                            }
                            Object data = task.getResult().getValue();
                            humidite.setText(getString(R.string.humidite) + data.toString());
                        }
                    });
                    table.addView(humidite);
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

    private void showPopupMenu(View v, DatabaseReference db) {
        PopupMenu popupMenu = new PopupMenu(context, v);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.Rouge) {
                    db.setValue("Rouge");
                } else if (itemId == R.id.Bleu) {
                    db.setValue("Bleu");
                } else if (itemId == R.id.Vert) {
                    db.setValue("Vert");
                } else if (itemId == R.id.Jaune) {
                    db.setValue("Jaune");
                } else if (itemId == R.id.Cyan) {
                    db.setValue("Cyan");
                } else if (itemId == R.id.Magenta) {
                    db.setValue("Magenta");
                } else if (itemId == R.id.Blanc) {
                    db.setValue("Blanc");
                }
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_colors);
        popupMenu.show();
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
