package com.iskcon.pb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.iskcon.pb.activities.MediaDetailActivity;
import com.iskcon.pb.R;
import com.iskcon.pb.adapters.MediaAdapter;
import com.iskcon.pb.models.KirtanData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class KirtansFragment extends Fragment {

    ListView listView;
    KirtanData currentKirtanData;
    ArrayAdapter<KirtanData> kirtanAdapter;
    AsyncHttpClient client;
    ArrayList<KirtanData> kirtans;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.media_list_layout, container, false);
        client = new AsyncHttpClient();

        // Get ListView object from xml
        listView = (ListView) v.findViewById(R.id.list);

        kirtans = new ArrayList<KirtanData>();

        kirtanAdapter = new MediaAdapter(getContext(), kirtans, getActivity().getFilesDir());

        // Assign adapter to ListView
        listView.setAdapter(kirtanAdapter);

        setUpViews();
        populateKirtans();
        return v;
    }

    public void populateKirtans() {
        String url = "http://iskonadmin.herokuapp.com/api/kirtans/";
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        kirtans.add(new KirtanData(row.getString("name"), row.getString("url"), row.getString("author")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                kirtanAdapter.notifyDataSetChanged();
            }
        });

    }

    public void setUpViews(){

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                Intent i = new Intent(getActivity(), MediaDetailActivity.class);
                KirtanData kirtanData = kirtans.get(position);
                i.putExtra("kirtan", kirtanData);
                startActivity(i);
            }
        });

    }

}