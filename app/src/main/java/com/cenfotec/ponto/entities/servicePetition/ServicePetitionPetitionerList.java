package com.cenfotec.ponto.entities.servicePetition;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cenfotec.ponto.R;
import com.google.android.material.tabs.TabLayout;

import adapter.TabLayoutAdapter_BidderList;
import adapter.TabLayoutAdapter_PetitionerList;
import customfonts.MyTextView_SF_Pro_Display_Semibold;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicePetitionPetitionerList extends Fragment {

    View view;//this one it's necessary in a new fragment
    ViewPager viewPager1;
    TabLayout tabLayout1;
    MyTextView_SF_Pro_Display_Semibold btnPetitionCreation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service_petition_petitioner_list, container, false);
        initContent();
        setContent();

        return view;
    }
    private void initContent() {
        btnPetitionCreation = view.findViewById(R.id.btnPetitionCreation);
        viewPager1 = view.findViewById(R.id.viewpager1);
        tabLayout1 = view.findViewById(R.id.tablayout1);
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setContent() {
        //Set the elements function by id
        tabLayout1.addTab(tabLayout1.newTab().setText("Mis solicitudes"));
        TabLayoutAdapter_PetitionerList adapter = new TabLayoutAdapter_PetitionerList(getChildFragmentManager(), tabLayout1.getTabCount());
        viewPager1.setAdapter(adapter);
        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));
        btnPetitionCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ServicePetitionCreationActivity.class);
                startActivity(intent);
            }
        });

    }
}
