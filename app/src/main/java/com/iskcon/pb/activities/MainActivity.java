package com.iskcon.pb.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.iskcon.pb.R;
import com.iskcon.pb.fragments.AnnoucementsFragment;
import com.iskcon.pb.fragments.DarshansFragment;
import com.iskcon.pb.fragments.KirtansFragment;
import com.iskcon.pb.fragments.LecturesFragment;


public class MainActivity extends AppCompatActivity {
    final int PAGE_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        MediaPagerAdapter adapter = new MediaPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapter);
        vpPager.setOffscreenPageLimit(3);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.red))));
        getSupportActionBar().setLogo(R.drawable.push_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MediaPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
        private String tabTitles[] = {"Kirtans", "Lectures", "Darshans", "Announcements"};
        private int tabIcons[] = {R.drawable.ic_kirtan, R.drawable.ic_lecture, R.drawable.ic_darshan,R.drawable.ic_announcement};

        public MediaPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if ( position == 0) {
                return new KirtansFragment();
            } else if (position == 1) {
                return new LecturesFragment();
            }  else if (position == 2) {
                return new DarshansFragment();
            } else if (position == 3) {
                return new AnnoucementsFragment();
            }
            else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public int getPageIconResId(int i) {
            return tabIcons[i];
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
