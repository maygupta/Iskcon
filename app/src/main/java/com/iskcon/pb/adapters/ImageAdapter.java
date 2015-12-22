package com.iskcon.pb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iskcon.pb.R;
import com.iskcon.pb.models.Darshan;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by maygupta on 10/21/15.
 */
public class ImageAdapter extends ArrayAdapter<Darshan> {

    private static class ViewHolder
    {
        public ImageView  ivImage;
        public TextView tvDescription;
    }

    public ImageAdapter(Context c, List<Darshan> images) {
        super(c,0,images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Darshan image = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivDarshan);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDarshanDescription);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDescription.setText(image.description);

        viewHolder.ivImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(image.url).placeholder(R.drawable.placeholder).into(viewHolder.ivImage);

        return convertView;
    }

}
