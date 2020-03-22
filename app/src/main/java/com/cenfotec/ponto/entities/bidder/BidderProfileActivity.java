package com.cenfotec.ponto.entities.bidder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cenfotec.ponto.entities.user.LoginActivity;
import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.cenfotec.ponto.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Scanner;

import adapter.ProfileAdapter;
import model.ProfileModel;

public class BidderProfileActivity extends AppCompatActivity {

    private static SharedPreferences sharedpreferences;
    private String activeUserId;
    private ProfileAdapter profileAdapter;
    private RecyclerView recyclerview;
    private ArrayList<ProfileModel> profileModelArrayList;
    User user;
    Integer inbox[] = {R.drawable.ic_calendar,R.drawable.ic_like, R.drawable.ic_star,
            R.drawable.ic_contract, R.drawable.ic_profile,R.drawable.ic_settings};
    Integer arrow = R.drawable.ic_chevron_right_black_24dp;
    String txttrades[] = {"Agenda de proyectos", "Recomendaciones", "Reseñas", "Contratos",
            "Mi Perfil", "Ajustes"};
    String txthistory[] = {"Revisá tus contrataciones", "Administrá tus recomendaciones",
            "Tu colección", "Tu colección", "Cambiá la información de tu perfil", "Ajustes"};
    TextView profileFullName;
    TextView profileEmail;
    TextView profileRating;
    TextView profileBiography;
    ImageView profileImage;
    Bidder bidder;
    CustomDatePickerDialog customDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_profile);
        initProfileControls();
        getActiveUserId();
        getUserByActiveUserId();
        showRecyclerViewOptions();
    }

    private void showRecyclerViewOptions() {
        recyclerview = findViewById(R.id.bidderProfileOptions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        profileModelArrayList = new ArrayList<>();

        for (int i = 0; i < inbox.length; i++) {
            ProfileModel view = new ProfileModel(inbox[i], arrow, txttrades[i], txthistory[i],
                    activeUserId,"bidder");
            profileModelArrayList.add(view);
        }

        profileAdapter = new ProfileAdapter(this, profileModelArrayList);
        recyclerview.setAdapter(profileAdapter);
    }

    private void getActiveUserId() {
        sharedpreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        activeUserId = sharedpreferences.getString("userId", "");
    }

    private void getUserByActiveUserId() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(activeUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                getBidderByUserId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initProfileControls() {
        profileFullName = findViewById(R.id.bidderFullNameProfile);
        profileEmail = findViewById(R.id.bidderMailProfile);
        profileRating = findViewById(R.id.bidderRatingProfile);
        profileBiography = findViewById(R.id.bidderBiographyProfile);
        profileImage = findViewById(R.id.imgBidderProfile);
        bidder = new Bidder();
        user = new User();
        customDatePickerDialog = new CustomDatePickerDialog();
    }

    private void getBidderByUserId() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByIdQuery =
                databaseReference.child("Bidders").orderByChild("userId").equalTo(activeUserId);
        getBidderByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    bidder = bidderSnapshot.getValue(Bidder.class);
                    showBidderProfileInformation();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void showBidderProfileInformation() {
        // Convert first letter to capital
        StringBuilder capitalized = new StringBuilder();
        Scanner lineScan = new Scanner(user.getFullName().toLowerCase());
        while(lineScan.hasNext()) {
            String word = lineScan.next();
            capitalized.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }

        if(!user.getProfileImageUrl().equals("")){
            Picasso.get().load(user.getProfileImageUrl()).into(profileImage);
        }
        profileFullName.setText(capitalized);
        profileEmail.setText(user.getEmail());
        profileRating.setText(String.valueOf(user.getRating()));
        profileBiography.setText(bidder.getBiography());
    }
}
