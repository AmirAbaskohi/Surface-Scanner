package com.example.cps2;

import androidx.appcompat.app.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startChartActivity(View view) {
        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }
}