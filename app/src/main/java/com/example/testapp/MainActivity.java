package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

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
    // on définit table, le layout où on se trouve
    TableRow table_text;
    TableRow table_button;

    ArrayList<String> listRoomNames = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        Log.d("Layout selected", "accueil");
        DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        String[] listRoom = {"Bedroom", "Living_room", "Kitchen", "Bathroom", "Other"};
        String[] listCaptor = {"Light", "Temperature", "Volets"};
        ArrayList<String> roomCaptor = new ArrayList<String>() {};
        ArrayList<Object> roomCaptorData = new ArrayList<Object>() {};
        // on cherche le noeud à ajouter
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

                            for (String roomType : listRoom) {
                                try {
                                    JSONObject roomData = data.getJSONObject(roomType);
                                    Iterator<String> roomNames = roomData.keys();
                                    while(roomNames.hasNext()) {
                                        String name = roomNames.next();
                                        listRoomNames.add(name);
                                        Log.d("firebase", name);
                                        // on crée le textView et le bouton et on leur donne ce qu'ils contiennent
                                        TextView txt = new TextView(context);
                                        Button button = new Button(context);
                                        txt.setText(name);
                                        txt.setGravity(Gravity.CENTER);
                                        button.setText(name);
                                        button.setGravity(Gravity.CENTER);
                                        // on def le click du bouton
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent ia = new Intent(MainActivity.this, Manage_room.class);
                                                try {
                                                    // on récup les données de la salle dans un JSONObject
                                                    JSONObject inRoomData =  data.getJSONObject(roomType).getJSONObject(name);
                                                    // on itère sur tous les capteurs pr&sent dans la salle
                                                    Iterator<String> inRoomCaptor = inRoomData.keys();
                                                    ia.putExtra("name", name);
                                                    ia.putExtra("type", roomType);

                                                    while (inRoomCaptor.hasNext()) {
                                                        String name = inRoomCaptor.next();
                                                        roomCaptor.add(name);
                                                        roomCaptorData.add(inRoomData.get(name));
                                                    }
                                                    ia.putExtra("roomCaptor", roomCaptor);
                                                    ia.putExtra("roomCaptorData", (Serializable) roomCaptorData);
                                                    Log.d("Value sent to manage", String.valueOf(roomCaptorData));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                startActivity(ia);
                                            }
                                        });

                                        // on setup des paramètres pour les textes et les boutons
                                        TableRow.LayoutParams parameter = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                                        txt.setLayoutParams(parameter);
                                        button.setLayoutParams(parameter);

                                        // on ajoute dans la table parente et on cherche la bonne table
                                        switch (roomType) {
                                            case "Bedroom":
                                                table_text = (TableRow) findViewById(R.id.table_bedroom_text);
                                                table_button = (TableRow) findViewById(R.id.table_bedroom_button);
                                                break;
                                            case "Living_room":
                                                table_text = (TableRow) findViewById(R.id.table_living_room_text);
                                                table_button = (TableRow) findViewById(R.id.table_living_room_button);
                                                break;
                                            case "Kitchen":
                                                table_text = (TableRow) findViewById(R.id.table_kitchen_text);
                                                table_button = (TableRow) findViewById(R.id.table_kitchen_button);
                                                break;
                                            case "Bathroom":
                                                table_text = (TableRow) findViewById(R.id.table_bathroom_text);
                                                table_button = (TableRow) findViewById(R.id.table_bathroom_button);
                                                break;
                                            default:
                                                table_text = (TableRow) findViewById(R.id.table_other_text);
                                                table_button = (TableRow) findViewById(R.id.table_other_button);
                                                break;
                                        }
                                        table_text.addView(txt, parameter);
                                        table_button.addView(button, parameter);
                                    }
                                } catch (JSONException ignored) {}
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                    }
                }
            }
        });
        }
    public static String convert(Object data) {
        // fonction qui permet de convertir les données contenu dans le datasnaphot en y ajoutant des "" pour pouvoir les transformer plus tard en JSON
        return data.toString().replaceAll("(\\b[\\w\\s]+)(?=[=:])(?=[=:])", "\"$1\"");
    }

    public void addRoom(View v) {
        Intent ia = new Intent(this, AddRoom.class);
        startActivity(ia);
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
        ia.putExtra("roomNames", listRoomNames);
        startActivity(ia);
    }

    public void display_data(View v) {
        Intent ia = new Intent(this, Display_data.class);
        startActivity(ia);
    }
}