package com.iskcon.pb.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.iskcon.pb.R;
import com.iskcon.pb.activities.FullDarshanDetailsActivity;
import com.iskcon.pb.adapters.ImageAdapter;
import com.iskcon.pb.models.Darshan;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;

public class DarshansFragment extends Fragment
{
    private ListView gridView;
    private ArrayList<Darshan>images;
    private ImageAdapter adapter;
    private AsyncHttpClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.darshans, container, false);

        // Get ListView object from xml
        gridView = (ListView) v.findViewById(R.id.gridview);
        images = new ArrayList<>();
        adapter = new ImageAdapter(getContext(), images);
        gridView.setAdapter(adapter);
        client = new AsyncHttpClient();

        setUpOnClickListener();
        loadDarshans();
        return v;
    }

    private void loadDarshans() {
        if(!isNetworkAvailable()) {
            Toast.makeText(getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        client.get("http://footsteps.herokuapp.com/darshans", null , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject darshanJSON = response.getJSONObject(i);
                        images.add(new Darshan(darshanJSON.getString("url"),
                                darshanJSON.getString("description")));
                    }
                    adapter.notifyDataSetChanged();
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
                Darshan image = images.get(position);
                intentForFullDarshan.putExtra("url", image.url);
                startActivity(intentForFullDarshan);
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
