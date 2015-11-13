package com.iskcon.pb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iskcon.pb.R;
import com.iskcon.pb.models.KirtanData;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by maygupta on 7/4/15.
 */
public class MediaAdapter extends ArrayAdapter<KirtanData> {

    private final Context context;
    private final ArrayList<KirtanData> values;
    private File mDir;

    public MediaAdapter(Context context, ArrayList<KirtanData> data, File dir) {
        super(context, -1, data);
        this.context = context;
        this.values = data;
        mDir = dir;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.media_layout, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.song_name);
        TextView tvSongType = (TextView) convertView.findViewById(R.id.tvSongType);

        tvSongType.setText(values.get(position).getmName());
        textView.setText(values.get(position).author);

        RoundedImageView ivAuthorImage = (RoundedImageView) convertView.findViewById(R.id.ivAuthorImage);
        ivAuthorImage.setImageResource(0);
        Picasso.with(getContext()).load(values.get(position).getImageUrl()).into(ivAuthorImage);

        convertView.setTag(values.get(position).getmUrl());

        return convertView;

    }


}
