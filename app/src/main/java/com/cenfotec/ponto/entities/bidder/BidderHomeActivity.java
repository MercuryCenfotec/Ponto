package com.cenfotec.ponto.entities.bidder;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.utils.GeneralActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import adapter.TabLayoutAdapter_BidderHome;

public class BidderHomeActivity extends GeneralActivity {


    protected TabLayoutAdapter_BidderHome viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_home);
        super.initComponents(R.id.homeView,R.id.bidderHomeNavbar);
        setTabs();
    }

    @Override
    protected void chargeAdapterViews(){
        viewPagerAdapter = new TabLayoutAdapter_BidderHome(getSupportFragmentManager());
        activityViewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void setTabs(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeView(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        catchIntent();
    }

    private void catchIntent(){
        if (getIntent().getExtras() != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(4);
            tab.select();
        }
    }

    public void changeView(int position){
        viewPagerAdapter.setActViewPos(position);
        activityViewPager.setAdapter(viewPagerAdapter);
    }

}
