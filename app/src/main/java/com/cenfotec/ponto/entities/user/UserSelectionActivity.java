package com.cenfotec.ponto.entities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;
import com.cenfotec.ponto.entities.petitioner.PetitionerRegistrationActivity;

public class UserSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
    }

    public void goToBidderRegistration(View view) {
        Intent intent = new Intent(this, BidderRegistrationActivity.class);
        startActivity(intent);
    }

    public void goToPetitionerRegistration(View view) {
        Intent intent = new Intent(this, PetitionerRegistrationActivity.class);
        startActivity(intent);
    }
}
