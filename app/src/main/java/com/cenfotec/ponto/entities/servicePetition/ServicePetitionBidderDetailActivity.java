package com.cenfotec.ponto.entities.servicePetition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.cenfotec.ponto.entities.offer.OfferCreationActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapter.Carousel_Adapter;
import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Semibold;
import customfonts.TextViewSFProDisplayRegular;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class ServicePetitionBidderDetailActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    private String petitionId;
    SharedPreferences sharedPreferences;
    ImageView returnIcon;
    Carousel_Adapter carousel_adapter;
    ViewPager carrousellViewPager;
    CircleIndicator circleIndicator;
    CircleImageView petitionUserImage;
    TextViewSFProDisplayRegular petitionServiceType;
    MyTextView_SF_Pro_Display_Bold petitionName;
    TextViewSFProDisplayRegular petitionDescription;
    MyTextView_SF_Pro_Display_Semibold btnOfferCreation;
    MyTextView_SF_Pro_Display_Semibold petitionUserName;
    ServicePetition servicePetition;
    ServiceType serviceType;
    User petitionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_bidder_detail);
        initContent();
        getPetitionId();
        chargePetition();
    }


    private void getPetitionId() {
        sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        petitionId = sharedPreferences.getString("servicePetitionId", "");
    }

    private void chargePetition() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        ref.child(petitionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                servicePetition = dataSnapshot.getValue(ServicePetition.class);
                chargeUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chargeUser() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(servicePetition.getPetitionerId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                petitionUser = dataSnapshot.getValue(User.class);
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
        returnIcon = findViewById(R.id.returnIcon);
        carrousellViewPager = findViewById(R.id.carrousellViewPager);
        circleIndicator = findViewById(R.id.circleIndicator);
        petitionServiceType = findViewById(R.id.petitionServiceType);
        petitionName = findViewById(R.id.petitionName);
        petitionDescription = findViewById(R.id.petitionDescription);
        btnOfferCreation = findViewById(R.id.btnOfferCreation);
        petitionUserImage = findViewById(R.id.petitionUserImage);
        petitionUserName = findViewById(R.id.petitionUserName);
    }

    private void setContent() {

        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomeBidder();
            }
        });
        setCarousel();
        petitionName.setText(servicePetition.getName());
        petitionServiceType.setText(serviceType.getServiceType());
        petitionDescription.setText(servicePetition.getDescription());
        if (!petitionUser.getProfileImageUrl().equals("")) {
            Picasso.get().load(petitionUser.getProfileImageUrl()).into(petitionUserImage);
        }
        petitionUserName.setText(petitionUser.getFullName());
        btnOfferCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOfferCreation();
            }
        });

        DatabaseReference offersRef = FirebaseDatabase.getInstance().getReference("Offers");
        Query offersQuery = offersRef.orderByChild("servicePetitionId").equalTo(servicePetition.getId());
        offersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userId = sharedPreferences.getString("userId","none");
                for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()) {
                    if (offerSnapshot.child("userId").getValue().toString().equals(userId) ) {
                        btnOfferCreation.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setCarousel() {
        List<String> images = new ArrayList<>();
        if (!(servicePetition.getFiles() == null)) {
            carousel_adapter = new Carousel_Adapter(getSupportFragmentManager(), servicePetition.getFiles());
        } else {
            images.add(serviceType.getImgUrl());
            carousel_adapter = new Carousel_Adapter(getSupportFragmentManager(), images);
            carrousellViewPager.setBackgroundColor(Color.parseColor(serviceType.getColor()));
            circleIndicator.setVisibility(View.GONE);
        }
        carrousellViewPager.setAdapter(carousel_adapter);
        circleIndicator.setViewPager(carrousellViewPager);

    }

    private void goToHomeBidder() {
        Intent intent = new Intent(this, BidderHomeActivity.class);
        startActivity(intent);
    }

    public void goToOfferCreation() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("servicePetitionTitle", servicePetition.getName());
        editor.commit();

        Intent intent = new Intent(this, OfferCreationActivity.class);
        startActivity(intent);
    }

}
