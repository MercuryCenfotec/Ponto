package com.cenfotec.ponto.entities.bidder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionCreationActivity;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionListActivity;
import com.cenfotec.ponto.entities.offer.OfferCreationActivity;
import com.cenfotec.ponto.entities.offer.OfferDetailActivity;
import com.cenfotec.ponto.utils.LogoutHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import adapter.TabLayoutAdapter_Home;

public class BidderHomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
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
        TabLayoutAdapter_Home adapter = new TabLayoutAdapter_Home(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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


}
