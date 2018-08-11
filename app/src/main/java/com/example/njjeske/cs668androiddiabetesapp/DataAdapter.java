package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

class DataAdapter extends CursorAdapter {
    public DataAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_result, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvType = (TextView) view.findViewById(R.id.ItemResult_Type);
        TextView tvDate = (TextView) view.findViewById(R.id.ItemResult_Date);
        TextView tvTime = (TextView) view.findViewById(R.id.ItemResult_Time);
        TextView tvDescription = (TextView) view.findViewById(R.id.ItemResult_Description);

        // Extract properties from cursor
        String type = cursor.getString(1);
        String date = cursor.getString(2);
        String time = cursor.getString(3);
        String description = cursor.getString(4);
        // Populate fields with extracted properties
        tvType.setText(type);
        tvDate.setText(date);
        tvTime.setText(time);
        tvDescription.setText(description);
    }
}
