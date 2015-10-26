package com.iskcon.pb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DarshansActivity  extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.darshans);
        ListView gridview = (ListView) findViewById(R.id.gridview);

        Intent intent = getIntent();
        String jsonStr = intent.getStringExtra("darshans");

        try {
            JSONObject darshans = new JSONObject(jsonStr);
            JSONArray darshansArray = darshans.getJSONArray("darshans");
            ArrayList<DarshanImage>images = new ArrayList<>();

            for(int i = 0; i < darshansArray.length(); i++) {
                JSONObject row = darshansArray.getJSONObject(i);
                DarshanImage image = new DarshanImage(row.getString("url"), row.getString("name"));
                images.add(image);
            }
            gridview.setAdapter(new ImageAdapter(this, images));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
