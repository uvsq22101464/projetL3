package com.example.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRoom extends AppCompatActivity {

    EditText roomName;
    Spinner type1;
    Spinner type2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room);

        roomName = (EditText)findViewById(R.id.roomName);
        type1 = (Spinner)findViewById(R.id.spinner_rooms);
        type2 = (Spinner)findViewById(R.id.spinner_captors);

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

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.captors,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        type2.setAdapter(adapter2);

    }

        public void createRoom(View v) {
            String name = roomName.getText().toString();
            String room_type = (type1.getItemAtPosition(type1.getSelectedItemPosition()).toString());
            String captor = (type2.getItemAtPosition(type2.getSelectedItemPosition()).toString());



            // écrire dans la base de données


            FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference(room_type + "/" + name + "/" + captor);
            // converti le type de capteur en la donnée qu'il prend
            Toast.makeText(this, captor, Toast.LENGTH_SHORT).show();
            if (captor.equals("Light")) {
                myRef.setValue(false);
            } else if (captor.equals("Temperature")) {
                myRef.setValue(0.0);
            } else {
                myRef.setValue("Other");
            }


            Intent ia = new Intent(this, Menu.class);
            startActivity(ia);
        }




    //très probablement inutile, j'ai testé sans et ça à l'air de marcher
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item is selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos).
            parent.getItemAtPosition(pos);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback.
        }

    }
}
