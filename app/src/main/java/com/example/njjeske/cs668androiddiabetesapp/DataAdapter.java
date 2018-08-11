package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class DataAdapter extends ArrayAdapter<DB_Object> {
    public DataAdapter(Context context, ArrayList<DB_Object> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DB_Object activity = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_result, parent, false);
        }
        // Lookup view for data population
        TextView tvType = (TextView) convertView.findViewById(R.id.ItemResult_Type);
        TextView tvDate = (TextView) convertView.findViewById(R.id.ItemResult_Date);
        TextView tvTime = (TextView) convertView.findViewById(R.id.ItemResult_Time);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.ItemResult_Description);
        // Populate the data into the template view using the data object
        tvType.setText(activity.getActivityType());
        tvDate.setText(activity.getDate());
        tvTime.setText(activity.getTime());
        tvDescription.setText(activity.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
