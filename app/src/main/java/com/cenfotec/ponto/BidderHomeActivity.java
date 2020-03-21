package com.cenfotec.ponto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;

public class BidderHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_home);
    }

    public void goToBidderProfile(View view) {
        Intent intent = new Intent(this, BidderProfileActivity.class);
        startActivity(intent);
    }

    public void goToBidderRegister(View view) {
        Intent intent = new Intent(this, BidderRegistrationActivity.class);
        startActivity(intent);
    }
}
