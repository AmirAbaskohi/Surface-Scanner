package com.example.cps2;

import static java.lang.Math.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessageActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;


    private int count = 0;
    float m = 0;

    private float velocity = 0;
    private float position = 0;

    private void setSensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        if(gyroscopeSensor == null){
            Toast.makeText(this, "The Device has no Gyroscope", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        setSensors();

        Intent intent = getIntent();
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);
        TextView textView4 = findViewById(R.id.textView4);



        textView.setText("0");
        textView2.setText("0");
        textView3.setText("0");

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {



                if (abs(event.values[1]) > m){
                    m = abs(event.values[1]);
                }

                if(abs(event.values[1]) > 0.05){
                    velocity += event.values[1] * 0.06;
                    velocity = max(velocity, 0);
                    position += velocity * 0.06;
                }



                count += 1;


                textView.setText(String.format("count: %s", count));
                textView2.setText(String.format("Y: %s", event.values[1]));
                textView3.setText(String.format("velocity: %s", velocity));
                textView4.setText(String.format("Position: %s", position));


//                if(event.values[0] > 0.5f || event.values[0] < -0.5f){
//                    textView.setText(String.format("X: %s", event.values[0]));
//                }
//                else
//                if(event.values[1] > 0.5f || event.values[1] < -0.5f){
//                    textView2.setText(String.format("Y: %s", event.values[1]));
//                }
//                if(event.values[2] > 0.5f || event.values[2] < -0.5f){
//                    textView3.setText(String.format("Z: %s", event.values[2]));
//                }
//                textView4.setText(String.format("Position: %s", position));

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, 2);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
    }
}