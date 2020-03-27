package com.cenfotec.ponto.entities.servicePetition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.offer.OfferCreationActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Semibold;
import customfonts.TextViewSFProDisplayRegular;

public class ServicePetitionDetailActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    private String petitionId;
    SharedPreferences sharedPreferences;
    ImageView petitionImage;
    TextViewSFProDisplayRegular petitionServiceType;
    MyTextView_SF_Pro_Display_Bold petitionName;
    TextViewSFProDisplayRegular petitionDescription;
    MyTextView_SF_Pro_Display_Semibold btnOfferCreation;
    ServicePetition servicePetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_detail);
        initContent();
        getPetitionId();
        chargePetition();
        setContent();
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
                setContent();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initContent() {
        petitionImage =  findViewById(R.id.petitionImage);
        petitionServiceType =  findViewById(R.id.petitionServiceType);
        petitionName =  findViewById(R.id.petitionName);
        petitionDescription =  findViewById(R.id.petitionDescription);
        btnOfferCreation =  findViewById(R.id.btnOfferCreation);
    }

    private void setContent() {

        btnOfferCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOfferCreation();
            }
        });
        petitionName.setText(servicePetition.getName());
        petitionServiceType.setText(servicePetition.getServiceTypeId());
        petitionDescription.setText(servicePetition.getDescription());
    }

    public void goToOfferCreation(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("servicePetitionTitle", servicePetition.getName());
        editor.commit();

        Intent intent = new Intent(this, OfferCreationActivity.class);
        startActivity(intent);
    }

}
