package com.example.njjeske.cs668androiddiabetesapp.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.njjeske.cs668androiddiabetesapp.DatabaseHelper;
import com.example.njjeske.cs668androiddiabetesapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int maxDate = 31;
    private static final int minDate = 1;
    private Cursor cursor;
    private String mParam1;
    private String mParam2;
    DatabaseHelper db;
    private BarChart chart;

    public static Fragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GraphFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph, container, false);

        // create a new chart object
        chart = (BarChart) v.findViewById(R.id.barChart);
        chart.getDescription().setText("");
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        List<BarEntry> entries = new ArrayList<BarEntry>();
        cursor = db.getDataByActivity("Blood Glucose");

        float data;
        String date;
        String[] xAxisLabels = new String[30];

        for (int i = 0; i < 30; i++) {
            xAxisLabels[i] = (Integer.toString(i + 1));
        }

        if (cursor.moveToNext()) {
            do {
                data = Float.parseFloat(cursor.getString(4));
                date = cursor.getString(2);
                entries.add(new BarEntry(getDate(date), data));
            } while (cursor.moveToNext());
            cursor.close();
        }

        BarDataSet dataSet = new BarDataSet(entries, "BGL Value");
        dataSet.setBarBorderColor(R.color.color_btnPrimaryDark);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);
        chart.setData(barData);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        return v;
    }

    private int getDate(String data) {
        String[] ret = data.split("/");
        return Integer.parseInt(ret[1]);
    }

    @Override
    public void onPause() {
        super.onPause();
        cursor.close();
        db.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
