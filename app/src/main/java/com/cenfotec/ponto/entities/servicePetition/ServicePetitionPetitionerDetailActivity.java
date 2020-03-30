package com.cenfotec.ponto.entities.servicePetition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.petitioner.PetitionerHomeActivity;
import com.google.android.material.tabs.TabLayout;

import adapter.TabLayoutAdapter_ServicePetitionDetailPetitioner;

public class ServicePetitionPetitionerDetailActivity extends AppCompatActivity {

    ImageView returnIcon;
    ViewPager petitionViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_petitioner_detail);
        initContent();
        setContent();
    }

    private void initContent() {
        returnIcon = findViewById(R.id.returnIcon);
        petitionViewPager = findViewById(R.id.petitionViewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setContent() {
        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomePetitioner();
            }
        });
        TabLayoutAdapter_ServicePetitionDetailPetitioner adapter = new TabLayoutAdapter_ServicePetitionDetailPetitioner(getSupportFragmentManager(), tabLayout.getTabCount());
        petitionViewPager.setAdapter(adapter);
        petitionViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                petitionViewPager.setCurrentItem(tab.getPosition());
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void goToHomePetitioner() {
        Intent intent = new Intent(this, PetitionerHomeActivity.class);
        startActivity(intent);
    }
}
