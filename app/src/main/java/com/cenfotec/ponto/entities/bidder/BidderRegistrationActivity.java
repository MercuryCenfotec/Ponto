package com.cenfotec.ponto.entities.bidder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.BCrypt;
import com.cenfotec.ponto.data.model.Bidder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BidderRegistrationActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText fullNameEditText;
    EditText birthDateEditText;
    EditText emailEditText;
    EditText identificationEditText;
    EditText passwordEditText;
    EditText biographyEditText;
    Button btnBidderRegistration;
    TextInputLayout fullNameInputLayout;
    TextInputLayout birthDateInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout identificationInputLayout;
    TextInputLayout passwordInputLayout;
    TextInputLayout biographyInputLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_registration);
        initFormControls();
        initBidderRegistrationButtonListener();
    }

    private void initFormControls() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Bidders");
        fullNameEditText = findViewById(R.id.fullNameEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        emailEditText = findViewById(R.id.emailEditText);
        identificationEditText = findViewById(R.id.identificationEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        biographyEditText = findViewById(R.id.biographyEditText);
        btnBidderRegistration = findViewById(R.id.btnBidderRegistration);
        fullNameInputLayout = findViewById(R.id.fullNameInputLayout);
        birthDateInputLayout = findViewById(R.id.birthDateInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        identificationInputLayout = findViewById(R.id.identificationInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        biographyInputLayout = findViewById(R.id.biographyInputLayout);
    }

    private void initBidderRegistrationButtonListener() {
        btnBidderRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerBidderToDB();
            }
        });
    }

    //create statements start here
    private void registerBidderToDB() {
        if (showErrorOnBlankSpaces() == false) {
            FirebaseDatabase.getInstance().getReference().child("Bidders")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean found = false;
                    for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                        if (identificationEditText.getText().toString().equals(bidderSnapshot.child("identificationNumber").getValue().toString())) {
                            found = true;
                            showToaster("Identificación ya");
                        } else if (emailEditText.getText().toString().equals(bidderSnapshot.child("email").getValue().toString())) {
                            found = true;
                            showToaster("Email ya");
                        }
                    }

                    if (found == false) {
                        String password = passwordEditText.getText().toString();
                        String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));

                        String id = databaseReference.push().getKey();
                        Bidder bidder = new Bidder(id, fullNameEditText.getText().toString(),
                                birthDateEditText.getText().toString(),
                                emailEditText.getText().toString(),
                                identificationEditText.getText().toString(), generatedSecuredPasswordHash,
                                biographyEditText.getText().toString(), 1);
                        databaseReference.child(id).setValue(bidder);

                        clearBidderRegistrationInputs();

                        showToaster("Oferente agregado");
                        initBidderProfileView(id);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        } else {
            showToaster("Error");
        }
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void initBidderProfileView(String id) {
        Intent intent = new Intent(BidderRegistrationActivity.this, BidderProfileActivity.class);
        intent.putExtra("token", id);
        startActivity(intent);
    }

    //validation statements start here
    private void clearBidderRegistrationInputs() {
        fullNameEditText.setText("");
        birthDateEditText.setText("");
        emailEditText.setText("");
        identificationEditText.setText("");
        passwordEditText.setText("");
        biographyEditText.setText("");
    }

    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;

        //This can be an array or a list
        if (fullNameEditText.getText().toString().equals("")) {
            fullNameInputLayout.setError("Campo requerido");
            isEmpty = true;
        } else {
            fullNameInputLayout.setError(null);
        }
        if (birthDateEditText.getText().toString().equals("")) {
            birthDateInputLayout.setError("Campo requerido");
            isEmpty = true;
        } else {
            birthDateInputLayout.setError(null);
        }
        if (emailEditText.getText().toString().equals("")) {
            emailInputLayout.setError("Campo requerido");
            isEmpty = true;
        } else {
            emailInputLayout.setError(null);
        }
        if (identificationEditText.getText().toString().equals("")) {
            identificationInputLayout.setError("Campo requerido");
            isEmpty = true;
        } else {
            identificationInputLayout.setError(null);
        }
        if (passwordEditText.getText().toString().equals("")) {
            passwordInputLayout.setError("Campo requerido");
            isEmpty = true;
        } else {
            passwordInputLayout.setError(null);
        }
        if (biographyEditText.getText().toString().equals("")) {
            biographyInputLayout.setError("Campo requerido");
            isEmpty = true;
        } else {
            biographyInputLayout.setError(null);
        }

        return isEmpty;

    }

}
