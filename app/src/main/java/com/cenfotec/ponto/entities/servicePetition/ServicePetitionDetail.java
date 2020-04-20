package com.cenfotec.ponto.entities.servicePetition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Account;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.Carousel_Adapter;
import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Semibold;
import customfonts.TextViewSFProDisplayRegular;
import me.relex.circleindicator.CircleIndicator;

public class ServicePetitionDetail extends Fragment {
    View view;
    public static final String MY_PREFERENCES = "MyPrefs";
    private String petitionId;
    SharedPreferences sharedPreferences;
    Carousel_Adapter carousel_adapter;
    ViewPager carrousellViewPager;
    CircleIndicator circleIndicator;
    TextViewSFProDisplayRegular petitionServiceType;
    MyTextView_SF_Pro_Display_Bold petitionName;
    TextViewSFProDisplayRegular petitionDescription;
    MyTextView_SF_Pro_Display_Semibold btnPetitionUpdate;
    public static ServicePetition servicePetition;
    ServiceType serviceType;
    LinearLayout modifyLayout;

    private TextView endWorkHint;
    private TextView btnEndWork;
    private TextView btnEndWorkDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_petition_detail, container, false);
        // Inflate the layout for this fragment
        initContent();
        getPetitionId();
        chargePetition();
        checkIfServiceHasAcceptedOffer();
        return view;
    }

    private void checkIfServiceHasAcceptedOffer() {
        FirebaseDatabase.getInstance().getReference().child("ServicePetitions").child(petitionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ServicePetition tempService = dataSnapshot.getValue(ServicePetition.class);
                if(!tempService.getAcceptedOfferId().equals("")) {
                    endWorkHint.setVisibility(View.VISIBLE);
                    btnEndWork.setVisibility(View.VISIBLE);
                }

                if (dataSnapshot.getValue(ServicePetition.class).getFinished()) {
                    endWorkHint.setText("Ya acabó la contratación de este servicio");
                    btnEndWork.setEnabled(false);
                    btnEndWork.setBackgroundResource(R.drawable.rect_disabled);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getPetitionId() {
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        petitionId = sharedPreferences.getString("servicePetitionId", "");
    }

    private void chargePetition() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        ref.child(petitionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                servicePetition = dataSnapshot.getValue(ServicePetition.class);
                isModifiable();
                chargeServiceType();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chargeServiceType() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ServiceTypes");
        ref.child(servicePetition.getServiceTypeId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceType = dataSnapshot.getValue(ServiceType.class);
                setContent();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initContent() {
        carrousellViewPager = view.findViewById(R.id.carrousellViewPager);
        circleIndicator = view.findViewById(R.id.circleIndicator);
        petitionServiceType = view.findViewById(R.id.petitionServiceType);
        petitionName = view.findViewById(R.id.petitionName);
        petitionDescription = view.findViewById(R.id.petitionDescription);
        btnPetitionUpdate = view.findViewById(R.id.btnPetitionUpdate);
        modifyLayout = view.findViewById(R.id.modifyLayout);

        endWorkHint = view.findViewById(R.id.endPetitionWorkHint);
        btnEndWork = view.findViewById(R.id.btnEndPetitionWork);
    }

    private void setContent() {
        setCarousel();
        petitionName.setText(servicePetition.getName());
        petitionServiceType.setText(serviceType.getServiceType());
        petitionDescription.setText(servicePetition.getDescription());
        btnPetitionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPetitionUpdate();
            }
        });
    }

    private void setCarousel() {
        List<String> images = new ArrayList<>();
        if (!(servicePetition.getFiles() == null)) {
            carousel_adapter = new Carousel_Adapter(getChildFragmentManager(), servicePetition.getFiles());
            carrousellViewPager.setAdapter(carousel_adapter);
            circleIndicator.setViewPager(carrousellViewPager);
        } else {
            images.add(serviceType.getImgUrl());
            carousel_adapter = new Carousel_Adapter(getChildFragmentManager(), images);
            carrousellViewPager.setAdapter(carousel_adapter);
            carrousellViewPager.setBackgroundColor(Color.parseColor(serviceType.getColor()));
            circleIndicator.setVisibility(View.GONE);
        }
        circleIndicator.setViewPager(carrousellViewPager);
    }

    public void goToPetitionUpdate() {
        Intent intent = new Intent(getActivity(), ServicePetitionUpdateActivity.class);
        startActivity(intent);
    }

    private void isModifiable() {
        DatabaseReference offerRef = FirebaseDatabase.getInstance().getReference("Offers");
        Query offersQuery = offerRef.orderByChild("servicePetitionId").equalTo(servicePetition.getId());

        offersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    modifyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
