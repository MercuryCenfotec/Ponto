package com.cenfotec.ponto.entities.servicePetition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import adapter.Carousel_Adapter;
import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Semibold;
import customfonts.TextViewSFProDisplayRegular;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class ServicePetitionPetitionerDetailActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    private String petitionId;
    SharedPreferences sharedPreferences;
    ImageView returnIcon;
    Carousel_Adapter carousel_adapter;
    ViewPager carrousellViewPager;
    CircleIndicator circleIndicator;
    TextViewSFProDisplayRegular petitionServiceType;
    MyTextView_SF_Pro_Display_Bold petitionName;
    TextViewSFProDisplayRegular petitionDescription;
    MyTextView_SF_Pro_Display_Semibold btnPetitionUpdate;
    ServicePetition servicePetition;
    ServiceType serviceType;

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
        petitionServiceType =  findViewById(R.id.petitionServiceType);
        petitionName =  findViewById(R.id.petitionName);
        petitionDescription =  findViewById(R.id.petitionDescription);
        btnPetitionUpdate =  findViewById(R.id.btnOfferCreation);
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
        btnPetitionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPetitionUpdate();
            }
        });
    }

    private void setCarousel(){
        if(!(servicePetition.getFiles() == null)) {
            if(!servicePetition.getFiles().isEmpty()) {
                carousel_adapter = new Carousel_Adapter(getSupportFragmentManager(), servicePetition.getFiles());
                carrousellViewPager.setAdapter(carousel_adapter);
                circleIndicator.setViewPager(carrousellViewPager);
            }
        }
    }
    private void goToHomeBidder() {
        Intent intent = new Intent(this, BidderHomeActivity.class);
        startActivity(intent);
    }

    public void goToPetitionUpdate(){
        Intent intent = new Intent(this, ServicePetitionUpdateActivity.class);
        startActivity(intent);
    }
}
