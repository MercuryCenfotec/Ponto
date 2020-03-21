package com.cenfotec.ponto.entities.petitioner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.cenfotec.ponto.LoginActivity;
import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Petitioner;
import com.cenfotec.ponto.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Scanner;

import adapter.ProfileAdapter;
import model.ProfileModel;

public class PetitionerProfileActivity extends AppCompatActivity {

    private static SharedPreferences sharedpreferences;
    private String activeUserId;
    TextView profilePetitionerFullName;
//    TextView profilePetitionerBirthDate;
    TextView profilePetitionerEmail;
//    TextView profilePetitionerIdentification;
    TextView profilePetitionerRating;
    User user;

    private ProfileAdapter profileAdapter;
    private RecyclerView recyclerview;
    private ArrayList<ProfileModel> profileModelArrayList;

    Integer inbox[] = {R.drawable.ic_calendar,R.drawable.ic_like, R.drawable.ic_star, R.drawable.ic_contract, R.drawable.ic_profile,R.drawable.ic_settings};
    Integer arrow = R.drawable.ic_chevron_right_black_24dp;
    String txttrades[] = {"Agenda de proyectos", "Recomendaciones", "Reseñas", "Contratos", "Mi Perfil", "Ajustes"};
    String txthistory[] = {"Revisá tus contrataciones", "Administrá tus recomendaciones", "Tu colección", "Tu colección", "Cambiá la información de tu perfil", "Ajustes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petitioner_profile);

        getActiveUserId();
        initProfileControls();
        getPetitionerByActiveUserId();
        showRecyclerViewOptions();
    }

    private void showRecyclerViewOptions() {
        recyclerview = findViewById(R.id.profileOptionsPetitioner);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        profileModelArrayList = new ArrayList<>();

        for (int i = 0; i < inbox.length; i++) {
            ProfileModel view = new ProfileModel(inbox[i], arrow, txttrades[i], txthistory[i], activeUserId, "petitioner");
            profileModelArrayList.add(view);
        }

        profileAdapter = new ProfileAdapter(this, profileModelArrayList);
        recyclerview.setAdapter(profileAdapter);
    }

    private void initProfileControls() {
        profilePetitionerFullName = findViewById(R.id.petitionerFullNameProfile);
//        profilePetitionerBirthDate = findViewById(R.id.petitionerBirthdateProfile);
        profilePetitionerEmail = findViewById(R.id.petitionerMailProfile);
//        profilePetitionerIdentification = findViewById(R.id.petitionerIdentificationProfile);
        profilePetitionerRating = findViewById(R.id.petitionerRatingProfile);
        user = new User();
    }

    private void getActiveUserId() {
        sharedpreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        activeUserId = sharedpreferences.getString("userId", "");
    }

    private void getPetitionerByActiveUserId() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(activeUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                showPetitionerProfileInformation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showPetitionerProfileInformation() {

        // Convert first letter to capital
        StringBuilder capitalized = new StringBuilder();
        Scanner lineScan = new Scanner(user.getFullName().toLowerCase());
        while(lineScan.hasNext()) {
            String word = lineScan.next();
            capitalized.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }

        profilePetitionerFullName.setText(capitalized);
//        profilePetitionerBirthDate.setText(petitioner.getBirthDate());
        profilePetitionerEmail.setText(user.getEmail());
//        profilePetitionerIdentification.setText(petitioner.getIdentificationNumber());
        profilePetitionerRating.setText(String.valueOf(user.getRating()));
    }
}
