package com.iskcon.pb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iskcon.pb.R;
import com.iskcon.pb.adapters.ImageAdapter;
import com.iskcon.pb.models.DarshanImage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DarshansFragment extends Fragment
{
    private ListView gridView;
    private ArrayList<DarshanImage>images;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.darshans, container, false);

        // Get ListView object from xml
        gridView = (ListView) v.findViewById(R.id.gridview);
        setUpOnClickListener();
        loadDarshans();
        return v;
    }

    private void loadDarshans() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://iskonadmin.herokuapp.com/api/images/", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                JSONArray darshansArray;
                try {
                    darshansArray = jsonObject.getJSONArray("darshans");
                    images = new ArrayList<>();

                    for(int i = 0; i < darshansArray.length(); i++) {
                        JSONObject row = darshansArray.getJSONObject(i);
                        DarshanImage image = new DarshanImage(row.getString("url"), row.getString("name"));
                        images.add(image);
                    }
                    gridView.setAdapter(new ImageAdapter(getActivity(), images));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void setUpOnClickListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intentForFullDarshan = new Intent(getActivity(), FullDarshanDetailsActivity.class);
                DarshanImage image = images.get(position);
                intentForFullDarshan.putExtra("url", image.url);
                startActivity(intentForFullDarshan);
            }
        });
    }
}
