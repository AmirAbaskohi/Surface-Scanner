package com.example.cps2;

import static java.lang.Math.*;

import androidx.appcompat.app.*;

import android.content.*;
import android.hardware.*;
import android.os.*;
import android.view.View;
import android.widget.*;

import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.*;

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

    private double theta = 0;
    private float velocity = 0;
    private float positionY = 0;
    private float positionZ = 0;

    private float DELTA_T = 0.06f;


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
                if(abs(event.values[0]) > 0.05){
                    theta += event.values[0] * DELTA_T;
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
                    velocity += event.values[1] * DELTA_T;
                    velocity = max(velocity, 0);

                    positionY += velocity * DELTA_T * cos(abs(theta) > 0.1 ? theta : 0);
                    positionZ += velocity * DELTA_T * sin(abs(theta) > 0.1 ? theta : 0);

                    System.out.println(theta);

                    dataValues.add(new Entry(positionY, positionZ));

                    if(dataValues.size() > 300){
                        dataValues.remove(0);
                    }

                    LineChart mpLineChart;
                    mpLineChart = (LineChart) findViewById(R.id.line_chart);

                    LineDataSet lineDataSet1 = new LineDataSet(dataValues, "Data Set 1");
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet1);
                    LineData data = new LineData(dataSets);

                    lineDataSet1.setDrawCircles(false);
                    lineDataSet1.setDrawValues(false);

                    mpLineChart.setDrawGridBackground(false);
                    mpLineChart.setDrawBorders(false);


                    mpLineChart.getAxisLeft().setAxisMaximum(0.25f);
                    mpLineChart.getAxisLeft().setAxisMinimum(-0.25f);

                    mpLineChart.getAxisRight().setAxisMaximum(0.25f);
                    mpLineChart.getAxisRight().setAxisMinimum(-0.25f);

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

    public void restartActivity(View view){
        theta = 0;
        velocity = 0;
        positionY = 0;
        positionZ = 0;
        dataValues.clear();
        onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(linearAccEventListener, linearAccSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(accEventListener, accSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
        sensorManager.unregisterListener(linearAccEventListener);
        sensorManager.unregisterListener(accEventListener);
    }
}