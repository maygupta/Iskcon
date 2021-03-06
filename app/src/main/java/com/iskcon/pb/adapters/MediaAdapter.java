package com.iskcon.pb.adapters;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iskcon.pb.R;
import com.iskcon.pb.models.Media;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by maygupta on 7/4/15.
 */
public class MediaAdapter extends ArrayAdapter<Media> {

    List<Media> mediaList;

    public MediaAdapter(Context context, List<Media> objects) {
        super(context, 0, objects);
        mediaList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Media media = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.media_layout, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.song_name);
        TextView tvSongType = (TextView) convertView.findViewById(R.id.tvSongType);

        tvSongType.setText(media.getName());
        textView.setText(media.getAuthor());

        RoundedImageView ivAuthorImage = (RoundedImageView) convertView.findViewById(R.id.ivAuthorImage);
        ivAuthorImage.setImageResource(0);
        Picasso.with(getContext()).load(media.getImageUrl()).into(ivAuthorImage);

        TextView tvDownloaded = (TextView) convertView.findViewById(R.id.tvDownloaded);
        if (fileExistsOnCard(media.getName())) {
            tvDownloaded.setVisibility(View.VISIBLE);
        } else {
            tvDownloaded.setVisibility(View.INVISIBLE);
        }

        convertView.setTag(media.getUrl());

        return convertView;

    }

    boolean fileExistsOnCard(String name) {
        File file = new File(getFilepath(name));
        return file.exists();
    }

    public String getFilepath(String name) {
        File mFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/iskcon");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        String filename = name.replaceAll(" ", "_").toLowerCase() + ".mp3";
        return mFolder.getAbsolutePath() + "/" + filename;
    }

}
