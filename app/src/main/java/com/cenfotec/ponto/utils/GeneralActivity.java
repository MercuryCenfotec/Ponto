package com.cenfotec.ponto.utils;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public abstract class GeneralActivity extends AppCompatActivity {

    static final String MY_PREFERENCES = "MyPrefs";
    protected SharedPreferences sharedPreferences;
    protected DatabaseReference databaseReference;
    protected ViewPager activityViewPager;
    protected TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
    }


    protected void initComponents(Integer activityViewPager){
        this.sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        this.activityViewPager = findViewById(activityViewPager);
        chargeAdapterViews();
    }

    protected void initComponents(Integer activityViewPager,Integer tabLayout){
        initComponents(activityViewPager);
        this.tabLayout = findViewById(tabLayout);

    }

    protected void initComponents(Integer activityViewPager,Integer tabLayout,String pathDB){
        initComponents(activityViewPager, tabLayout);
        this.databaseReference = FirebaseDatabase.getInstance().getReference(pathDB);
    }

    protected void redirectTo(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    protected abstract void chargeAdapterViews();

    protected abstract void setTabs();



}
