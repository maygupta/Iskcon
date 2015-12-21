package com.iskcon.pb.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iskcon.pb.R;
import com.iskcon.pb.models.Announcement;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by maygupta on 12/21/15.
 */
public class AnnouncementAdapter extends ArrayAdapter<Announcement> {

     private static class ViewHolder
    {
        public TextView tvMessage;
        public TextView tvDate;
        public ImageView imageView;
    }

    public AnnouncementAdapter(Context c, List<Announcement> announcementList) {
        super(c,0,announcementList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Announcement announcement = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_announcement, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvMessage.setText(announcement.message);
        viewHolder.tvDate.setText(announcement.date);
        Picasso.with(getContext()).load(Uri.parse(announcement.url)).into(viewHolder.imageView);

        return convertView;
    }
}
