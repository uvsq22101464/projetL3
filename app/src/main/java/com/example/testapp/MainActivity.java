package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    Context context = this;
    TableRow table;
    ArrayList<String> listRoomNames = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        Log.d("Layout selected", "accueil");
        DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        ArrayList<String> roomCaptor = new ArrayList<String>() {};
        ArrayList<Object> roomCaptorData = new ArrayList<Object>() {};
        String[] dataType = {"Action", "Détection", "Mode"};
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                        try {
                            Log.e("JSON data", String.valueOf(task.getResult()));
                            JSONObject data = new JSONObject(convert(task.getResult().getValue()));
                                try {
                                    JSONObject roomData = data.getJSONObject("Maison");
                                    Iterator<String> roomNames = roomData.keys();
                                    while(roomNames.hasNext()) {
                                        String name = roomNames.next();
                                        listRoomNames.add(name);
                                        Log.d("firebase", name);
                                        // on crée le textView et le bouton et on leur donne ce qu'ils contiennent
                                        Button button = new Button(context);
                                        button.setText(name);
                                        button.setGravity(Gravity.CENTER);
                                        // on def le click du bouton
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent ia = new Intent(MainActivity.this, Manage_room.class);
                                                try {
                                                    // on récup les données de la salle dans un JSONObject
                                                    for (String type : dataType) {
                                                        JSONObject inRoomData =  data.getJSONObject("Maison").getJSONObject(name).getJSONObject(type);
                                                        // on itère sur tous les capteurs présent dans la salle
                                                        Iterator<String> inRoomCaptor = inRoomData.keys();
                                                        ia.putExtra("name", name);

                                                        while (inRoomCaptor.hasNext()) {
                                                            String name = inRoomCaptor.next();
                                                            roomCaptor.add(name);
                                                            roomCaptorData.add(inRoomData.get(name));
                                                        }
                                                        Log.d("DATA ROOM CAPTOR", String.valueOf(roomCaptor));
                                                        ia.putExtra("roomCaptor", roomCaptor);
                                                        ia.putExtra("roomCaptorData", (Serializable) roomCaptorData);
                                                        Log.d("Value sent to manage", String.valueOf(roomCaptorData));
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                startActivity(ia);
                                            }
                                        });

                                        TableRow.LayoutParams parameter = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                                        button.setLayoutParams(parameter);
                                        table = (TableRow) findViewById(R.id.table);
                                        table.addView(button);

                                        SharedPreferences storage = getSharedPreferences("data", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor modif_storage = storage.edit();
                                        modif_storage.putString("listRoom", listRoomNames.toString().substring(1, listRoomNames.toString().length() - 1));
                                        modif_storage.apply();
                                    }
                                } catch (JSONException ignored) {}

                        }catch (JSONException e) {
                            e.printStackTrace();
                    }
                }
            }
        });
        }
    public static String convert(Object data) {
        // fonction qui permet de convertir les données contenu dans le datasnaphot en y ajoutant des "" pour pouvoir les transformer plus tard en JSON
        Object temp = data.toString().replaceAll("(\\b[\\w\\s]+)(?==)(?==)", "\"$1\"");
        return temp.toString().replaceAll("(\\b[\\d\\s:/]+)(?=[}])(?=[}])", "\"$1\"");
    }

    public void addRoom(View v) {
        Intent ia = new Intent(this, AddRoom.class);
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