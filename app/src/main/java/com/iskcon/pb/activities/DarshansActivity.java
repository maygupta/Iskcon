package com.iskcon.pb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iskcon.pb.R;
import com.iskcon.pb.adapters.ImageAdapter;
import com.iskcon.pb.models.DarshanImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DarshansActivity  extends ActionBarActivity
{
    private ListView gridView;
    private ArrayList<DarshanImage>images;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.darshans);
        gridView = (ListView) findViewById(R.id.gridview);

        setUpOnClickListener();

        Intent intent = getIntent();
        String jsonStr = intent.getStringExtra("darshans");

        try {
            JSONObject darshans = new JSONObject(jsonStr);
            JSONArray darshansArray = darshans.getJSONArray("darshans");
            images = new ArrayList<>();

            for(int i = 0; i < darshansArray.length(); i++) {
                JSONObject row = darshansArray.getJSONObject(i);
                DarshanImage image = new DarshanImage(row.getString("url"), row.getString("name"));
                images.add(image);
            }
            gridView.setAdapter(new ImageAdapter(this, images));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }

    public void setUpOnClickListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intentForFullDarshan = new Intent(DarshansActivity.this, FullDarshanDetailsActivity.class);
                DarshanImage image = images.get(position);
                intentForFullDarshan.putExtra("url", image.url);
                startActivity(intentForFullDarshan);
            }
        });
    }
}
