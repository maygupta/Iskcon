package com.iskcon.pb;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
        gridview.setAdapter(new ImageAdapter(this));
    }
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        public int getCount() {
            return mThumbIds.length;
        }
        public Object getItem(int position) {
            return mThumbIds[position];
        }
        public long getItemId(int position) {
            return 0;
        }
        public ImageAdapter(Context c) {
            mContext = c;
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
//            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        private String[] mThumbIds = {
            "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/t31.0-8/11908931_854250764651918_1612834854296005262_o.jpg",
                "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xtp1/t31.0-8/11891866_854250787985249_7149663002418038881_o.jpg",
            "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpt1/t31.0-8/11886147_854250867985241_5584858500421011082_o.jpg",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xaf1/t31.0-8/11240091_854251214651873_4119657808997906254_o.jpg",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xtp1/t31.0-8/11872216_853383668071961_6449682566572984649_o.jpg",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/t31.0-8/11894515_853383198072008_6833634790752743256_o.jpg",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/t31.0-8/11894515_853383198072008_6833634790752743256_o.jpg",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/t31.0-8/11894515_853383198072008_6833634790752743256_o.jpg",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/t31.0-8/11894515_853383198072008_6833634790752743256_o.jpg",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xfa1/t31.0-8/11856387_853383351405326_3158197932464725375_o.jpg",
                "https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xfp1/t31.0-8/11885693_853383394738655_7439951516826857613_o.jpg",
                "https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xfp1/t31.0-8/11885693_853383394738655_7439951516826857613_o.jpg",
                "https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xfp1/t31.0-8/11885693_853383394738655_7439951516826857613_o.jpg"

        };

        private String[] mDescriptions = {
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",
                "Krishna Balram, 20th August 2015",

        };


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

            expandedImageView.getLayoutParams().height = 600;
            expandedImageView.getLayoutParams().width = 800;

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
