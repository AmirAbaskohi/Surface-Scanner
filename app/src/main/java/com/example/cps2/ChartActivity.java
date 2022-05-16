package com.example.cps2;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

import androidx.appcompat.app.*;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.*;

public class ChartActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private Sensor linearAccSensor;
    private Sensor accSensor;

    private SensorEventListener gyroscopeEventListener;
    private SensorEventListener linearAccEventListener;
    private SensorEventListener accEventListener;



    ArrayList<Entry> dataValues = new ArrayList<>();
    private int count = 0;

    private double theta = 0;
    private float velocity = 0;
    private float distance = 0;
    private float positionY = 0;
    private float positionZ = 0;


    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    private boolean rightRotate = false;
    private float rotationSpeed;

    private void setSensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        linearAccSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(gyroscopeSensor == null){
            Toast.makeText(this, "The Device has no Gyroscope", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public SensorEventListener getGyroscopeEventListener(){
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                rotationSpeed = event.values[0];
                if(abs(event.values[0]) > 0.05){
                    theta += event.values[0] * 0.06;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    public SensorEventListener getLinearAccEventListener(){
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(abs(event.values[1]) > 0.05){
                    velocity += event.values[1] * 0.06;
                    velocity = max(velocity, 0);
                    distance += velocity * 0.06;

                    positionY += velocity * 0.06 * cos(abs(theta) > 0.15 ? theta : 0);
                    positionZ += velocity * 0.06 * sin(abs(theta) > 0.15 ? theta : 0);

                    System.out.println(theta);

                    dataValues.add(new Entry(positionY, positionZ));

                if(dataValues.size() > 100){
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
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    public SensorEventListener getAccEventListener(){
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                count += 1;
//                theta = acos(min(event.values[2] / 9.8, 1));


//                if(dataValues.size() > 100){
//                    dataValues.remove(0);
//                }

//                textView.setText(String.format("Y: %s", positionY));
//                textView2.setText(String.format("Z: %s", positionZ));
//                textView3.setText(String.format("theta: %s", theta));
//                textView4.setText(String.format("speed: %s", rightRotate));


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setSensors();

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);

        gyroscopeEventListener = getGyroscopeEventListener();
        linearAccEventListener = getLinearAccEventListener();
        accEventListener = getAccEventListener();
    }

    public void stopActivity(View view){
        onPause();
    }

    public void startActivity(View view){
        onResume();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, 2);
        sensorManager.registerListener(linearAccEventListener, linearAccSensor, 2);
        sensorManager.registerListener(accEventListener, accSensor, 2);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
        sensorManager.unregisterListener(linearAccEventListener);
        sensorManager.unregisterListener(accEventListener);
    }
}