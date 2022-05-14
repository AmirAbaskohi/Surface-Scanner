package com.example.cps2;

import androidx.appcompat.app.*;
import android.os.*;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.*;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        LineChart mpLineChart;
        mpLineChart = (LineChart) findViewById(R.id.line_chart);

        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Data Set 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);

        mpLineChart.setData(data);
    }

    private ArrayList<Entry> dataValues1(){
        ArrayList<Entry> dataValues = new ArrayList<>();
        dataValues.add(new Entry(0, 20));
        dataValues.add(new Entry(1, 24));
        dataValues.add(new Entry(2, 2));
        dataValues.add(new Entry(3, 10));
        dataValues.add(new Entry(4, 15));
        return dataValues;
    }
}