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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.iskcon.pb.R;
import com.iskcon.pb.activities.MediaDetailActivity;
import com.iskcon.pb.adapters.MediaAdapter;
import com.iskcon.pb.models.Media;
import com.loopj.android.http.AsyncHttpClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class KirtansFragment extends Fragment {

    ListView listView;
    ArrayAdapter<Media> kirtanAdapter;
    AsyncHttpClient client;
    ArrayList<Media> kirtans;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.media_list_layout, container, false);
        client = new AsyncHttpClient();

        // Get ListView object from xml
        listView = (ListView) v.findViewById(R.id.list);

        kirtans = new ArrayList<Media>();

        kirtanAdapter = new MediaAdapter(getContext(), kirtans);

        // Assign adapter to ListView
        listView.setAdapter(kirtanAdapter);

        setUpViews();
        populateKirtans();
        return v;
    }

    public void populateKirtans() {
        ParseQuery<Media> query = ParseQuery.getQuery(Media.class);
        query.whereEqualTo("type", "kirtan");
        if(!isNetworkAvailable()) {
            query.fromLocalDatastore();
        }
        query.findInBackground(new FindCallback<Media>() {
            @Override
            public void done(List<Media> objects, ParseException e) {
                for (Media object : objects) {
                    kirtanAdapter.add(new Media(object.getString("name"),
                            object.getString("author"),
                            object.getString("url"),
                            object.getString("type")));
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
                Media media = kirtans.get(position);
                i.putExtra("url", media.getUrl());
                i.putExtra("image_url", media.getImageUrl());
                i.putExtra("name", media.getName());
                startActivity(i);
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