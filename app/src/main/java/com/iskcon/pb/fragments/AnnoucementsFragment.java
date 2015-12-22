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

import com.iskcon.pb.R;
import com.iskcon.pb.adapters.AnnouncementAdapter;
import com.iskcon.pb.models.Announcement;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maygupta on 12/21/15.
 */
public class AnnoucementsFragment extends Fragment {
    private ListView gridView;
    private ArrayList<Announcement>announcements;
    private AnnouncementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.announcements, container, false);

        // Get ListView object from xml
        gridView = (ListView) v.findViewById(R.id.gridview);
        announcements = new ArrayList<>();
        adapter = new AnnouncementAdapter(getContext(), announcements);
        gridView.setAdapter(adapter);

        loadAnnouncements();
        return v;
    }

    private void loadAnnouncements() {
        ParseQuery<Announcement> query = ParseQuery.getQuery(Announcement.class);
        if(!isNetworkAvailable()) {
            query.fromLocalDatastore();
            query.setLimit(10);
        }
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Announcement>() {
            @Override
            public void done(List<Announcement> objects, ParseException e) {
                for (ParseObject object : objects) {
                    Announcement announcement = new Announcement(object.getString("message"),
                            object.getString("date"),
                            object.getString("url")
                    );
                    announcements.add(announcement);
                }
                adapter.notifyDataSetChanged();
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

