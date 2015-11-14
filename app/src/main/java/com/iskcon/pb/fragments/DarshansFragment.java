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

import com.iskcon.pb.R;
import com.iskcon.pb.activities.FullDarshanDetailsActivity;
import com.iskcon.pb.adapters.ImageAdapter;
import com.iskcon.pb.models.Darshan;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class DarshansFragment extends Fragment
{
    private ListView gridView;
    private ArrayList<Darshan>images;
    private ImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.darshans, container, false);

        // Get ListView object from xml
        gridView = (ListView) v.findViewById(R.id.gridview);
        images = new ArrayList<>();
        adapter = new ImageAdapter(getContext(), images);
        gridView.setAdapter(adapter);

        setUpOnClickListener();
        loadDarshans();
        return v;
    }

    private void loadDarshans() {
        ParseQuery<Darshan> query = ParseQuery.getQuery(Darshan.class);
        if(!isNetworkAvailable()) {
            query.fromLocalDatastore();
            query.setLimit(10);
        }
        query.findInBackground(new FindCallback<Darshan>() {
            @Override
            public void done(List<Darshan> objects, ParseException e) {
                for (ParseObject object : objects) {
                    Darshan darshan = new Darshan(object.getString("url"), object.getString("description"));
                    images.add(darshan);
                }
                adapter.notifyDataSetChanged();
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
