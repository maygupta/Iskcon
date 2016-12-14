package com.iskcon.pb.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.iskcon.pb.R;
import com.iskcon.pb.adapters.AnnouncementAdapter;
import com.iskcon.pb.models.Announcement;
import com.iskcon.pb.models.Media;
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

/**
 * Created by maygupta on 12/21/15.
 */
public class AnnoucementsFragment extends Fragment {
    private ListView gridView;
    private ArrayList<Announcement>announcements;
    private AnnouncementAdapter adapter;
    AsyncHttpClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.announcements, container, false);

        client = new AsyncHttpClient();

        // Get ListView object from xml
        gridView = (ListView) v.findViewById(R.id.gridview);
        announcements = new ArrayList<>();
        adapter = new AnnouncementAdapter(getContext(), announcements);
        gridView.setAdapter(adapter);

        loadAnnouncements();
        return v;
    }

    private void loadAnnouncements() {
        if(!isNetworkAvailable()) {
            Toast.makeText(getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        client.get("http://footsteps.herokuapp.com/announcements", null , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        Announcement announcement = new Announcement(json.getString("message"),
                                json.getString("date"),
                                json.getString("url")
                        );
                        announcements.add(announcement);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Unable to load Announcements!", Toast.LENGTH_LONG).show();
                }

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

