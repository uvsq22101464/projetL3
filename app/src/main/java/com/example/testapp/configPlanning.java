package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class configPlanning extends AppCompatActivity {

    ArrayList<String> listRoom;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> selectedItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_planning);
        Log.d("Layout selected", "config_planing");

        listRoom = getIntent().getStringArrayListExtra("rooms");
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listRoom);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        selectedItems = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = listRoom.get(position);
                if (listView.isItemChecked(position)) {
                    if (!selectedItems.contains(selectedItem)) {
                        selectedItems.add(selectedItem);
                    }
                } else {
                    selectedItems.remove(selectedItem);
                }
            }
        });

    }

    public void validate(View v) {
        EditText editText = findViewById(R.id.planningName);
        String name_selected = editText.getText().toString();
        if (!name_selected.isEmpty()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/");
            for (String item : selectedItems) {
                DatabaseReference myRef = database.getReference("Planning Temperature/" + name_selected + "/" + item);
                // envoyer la donnée à garder genre le température à ne pas dépasser ou alors l'heure pour activer le truc
                //
                //
                myRef.setValue(false);
                //
                //
            }

            Intent ia = new Intent(this, MainActivity.class);
            startActivity(ia);
        } else {
            Toast.makeText(this, getString(R.string.name_error), Toast.LENGTH_SHORT).show();
        }

    }

}
