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

public class AddRoom extends AppCompatActivity {

    EditText roomName;
    Spinner type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room);

        roomName = (EditText)findViewById(R.id.roomName);
        type = (Spinner)findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.roomType,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        type.setAdapter(adapter);
    }

        public void createRoom(View v) {
            Room room = new Room();
            room.setNom(roomName.getText().toString());
            String name = (type.getItemAtPosition(type.getSelectedItemPosition()).toString());
            room.setType(name);
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

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
