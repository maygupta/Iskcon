package com.iskcon.pb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by maygupta on 10/21/15.
 */
public class ImageAdapter extends ArrayAdapter<DarshanImage> {

        public ImageAdapter(Context c, List<DarshanImage> images) {
            super(c,0,images);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DarshanImage image = getItem(position);

            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_layout, parent, false);
            }

            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivDarshan);
            TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDarshanDescription);

            tvDescription.setText(image.description);

            ivImage.setImageResource(0);
            Picasso.with(getContext()).load(image.url).into(ivImage);

            return convertView;
        }

    }
