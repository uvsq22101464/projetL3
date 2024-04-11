package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Planning extends AppCompatActivity {

    ArrayList<String> roomNames;
    String[] listRoom = {"Bedroom", "Living_room", "Kitchen", "Bathroom", "Other"};
    String[] listCaptor = {"Light", "Temperature", "Volets"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning);

        roomNames = getIntent().getStringArrayListExtra("roomNames");
        ArrayList<String> roomWithTemperature = new ArrayList<String>();
        ArrayList<String> roomWithVolets = new ArrayList<String>();
        HashMap<String, ArrayList<String>> mapRoom = getRoomCaptors(roomNames);
        for (String captor : mapRoom.keySet()) {
            switch (captor) {
                case "Temperature":
                    roomWithTemperature.add(String.valueOf(mapRoom.get(captor)));
                case "Volets":
                    roomWithVolets.add(String.valueOf(mapRoom.get(captor)));
            }
        }

        if (!roomWithTemperature.isEmpty()) {
            // créer les objets dans le layout
        }
        if (!roomWithVolets.isEmpty()) {
            // créer les objets dans le layout
        }

    }

    public HashMap<String, ArrayList<String>> getRoomCaptors(ArrayList<String> names) {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        ArrayList<String> name = new ArrayList<String>();
        DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        for (String rooms : listRoom) {
            for (String roomName : names) {
                database.child(rooms).child(roomName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Iterable<DataSnapshot> children = task.getResult().getChildren();
                        for (DataSnapshot captor: children) {
                            if (captor.getValue().equals("Temperature")) {
                                name.add(roomName);
                                map.put("Temperature", name);
                            } else if (captor.getValue().equals("Volets")) {
                                name.add(roomName);
                                map.put("Volets", name);
                            }
                        }
                    }
                });
            }
        }
        return map;
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
