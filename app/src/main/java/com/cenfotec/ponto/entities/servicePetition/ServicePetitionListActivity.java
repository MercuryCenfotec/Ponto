package com.cenfotec.ponto.entities.servicePetition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.adapter.TablayoutAdapter_Home;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

import customfonts.MyTextView_SF_Pro_Display_Medium;

public class ServicePetitionListActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    private String activeUserId;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    List<ServicePetition> servicePetitionList;
    ViewPager viewPager1;
    TabLayout tabLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_list);
        initContent();
        getStorageData();
        setData();
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);

        TablayoutAdapter_Home adapter = new TablayoutAdapter_Home(getSupportFragmentManager(), tabLayout1.getTabCount());
        viewPager1.setAdapter(adapter);

        viewPager1.setOffscreenPageLimit(5);
        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));

        for (int i = 0; i < tabLayout1.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout1.getTabAt(i);
            if (tab != null) {

                TextView tabTextView = new TextView(this);
                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());
                tabTextView.setTextColor(Color.parseColor("#acacac"));

                // First tab is the selected tab, so if i==0 then set BOLD typeface
                if (i == 0) {
                    tabTextView.setTypeface(null, Typeface.BOLD);
                    tabTextView.setTextColor(Color.parseColor("#000000"));
                }

            }

        }



        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager1.setCurrentItem(tab.getPosition());
                TextView text = (TextView) tab.getCustomView();

                text.setTextColor(Color.parseColor("#000000"));
                text.setTypeface(null, Typeface.BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                text.setTextColor(Color.parseColor("#acacac"));
                text.setTypeface(null, Typeface.NORMAL);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setData() {
        tabLayout1.addTab(tabLayout1.newTab().setText("Todas"));
        tabLayout1.addTab(tabLayout1.newTab().setText("Mis peticiones"));
        tabLayout1.addTab(tabLayout1.newTab().setText("Mis aplicaciones"));
    }


    private void initContent() {
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        viewPager1 = findViewById(R.id.viewpager1);
        tabLayout1 = findViewById(R.id.tablayout1);
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void getStorageData() {
        activeUserId = sharedPreferences.getString("userId", "");
    }
}
