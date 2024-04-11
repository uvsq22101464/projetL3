package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;


public class Planning extends AppCompatActivity {

    ArrayList<String> roomNames;
    String[] listRoom = {"Bedroom", "Living_room", "Kitchen", "Bathroom", "Other"};
    String[] listCaptor = {"Light", "Temperature", "Volets"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning);

        roomNames = getIntent().getStringArrayListExtra("roomNames");

        getRoomCaptors(roomNames, new CompletionListener() {
            @Override
            public void onComplete(HashMap<String, ArrayList<String>> map) {
                Log.d("Data room with temperature", String.valueOf(map));
                handleRoomCaptors(map);
            }
        });
    }

    public void handleRoomCaptors(HashMap<String, ArrayList<String>> map) {
        ArrayList<String> roomWithTemperature = map.get("Temperature");
        ArrayList<String> roomWithVolets = map.get("Volets");
        if (roomWithTemperature != null && !roomWithTemperature.isEmpty()) {
            Button buttonTemperature = new Button(this);
            buttonTemperature.setText("Gérer la température");
            buttonTemperature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ia = new Intent(Planning.this, configPlanning.class);
                    ia.putExtra("roomWithTemperature", roomWithTemperature);
                    startActivity(ia);
                }
            });
            TableLayout table = findViewById(R.id.tablePlanning);
            table.addView(buttonTemperature);
        }
        if (roomWithVolets != null && !roomWithVolets.isEmpty()) {
            Button buttonVolets = new Button(this);
            buttonVolets.setText("Gérer les volets");
            buttonVolets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ia = new Intent(Planning.this, configPlanning.class);
                    ia.putExtra("roomWithVolets", roomWithTemperature);
                    startActivity(ia);
                }
            });
            TableLayout table = findViewById(R.id.tablePlanning);
            table.addView(buttonVolets);
        }
    }

    public interface CompletionListener {
        void onComplete(HashMap<String, ArrayList<String>> map);
    }

    public void getRoomCaptors(ArrayList<String> names, CompletionListener listener) {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> name = new ArrayList<String>();
        DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        AtomicInteger counter = new AtomicInteger(names.size() * listRoom.length);
        for (String rooms : listRoom) {
            for (String roomName : names) {
                database.child(rooms).child(roomName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            if (task.getResult().getValue() != null) {
                                try {
                                    JSONObject data = new JSONObject(MainActivity.convert(task.getResult().getValue()));
                                    for (Iterator<String> it = data.keys(); it.hasNext(); ) {
                                        String captor = it.next();
                                        if (captor.equals("Temperature")) {
                                            name.add(roomName);
                                            map.put("Temperature", name);
                                        } else if (captor.equals("Volets")) {
                                            name.add(roomName);
                                            map.put("Volets", name);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (counter.decrementAndGet() == 0) {
                            listener.onComplete(map);
                        }
                    }
                });
            }
        }
    }

    //toolbar
    public void edit_room(View v) {
        Intent ia = new Intent(this, Menu.class);
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
        Intent ia = new Intent(this, Display_data.class);
        startActivity(ia);
    }
}
