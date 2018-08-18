package com.example.njjeske.cs668androiddiabetesapp.Fragments;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.njjeske.cs668androiddiabetesapp.DatabaseHelper;
import com.example.njjeske.cs668androiddiabetesapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseHelper db;
    ArrayList<Integer> dataList;
    double[] valBGL = new double[1000000];

    //
    private TextView Ave;
    private TextView Min;
    private TextView Max;
    private TextView Med;


    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();


        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        Ave = (TextView) rootView.findViewById(R.id.statViewAve);
        Min = (TextView) rootView.findViewById(R.id.statViewMin);
        Max = (TextView) rootView.findViewById(R.id.statViewMax);
        Med = (TextView) rootView.findViewById(R.id.statViewMedian);


        Ave.setText(String.valueOf("" + averageBGL()));
        Min.setText(String.valueOf("" + minBGL()));
        Max.setText(String.valueOf("" + maxBGL()));
        Med.setText(String.valueOf("" + medBGL()));
        // Inflate the layout for this fragment
        return rootView;
    }

    public double averageBGL() {
        List<Double> temp = new ArrayList<>();
        int sum = 0;
        int counter = 0;
        Cursor cursor = db.getDataByActivity("Blood Glucose");
        if (cursor.moveToFirst()) {
            do {
                sum += Double.parseDouble(cursor.getString(4));
                counter++;
            } while (cursor.moveToNext());
        } else {
            return -1.0;
        }
        cursor.close();
        db.close();
        return sum / counter;
    }

    public double minBGL() {
        List<Double> temp = new ArrayList<>();
        Cursor cursor = db.getDataByActivity("Blood Glucose");

        if (cursor.moveToFirst()) {
            do {
                temp.add(Double.parseDouble(cursor.getString(4)));
            } while (cursor.moveToNext());

        } else {
            return -1.0;
        }
        Collections.sort(temp);

        cursor.close();
        db.close();
        return temp.get(0);
    }

    public double maxBGL() {
        List<Double> temp = new ArrayList<>();
        int counter = 0;
        Cursor cursor = db.getDataByActivity("Blood Glucose");

        if (cursor.moveToFirst()) {
            do {
                temp.add(Double.parseDouble(cursor.getString(4)));
            } while (cursor.moveToNext());

        } else {
            return -1.0;
        }
        Collections.sort(temp);
        cursor.close();
        db.close();
        return temp.get(temp.size() - 1);
    }

    public double medBGL() {
        List<Double> temp = new ArrayList<>();
        int counter = 0;
        Cursor cursor = db.getDataByActivity("Blood Glucose");

        if (cursor.moveToFirst()) {
            do {
                temp.add(Double.parseDouble(cursor.getString(4)));
                counter++;
            } while (cursor.moveToNext());
        }

        Collections.sort(temp);
        Log.w("temp", temp.toString());

        cursor.close();
        db.close();
        if (temp.size() > 1) {
            return temp.get((temp.size() / 2));
        } else if (temp.size() == 1) {
            return temp.get(0);
        } else return 0.0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
