package com.example.maygupta.iskcon;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;


public class MainActivity extends TabActivity {

    private static TabHost mTabHst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setUpTabs() {
        Resources res = getResources();
        Intent i = new Intent(this,Kirtans.class);
        Intent j = new Intent(this,Lectures.class);
        Intent k = new Intent(this,Darshans.class);
        mTabHst = getTabHost();
        mTabHst.addTab(mTabHst.newTabSpec("tab_1")
                .setIndicator("Kirtans",res.getDrawable(R.drawable.kirtans))
                .setContent(i));
        mTabHst.addTab(mTabHst.newTabSpec("tab_2")
                .setIndicator("Lectures",res.getDrawable(R.drawable.kirtans))
                .setContent(j));
        mTabHst.addTab(mTabHst.newTabSpec("tab_3")
                .setIndicator("Darshans",res.getDrawable(R.drawable.kirtans))
                .setContent(k));
        mTabHst.setCurrentTab(0);
        mTabHst.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                setTabColor(getTabHost());
            }
        });
        setTabColor(mTabHst);
    }

    public static void setCurrentTab(int tab) {
        mTabHst.setCurrentTab(tab);
    }

    public static void setTabColor(TabHost tabhost) {
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#B8B8B8")); //unselected
        }
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#E0E0E0")); // selected
    }

}
