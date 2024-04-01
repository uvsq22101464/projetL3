package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    // on définit table, le layout où on se trouve
    TableRow table_text;
    TableRow table_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        // on cherche l'id de notre layout
        table_text = (TableRow) findViewById(R.id.table_bedroom_text);
        table_button = (TableRow) findViewById(R.id.table_bedroom_button);
        // on cherche le noeud à ajouter
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    try {
                        JSONObject data = new JSONObject(String.valueOf(task.getResult().getValue()));
                        JSONObject bedroomData = data.getJSONObject("Bedroom");
                        Iterator<String> bedroomNames = bedroomData.keys();
                        // faire en sorte de d'abord récup toutes les données qu'on stock dans un tableau puis on itère dans ce tableau pour crée les bouttons
                        while(bedroomNames.hasNext()) {
                            String name = bedroomNames.next();
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
                                    startActivity(ia);
                                }
                            });
                            // on setup des paramètres pour les textes et les boutons
                            TableRow.LayoutParams parameter = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                            txt.setLayoutParams(parameter);
                            button.setLayoutParams(parameter);

                            // on ajoute dans la table parente
                            table_text.addView(txt, parameter);
                            table_button.addView(button, parameter);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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