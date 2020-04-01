package com.cenfotec.ponto.entities.bidder;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.cenfotec.ponto.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import adapter.TabLayoutAdapter_BidderHome;

public class BidderHomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabLayoutAdapter_BidderHome adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_home);
        bindContent();
        initContent();
        catchIntent();
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
    }

    private void bindContent() {
        viewPager = findViewById(R.id.homeView);
        tabLayout = findViewById(R.id.bidderHomeNavbar);
    }

    private void catchIntent(){
        if (getIntent().getExtras() != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(4);
            tab.select();
        }
    }

    private void initContent() {
        adapter = new TabLayoutAdapter_BidderHome(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
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
    }

    public void changeView(int position){
        adapter.setActViewPos(position);
        viewPager.setAdapter(adapter);
    }

}
