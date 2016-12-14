package com.iskcon.pb.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.iskcon.pb.R;
import com.iskcon.pb.activities.MediaDetailActivity;
import com.iskcon.pb.adapters.MediaAdapter;
import com.iskcon.pb.models.Media;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class LecturesFragment extends Fragment {

    ListView listView;
    MediaAdapter lectureAdapter;
    AsyncHttpClient client;
    ArrayList<Media> lectures;
    ArrayList<Media> originalLectures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.media_list_layout, container, false);
        client = new AsyncHttpClient();

        // Get ListView object from xml
        listView = (ListView) v.findViewById(R.id.list);

        lectures = new ArrayList<Media>();
        originalLectures = new ArrayList<Media>();

        lectureAdapter = new MediaAdapter(getContext(), lectures);

        // Assign adapter to ListView
        listView.setAdapter(lectureAdapter);

        setHasOptionsMenu(true);

        setUpViews();
        populateKirtans();
        return v;
    }

    public void populateKirtans() {
        if(!isNetworkAvailable()) {
            Toast.makeText(getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        client.get("http://footsteps.herokuapp.com/medias?category=lecture", null , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject mediaJSON = response.getJSONObject(i);
                        Media media = new Media(mediaJSON.getString("name"),
                                mediaJSON.getString("author"),
                                mediaJSON.getString("url"),
                                mediaJSON.getString("category"),
                                mediaJSON.getString("author_image_url"));
                        originalLectures.add(media);
                        lectureAdapter.add(media);
                    }
                    lectureAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Unable to load Lectures!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                //filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });

    }

     private void filter(String query) {
        ArrayList<Media> matches = new ArrayList<Media>();
        lectureAdapter.clear();
        if (query == null || query.isEmpty()) {
            lectureAdapter.addAll(originalLectures);
            lectureAdapter.notifyDataSetChanged();
            return;
        }
        for(Media m: originalLectures) {
            if(m.getName().toLowerCase().contains(query.toLowerCase()) ||
                    m.getAuthor().toLowerCase().contains(query.toLowerCase()))  {
                matches.add(m);
            }
        }
        lectureAdapter.addAll(matches);
        lectureAdapter.notifyDataSetChanged();
    }

    public void setUpViews(){
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                Intent i = new Intent(getActivity(), MediaDetailActivity.class);
                Media media = lectures.get(position);
                i.putExtra("url", media.getUrl());
                i.putExtra("type", media.type);
                i.putExtra("author", media.author);
                i.putExtra("author_image_url", media.getImageUrl());
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