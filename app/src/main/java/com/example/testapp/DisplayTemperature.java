package com.example.testapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DisplayTemperature extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_temperature);
        Log.d("Layout selected", "display_temperature");

        DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Historique températures");
        LineChart chart = findViewById(R.id.lineChart);
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(true);
        chart.getAxisRight().setEnabled(false);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> data = snapshot.getChildren();
                ArrayList<Entry> values = new ArrayList<>();
                for (DataSnapshot elem : data) {
                    Long date = Long.parseLong(elem.getKey());
                    float millis = (float) date;
                    values.add(new Entry(millis, elem.child("Temperature").getValue(float.class)));

                }
                LineDataSet dataSet = new LineDataSet(values, "Températures");
                dataSet.setLineWidth(2f);
                dataSet.setValueTextSize(10f);
                LineData line = new LineData(dataSet);
                chart.setData(line);
                chart.invalidate();
                XAxis xAxis = chart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        Date date = new Date((long) value*1000);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm");
                        return sdf.format(date);
                    }
                });
                xAxis.setLabelRotationAngle(15f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setAxisMaximum(chart.getXChartMax()+500);
                xAxis.setAxisMinimum(chart.getXChartMin()-500);
                YAxis yAxis = chart.getAxisLeft();
                yAxis.setAxisMaximum(chart.getYMax()+1);
                yAxis.setAxisMinimum(chart.getYMin()-1);
                chart.animateX(1500);
                chart.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //toolbar
    public void home(View v) {
        Intent ia = new Intent(this, MainActivity.class);
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
        Intent ia = new Intent(this, DisplayTemperature.class);
        startActivity(ia);
    }
}
