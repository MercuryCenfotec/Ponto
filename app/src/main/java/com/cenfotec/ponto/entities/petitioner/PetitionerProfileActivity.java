package com.cenfotec.ponto.entities.petitioner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Petitioner;
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

    String intentToken;
    TextView profilePetitionerFullName;
//    TextView profilePetitionerBirthDate;
    TextView profilePetitionerEmail;
//    TextView profilePetitionerIdentification;
    TextView profilePetitionerRating;
    Petitioner petitioner;
    String petitionerId;

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

        catchIntentContent();
        initProfileControls();
        getPetitionerByIntentToken();
        showRecyclerViewOptions();
    }

    private void showRecyclerViewOptions() {
        recyclerview = findViewById(R.id.profileOptionsPetitioner);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        profileModelArrayList = new ArrayList<>();

        for (int i = 0; i < inbox.length; i++) {
            ProfileModel view = new ProfileModel(inbox[i], arrow, txttrades[i], txthistory[i], petitionerId);
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
        petitioner = new Petitioner();
    }

    private void catchIntentContent() {
        Intent intent = getIntent();
        intentToken = intent.getExtras().getString("token");
        petitionerId = intentToken;
    }

    private void getPetitionerByIntentToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getPetitionerByIdQuery = databaseReference.child("Users").orderByChild("id").equalTo(intentToken);
        getPetitionerByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot petitionerSnapshot : dataSnapshot.getChildren()) {
                    petitioner = petitionerSnapshot.getValue(Petitioner.class);
                    showPetitionerProfileInformation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showPetitionerProfileInformation() {

        // Convert first letter to capital
        StringBuilder capitalized = new StringBuilder();
        Scanner lineScan = new Scanner(petitioner.getFullName().toLowerCase());
        while(lineScan.hasNext()) {
            String word = lineScan.next();
            capitalized.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }

        profilePetitionerFullName.setText(capitalized);
//        profilePetitionerBirthDate.setText(petitioner.getBirthDate());
        profilePetitionerEmail.setText(petitioner.getEmail());
//        profilePetitionerIdentification.setText(petitioner.getIdentificationNumber());
        profilePetitionerRating.setText(String.valueOf(petitioner.getRating()));
    }
}
