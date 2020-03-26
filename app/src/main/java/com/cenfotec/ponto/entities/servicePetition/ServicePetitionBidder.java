package com.cenfotec.ponto.entities.servicePetition;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cenfotec.ponto.R;
import com.google.android.material.tabs.TabLayout;

import adapter.TabLayoutAdapter_List;

public class ServicePetitionBidder extends Fragment {

    View view;//this one it's necessary in a new fragment
    ViewPager viewPager1;
    TabLayout tabLayout1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_petition_bidder,
                container, false);
        initContent();
        setContent();

        return view;
    }
    private void initContent() {
        //Get the elements by the id
        viewPager1 = view.findViewById(R.id.viewpager1);
        tabLayout1 = view.findViewById(R.id.tablayout1);
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setContent() {
        //Set the elements function by id
        tabLayout1.addTab(tabLayout1.newTab().setText("Peticiones"));
        tabLayout1.addTab(tabLayout1.newTab().setText("Mis ofertas"));
        TabLayoutAdapter_List adapter = new TabLayoutAdapter_List(getChildFragmentManager(), tabLayout1.getTabCount());
        viewPager1.setAdapter(adapter);
        viewPager1.setOffscreenPageLimit(1);
        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));

        for (int i = 0; i < tabLayout1.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout1.getTabAt(i);
            if (tab != null) {

                TextView tabTextView = new TextView(getActivity());
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
    //    here goes the activity's methods
}
