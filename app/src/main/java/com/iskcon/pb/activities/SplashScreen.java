package com.iskcon.pb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.iskcon.pb.R;

/**
 * Created by maygupta on 7/4/15.
 */
public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

//    private class PrefetchData extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            Download.JsonParser jsonParser = new Download.JsonParser();
//            lectures = jsonParser.getJSONFromUrl("http://iskonadmin.herokuapp.com/api/lectures/");
//            darshans = jsonParser.getJSONFromUrl("http://iskonadmin.herokuapp.com/api/images/");
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//
//            Intent i = new Intent(SplashScreen.this, MainActivity.class);
//            i.putExtra("lectures", lectures);
//            i.putExtra("kirtans", kirtans);
//            i.putExtra("darshans", darshans);
//            startActivity(i);
//
//            finish();
//        }
//
//    }

}
