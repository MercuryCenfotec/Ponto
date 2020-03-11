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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BidderRegistrationActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText fullNameEditText;
    EditText birthDateEditText;
    EditText emailEditText;
    EditText identificationEditText;
    EditText passwordEditText;
    EditText biographyEditText;
    Button btnBidderRegistration;

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
    }

    private void initBidderRegistrationButtonListener() {
        btnBidderRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerBidderToDB();
            }
        });
    }

    private void registerBidderToDB() {
        String password = passwordEditText.getText().toString();
        String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));

        String id = databaseReference.push().getKey();
        Bidder bidder = new Bidder(id, fullNameEditText.getText().toString(),
                birthDateEditText.getText().toString(),
                emailEditText.getText().toString(),
                identificationEditText.getText().toString(), generatedSecuredPasswordHash,
                biographyEditText.getText().toString(), 1);
        databaseReference.child(id).setValue(bidder);

        fullNameEditText.setText("");
        birthDateEditText.setText("");
        emailEditText.setText("");
        identificationEditText.setText("");
        passwordEditText.setText("");
        biographyEditText.setText("");

        Toast.makeText(this, "Bidder added", Toast.LENGTH_LONG).show();
        initBidderProfileView(id);
    }

    private void initBidderProfileView(String id) {
        Intent intent = new Intent(BidderRegistrationActivity.this, BidderProfileActivity.class);
        intent.putExtra("token", id);
        startActivity(intent);
    }
}
