package com.example.maygupta.iskcon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maygupta on 7/4/15.
 */
public class MyArrayAdapter extends ArrayAdapter<KirtanData> {

    private final Context context;
    private final ArrayList<KirtanData> values;

    public MyArrayAdapter(Context context, ArrayList<KirtanData> data) {
        super(context, -1, data);
        this.context = context;
        this.values = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.song_name);
        textView.setText(values.get(position).getmName());

        rowView.setTag(values.get(position).getmUrl());

        return rowView;


    }
}
