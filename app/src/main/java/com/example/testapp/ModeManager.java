package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ModeManager extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_manager);
        Log.d("Layout selected", "mode_manager");

        TableLayout table = findViewById(R.id.tableMode);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference databaseRef = database.getReference("Maison");
        databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot names : task.getResult().getChildren()) {
                    String name = names.getKey();
                    if (names.child("Mode").exists()) {
                        TextView title = new TextView(context);
                        title.setText(name);
                        title.setTextSize(15);
                        title.setTextColor(getResources().getColor(R.color.Blanc));
                        TableRow titleRow = new TableRow(context);
                        titleRow.addView(title);
                        table.addView(titleRow);
                    }
                    TableRow rowText = new TableRow(context);
                    TableRow rowButton = new TableRow(context);
                    for (DataSnapshot modes : names.child("Mode").getChildren()) {
                        String mode = modes.getKey();
                        ToggleButton button = new ToggleButton(context);
                        button.setTextOn(getString(R.string.mode_on));
                        button.setTextOff(getString(R.string.mode_off));
                        boolean value = (boolean) modes.getValue();
                        button.setChecked(value);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference ref = database.getReference("Maison/" + name + "/Mode/" + mode);
                                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        Log.e("TESTTEST", String.valueOf(task.getResult().getValue()));
                                        Log.e("MODE", mode);
                                        boolean data = (boolean) task.getResult().getValue();
                                        if (data) {
                                            ref.setValue(!data);
                                            button.setChecked(!data);
                                        }
                                    }
                                });
                                ref.setValue(!value);
                                button.setChecked(!value);
                            }
                        });
                        TextView text = new TextView(context);
                        text.setText(mode);
                        text.setTextSize(15);
                        text.setTextColor(getResources().getColor(R.color.Blanc));
                        rowText.addView(text);
                        rowButton.addView(button);
                    }
                    TableRow.LayoutParams paramsText = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                    rowText.setLayoutParams(paramsText);
                    TableRow.LayoutParams paramsButton = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                    rowButton.setLayoutParams(paramsButton);
                    rowText.setLayoutParams(paramsText);
                    rowButton.setLayoutParams(paramsButton);
                    table.addView(rowText);
                    table.addView(rowButton);
                }
            }
        });
    }

    //toolbar
    public void home(View v) {
        Intent ia = new Intent(this, MainActivity.class);
        startActivity(ia);
    }

    public void modeManager(View v) {
        Intent ia = new Intent(this, ModeManager.class);
        startActivity(ia);
    }

    public void display_data(View v) {
        Intent ia = new Intent(this, DisplayTemperature.class);
        startActivity(ia);
    }
}
