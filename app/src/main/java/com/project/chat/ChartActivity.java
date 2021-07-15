package com.project.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

public class ChartActivity extends AppCompatActivity {

    BarChart barChart;

    // get date and time
    private String currentDate;
    private String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        barChart = findViewById(R.id.bar_chart);



        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(2014, 420));
        visitors.add(new BarEntry(2015, 420));
        visitors.add(new BarEntry(2016, 420));
        visitors.add(new BarEntry(2017, 420));
        visitors.add(new BarEntry(2018, 420));
        visitors.add(new BarEntry(2019, 420));
        visitors.add(new BarEntry(2020, 420));
        visitors.add(new BarEntry(2021, 420));
        visitors.add(new BarEntry(2022, 420));
        visitors.add(new BarEntry(2023, 420));

        BarDataSet barDataSet = new BarDataSet(visitors, "Visirots");
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart");
        barChart.animateY(2000);
        barChart.setVisibleXRangeMaximum(6);
        barChart.moveViewToX(10);
    }

    private void getNow() {
        // get date and time
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd");
        currentDate = simpleDateFormat.format(calendar1.getTime());

        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
        currentTime = simpleTimeFormat.format(calendar2.getTime());
    }
}