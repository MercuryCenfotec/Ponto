package com.cenfotec.ponto.entities.bidder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;
import com.cenfotec.ponto.utils.LogoutHelper;

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

<<<<<<< Updated upstream
=======
    public void goToPetitionCreation(View view) {
        Intent intent = new Intent(this, ServicePetitionCreationActivity.class);
        startActivity(intent);
    }

    public void goToPetitionList(View view) {
        Intent intent = new Intent(this, ServicePetitionListActivity.class);
        startActivity(intent);
    }

    public void goToOfferCreation(View view) {
        Intent intent = new Intent(this, OfferCreationActivity.class);
        startActivity(intent);
    }

    public void goToOfferDetail(View view) {
        Intent intent = new Intent(this, OfferDetailActivity.class);
        startActivity(intent);
    }

>>>>>>> Stashed changes
    public void logout(View view) {
        LogoutHelper.logout(this);
    }
}
