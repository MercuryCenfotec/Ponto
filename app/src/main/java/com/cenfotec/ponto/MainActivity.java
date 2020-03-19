package com.cenfotec.ponto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;
import com.cenfotec.ponto.entities.petitioner.PetitionerRegistrationActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSwapToBidderRegistration;
    Button btnPetitioner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSwapToBidderRegistration = findViewById(R.id.btnSwapToBidderRegistration);
        btnSwapToBidderRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBidderRegistrationView();
            }
        });

        btnPetitioner = findViewById(R.id.btnSolicitante);
        btnPetitioner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openPetitionerRegistrationView();
            }
        });
    }

    private void openBidderRegistrationView(){
        Intent intent = new Intent(this, BidderRegistrationActivity.class);
        startActivity(intent);
    }

    private void openPetitionerRegistrationView(){
        Intent i = new Intent(this, PetitionerRegistrationActivity.class);
        this.startActivity(i);
    }
}
