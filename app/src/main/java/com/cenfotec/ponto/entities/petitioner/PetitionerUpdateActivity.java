package com.cenfotec.ponto.entities.petitioner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Petitioner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Scanner;

public class PetitionerUpdateActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText identificationEditText;
    EditText fullNameEditText;
    EditText birthDateEditText;
    EditText emailEditText;
    EditText passwordEditText;
    Petitioner updatedPet;

    String userId;

    String fullname;
    String identification;
    String birthdate;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petitioner_update);

        findViews();
        getPetitionerByIntentToken();
    }

    private void findViews() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        fullNameEditText = findViewById(R.id.fullNameUpdPetEditText);
        birthDateEditText = findViewById(R.id.birthDateUpdPetEditText);
        emailEditText = findViewById(R.id.emailUpdPetEditText);
        identificationEditText = findViewById(R.id.idUpdPetEditText);

        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("petitionerId");
        }
    }

    private void fillUpdateFields() {
        fullNameEditText.setText(updatedPet.getFullName());
        birthDateEditText.setText(updatedPet.getBirthDate());
        emailEditText.setText(updatedPet.getEmail());
        identificationEditText.setText(updatedPet.getIdentificationNumber());
    }

    private void getPetitionerByIntentToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getPetitionerByIdQuery = databaseReference.child("Users").orderByChild("id").equalTo(userId);
        getPetitionerByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot petitionerSnapshot : dataSnapshot.getChildren()) {
                    updatedPet = petitionerSnapshot.getValue(Petitioner.class);
                    fillUpdateFields();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updatePetitioner(View view) {
        if (view.getId() == R.id.btnRegisterPetitioner) {

            if (userId != null) {
                final DatabaseReference[] ref = {FirebaseDatabase.getInstance().getReference("Users")};
                ref[0].child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            updatedPet.setFullName(fullNameEditText.getText().toString());
                            updatedPet.setEmail(emailEditText.getText().toString());

                            ref[0].child(userId).setValue(updatedPet);
                            showToaster("Actualización exitosa");
                            openPetitionerProfile(userId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBack(View view) {
        finish();
    }

    private void openPetitionerProfile(String id) {
        Intent intent = new Intent(PetitionerUpdateActivity.this, PetitionerProfileActivity.class);
        intent.putExtra("token", id);
        startActivity(intent);
    }
}
