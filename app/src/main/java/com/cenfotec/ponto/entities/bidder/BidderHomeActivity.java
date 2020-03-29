package com.cenfotec.ponto.entities.bidder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.cenfotec.ponto.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import adapter.TabLayoutAdapter_Home;

public class BidderHomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabLayoutAdapter_Home adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_home);
        bindContent();
        initContent();
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
    }

    private void bindContent() {
        viewPager = findViewById(R.id.homeView);
        tabLayout = findViewById(R.id.bidderHomeNavbar);
    }

    private void initContent() {
        adapter = new TabLayoutAdapter_Home(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeView(tab.getPosition());
                tab.getIcon().setColorFilter(Color.parseColor("#118df0"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
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
