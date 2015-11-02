package com.iskcon.pb.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iskcon.pb.R;
import com.iskcon.pb.utils.TouchImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by maygupta on 11/1/15.
 */
public class FullDarshanDetailsActivity extends AppCompatActivity {

    TouchImageView ivFullImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_darshan_view_layout);
        ivFullImage = (TouchImageView) findViewById(R.id.ivFullImage);
        Picasso.with(this).load(Uri.parse(getIntent().getStringExtra("url"))).into(ivFullImage);
    }
}
