package com.example.cps2;

import static java.lang.Math.acos;
import static java.lang.Math.min;
import static java.lang.Math.toDegrees;

import androidx.appcompat.app.*;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.*;

public class ChartActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;



    ArrayList<Entry> dataValues = new ArrayList<>();
    private int count = 0;

    private void setSensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(gyroscopeSensor == null){
            Toast.makeText(this, "The Device has no Gyroscope", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setSensors();



        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                count += 1;
                dataValues.add(new Entry(count, (float) toDegrees(acos(min(event.values[2] / 9.8, 1)))));

                if(dataValues.size() > 25){
                    dataValues.remove(0);
                }

                LineChart mpLineChart;
                mpLineChart = (LineChart) findViewById(R.id.line_chart);
                LineDataSet lineDataSet1 = new LineDataSet(dataValues, "Data Set 1");
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet1);
                LineData data = new LineData(dataSets);
                mpLineChart.setData(data);
                mpLineChart.invalidate();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, 3);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
    }
}