package com.iskcon.pb;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Darshans  extends Activity
{

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.darshans);
        GridView gridview = (GridView) findViewById(R.id.gridview);

        Intent intent = getIntent();
        String jsonStr = intent.getStringExtra("darshans");

        try {
            gridview.setAdapter(new ImageAdapter(this, jsonStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private String mJson;
        public int getCount() {
            return mThumbIds.length;
        }
        public Object getItem(int position) {
            return mThumbIds[position];
        }
        public long getItemId(int position) {
            return 0;
        }
        private String[] mThumbIds;
        private String[] mDescriptions;

        public ImageAdapter(Context c, String json) throws JSONException {
            mContext = c;
            mJson = json;
            JSONObject darshans = new JSONObject(json);
            JSONArray darshansArray = darshans.getJSONArray("darshans");

            mThumbIds = new String[darshansArray.length()];
            mDescriptions = new String[darshansArray.length()];

            for(int i = 0; i < darshansArray.length(); i++) {
                JSONObject row = darshansArray.getJSONObject(i);
                mThumbIds[i] = row.getString("url");
                mDescriptions[i] = row.getString("name");
            }

        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            }
            else{
                imageView = (ImageView) convertView;
            }

            int loader = R.drawable.krishna_balram;

            final String imageUrl = mThumbIds[position];

            ImageLoader imageLoader = new ImageLoader(getApplicationContext());

            imageLoader.DisplayImage(imageUrl, 0, imageView);
            imageView.getLayoutParams().height = 300;
            imageView.getLayoutParams().width = 300;

            final String imageDescription = mDescriptions[position];

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        zoomImageFromThumb(imageView, imageUrl, imageDescription);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            return imageView;
        }


        private void zoomImageFromThumb(final View thumbView, String imageUrl, String description) throws IOException {
            // If there's an animation in progress, cancel it
            // immediately and proceed with this one.
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            // Load the high-resolution "zoomed-in" image.
            final ImageView expandedImageView = (ImageView) findViewById(
                    R.id.expanded_image);

            ImageLoader imageLoader = new ImageLoader(getApplicationContext());

            imageLoader.DisplayImage(imageUrl, 0, expandedImageView);

            final Button backButton = (Button) findViewById(R.id.back_button);
            final TextView textView = (TextView) findViewById(R.id.image_description);
            textView.setText(description);

            expandedImageView.setVisibility(View.VISIBLE);
            final GridView gridView = (GridView) findViewById(R.id.gridview);
            gridView.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);

            backButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    expandedImageView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    backButton.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
            });

            expandedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    expandedImageView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    backButton.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
            });
        }
    }
}
