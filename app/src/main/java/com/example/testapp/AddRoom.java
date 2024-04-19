package com.example.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddRoom extends AppCompatActivity {

    EditText roomName;
    Spinner type1;
    ArrayList<Spinner> spinners;
    String name;
    String roomType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room);
        Log.d("Layout selected", "add_room");

        roomName = (EditText)findViewById(R.id.roomName);
        type1 = (Spinner)findViewById(R.id.spinner_rooms);
        spinners = new ArrayList<Spinner>();
        name = getIntent().getStringExtra("name");
        if (name != null) {
            roomName.setText(name);
            TextView title = findViewById(R.id.title);
            title.setText("Modifier la pièce");
        }


        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this,
                R.array.roomType,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        type1.setAdapter(adapter1);
        roomType = getIntent().getStringExtra("roomType");
        if (roomType != null) {
            type1.setSelection(adapter1.getPosition(roomType));
        }
        addCaptor(null);
    }

        public void createRoom(View v) {
            String name_selected = roomName.getText().toString();
            if (!name_selected.isEmpty()) {
                String room_type = (type1.getItemAtPosition(type1.getSelectedItemPosition()).toString());
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/");
                for (Spinner spinner : spinners) {
                    String captor = (spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                    DatabaseReference myRef = database.getReference(room_type + "/" + name_selected + "/" + captor);
                    switch (captor) {
                        case "Lampes":
                            myRef.setValue(false);
                            break;
                        case "Volets":
                            myRef.setValue(false);
                            break;
                        case "Détecteur de fumée":
                            myRef.setValue(false);
                            break;
                        case "Détecteur de mouvement":
                            myRef.setValue(false);
                            break;
                    }
                }
                DatabaseReference myRef = database.getReference();
                if (!name.equals(name_selected) && !roomType.equals(room_type)) {
                    myRef.child(roomType + "/" + name).removeValue();
                } else if (!name.equals(name_selected) && roomType.equals(room_type)) {
                    myRef.child(room_type + "/" + name).removeValue();
                } else if (name.equals(name_selected) && !roomType.equals(room_type)) {
                    myRef.child(roomType + "/" + name_selected).removeValue();
                }
                Intent ia = new Intent(this, MainActivity.class);
                startActivity(ia);
            } else {
                Toast.makeText(this, getString(R.string.name_error), Toast.LENGTH_SHORT).show();
            }

        }


    public void addCaptor(View v) {
        if (spinners.size() < getResources().getStringArray(R.array.captors).length)  {
            TableLayout table = findViewById(R.id.add_room_table);
            Spinner spinner = new Spinner(this);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.captors,
                    android.R.layout.simple_spinner_item
            );
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            spinner.setAdapter(adapter);
            spinners.add(spinner);
            TableLayout.LayoutParams parameter = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            parameter.setMargins(0, 0, 0, 20);
            spinner.setLayoutParams(parameter);
            table.addView(spinner);
        } else {
            Toast.makeText(this, "Nombre maximum de capteurs atteint",Toast.LENGTH_SHORT).show();
        }
    }

    public void removeCaptor(View v) {
        if (spinners.size() > 1) {
            TableLayout table = findViewById(R.id.add_room_table);
            table.removeView(spinners.get(spinners.size() - 1));
            spinners.remove(spinners.size() - 1);
        }
    }

}
