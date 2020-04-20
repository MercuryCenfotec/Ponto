package com.cenfotec.ponto.entities.servicePetition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Account;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.data.model.Rating;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.petitioner.PetitionerHomeActivity;
import com.cenfotec.ponto.entities.rating.RateUserDialog;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import adapter.TabLayoutAdapter_ServicePetitionDetailPetitioner;

public class ServicePetitionPetitionerDetailActivity extends AppCompatActivity implements EndWorkDialog.EndWorkDialogListener, RateUserDialog.RateUserDialogConfirmListener {

    ImageView returnIcon;
    ViewPager petitionViewPager;
    TabLayout tabLayout;
    TabLayoutAdapter_ServicePetitionDetailPetitioner adapter;

    private static final String MY_PREFERENCES = "MyPrefs";
    private DatabaseReference accountDBReference;
    private DatabaseReference servicePetitionBReference;
    private DatabaseReference ratingDBReference;
    private DatabaseReference userDBReference;
    private static User bidder;
    private static Account bidderUserAccount;
    private static Offer acceptedOffer;
    private String userId;
    private ArrayList<Rating> bidderRatings;

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

        bidderRatings = new ArrayList<>();

        SharedPreferences myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        userId = myPrefs.getString("userId", "none");
        accountDBReference = FirebaseDatabase.getInstance().getReference("Accounts");
        servicePetitionBReference = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        ratingDBReference = FirebaseDatabase.getInstance().getReference("Ratings");
        userDBReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    private void setContent() {
        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomePetitioner();
            }
        });
        adapter = new TabLayoutAdapter_ServicePetitionDetailPetitioner(getSupportFragmentManager(), tabLayout.getTabCount());
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

    static void getOffer() {
        final DatabaseReference offerDBReference = FirebaseDatabase.getInstance().getReference();
        Query getOfferQuery = offerDBReference.child("Offers").orderByChild("id").equalTo(ServicePetitionDetail.servicePetition.getAcceptedOfferId());
        getOfferQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()) {
                    acceptedOffer = offerSnapshot.getValue(Offer.class);
                    getBidderUser(acceptedOffer.getUserId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void getBidderUser(String userId) {

        final DatabaseReference userDBReference = FirebaseDatabase.getInstance().getReference();
        Query getUserByIdQuery = userDBReference.child("Users").orderByChild("id").equalTo(userId);
        getUserByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bidder = snapshot.getValue(User.class);
                    getUserAccount(bidder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void getUserAccount(User activeUser) {
        final DatabaseReference accountDBReference = FirebaseDatabase.getInstance().getReference();
        Query getUserAccountQuery = accountDBReference.child("Accounts").orderByChild("accountNumber").equalTo(activeUser.getUserAccount());
        getUserAccountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                    bidderUserAccount = accountSnapshot.getValue(Account.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openEndWorkDialog(View view) {
        getOffer();

        EndWorkDialog endWorkDialog = new EndWorkDialog();
        endWorkDialog.show(getSupportFragmentManager(), "end work dialog");
    }

    @Override
    public void dialogEndWork() {
        accountDBReference.child(bidderUserAccount.getAccountNumber()).child("balance").setValue(bidderUserAccount.getBalance() + acceptedOffer.getCost());
        servicePetitionBReference.child(ServicePetitionDetail.servicePetition.getId()).child("finished").setValue(true);

        adapter = new TabLayoutAdapter_ServicePetitionDetailPetitioner(getSupportFragmentManager(), tabLayout.getTabCount());
        petitionViewPager.setAdapter(adapter);

        openRatingDialog();
    }

    // Método que abre el dialog para calificar al usuario
    private void openRatingDialog() {
        RateUserDialog rateUserDialog = new RateUserDialog();
        rateUserDialog.setCancelable(false);
        rateUserDialog.show(getSupportFragmentManager(), "rate user dialog");

        getAllRatingsByUser();
    }

    private void getAllRatingsByUser() {

        ratingDBReference.orderByChild("userId").equalTo(bidder.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bidderRatings.add(snapshot.getValue(Rating.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Implementar RateUserDialogConfirmListener y sobreescribir el método para manejar el click de Calificar dentro del dialogo
    @Override
    public void dialogConfirmRating(float rating) {

        int totalRating = 0;
        int numRatings = bidderRatings.size();

        // Según el total de rating que ha tenido el usuario a calificar, sumar la totalidad de las calificaciones
        for (int i = 0; i < bidderRatings.size(); i++) {
            totalRating += bidderRatings.get(i).getRating();
        }

        // Guardar en la base de datos en nuevo rating
        String ratingId = ratingDBReference.push().getKey();
        Rating myRating = new Rating(ratingId, bidder.getId(), userId, rating);
        ratingDBReference.child(ratingId).setValue(myRating);

        DecimalFormat df = new DecimalFormat("###.##");

        // Actualizar el rating del usuario tomando la totalidad que se sumó arriba (además del rating nuevo) y dividirla por la cantidad total de ratings que tiene el user
        Float newRating = (totalRating + rating) / (numRatings + 1);
        userDBReference.child(bidder.getId()).child("rating").setValue(Float.parseFloat(df.format(newRating)));

    }
}