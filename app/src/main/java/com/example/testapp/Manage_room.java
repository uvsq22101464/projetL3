package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Manage_room extends AppCompatActivity {

    Context context = this;
    String name;
    String roomType;
    ArrayList<String> roomCaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_room);

        name = getIntent().getStringExtra("name");
        roomCaptor = getIntent().getStringArrayListExtra("roomCaptor");
        roomType = getIntent().getStringExtra("type");
        TableRow.LayoutParams parameter = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        TableLayout table = (TableLayout) findViewById(R.id.table);

        for (String captor : roomCaptor) {
            TextView text = new TextView(context);
            text.setText(captor);
            text.setLayoutParams(parameter);
            TableRow row = new TableRow(context);
            row.addView(text, parameter);
            table.addView(row, parameter);
            Log.d("Ajout", captor);
            if (captor.equals("Light")) {
                ToggleButton button = getToggleButton(captor);
                table.addView(button);

            }
            // de plus il faut ajouter la récup de données en temps réel
        }

        TextView page_name = (TextView) findViewById(R.id.name);
        page_name.setText(name);



    }

    @NonNull
    private ToggleButton getToggleButton(String captor) {
        ToggleButton button = new ToggleButton(context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference(roomType + "/" + name + "/" + captor);
                database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Toggle Button", "error retreiving data", task.getException());
                        }
                        Object data = task.getResult().getValue();
                        if (data instanceof Boolean) {
                            boolean value = (Boolean) data;
                            database.setValue(!value);
                        } else {
                            Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
                        }
                    }
                });

            }
        });
        return button;
    }

    public void reload(View v) {
        Intent ia = new Intent(this, MainActivity.class);
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
        startActivity(ia);
    }

    public void display_data(View v) {
        Intent ia = new Intent(this, Display_data.class);
        startActivity(ia);
    }

}
