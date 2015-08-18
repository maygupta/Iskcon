package com.iskcon.pb;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Created by maygupta on 7/4/15.
 */
public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    String lectures;
    String kirtans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new PrefetchData().execute();
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JsonParser jsonParser = new JsonParser();
            lectures = jsonParser.getJSONFromUrl("http://iskonadmin.herokuapp.com/api/lectures/");
            kirtans= jsonParser.getJSONFromUrl("http://iskonadmin.herokuapp.com/api/kirtans/");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            i.putExtra("lectures", lectures);
            i.putExtra("kirtans", kirtans);
            startActivity(i);

            finish();
        }

    }

}
