package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Manage_room extends AppCompatActivity {

    Context context = this;
    String name;
    ArrayList<String> roomCaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_room);

        name = getIntent().getStringExtra("name");
        roomCaptor = getIntent().getStringArrayListExtra("roomCaptor");
        TableRow.LayoutParams parameter = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow table = (TableRow) findViewById(R.id.table);
        for (String captor : roomCaptor) {
            TextView text = new TextView(context);
            text.setText(captor);
            text.setLayoutParams(parameter);
            table.addView(text, parameter);
            // de plus il faut ajouter la récup de données en temps réel
        }

        TextView page_name = (TextView) findViewById(R.id.name);
        page_name.setText(name);



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
