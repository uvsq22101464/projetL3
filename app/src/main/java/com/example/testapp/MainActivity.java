package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

public class MainActivity extends AppCompatActivity {

    Context context = this;
    // on définit table, le layout où on se trouve
    RelativeLayout table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        // on cherche l'id de notre layout
        table = (RelativeLayout)findViewById(R.id.tableLayout);
        // on cherche le noeud à ajouter
        database.child("ch1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String name = String.valueOf(task.getResult().getValue());
                    Log.d("firebase", name);
                    // row, on crée un nouveau TableRow. TextView, on crée ce qui contient le texte. on set le texte
                    TableRow row = new TableRow(context);
                    TextView txt = new TextView(context);
                    txt.setText(name);
                    // On def des paramètres pour la TableRow qu'on associe aussi au textView
                    TableRow.LayoutParams parametre = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                    txt.setLayoutParams(parametre);
                    // on ajoute le textView à la TableRow
                    row.addView(txt);
                    // on ajoute dans la table la TableRow
                    table.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
        });
        //DatabaseReference myRef = database.getReference("ch1");


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