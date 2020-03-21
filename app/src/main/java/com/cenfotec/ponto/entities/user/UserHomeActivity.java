package com.cenfotec.ponto.entities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.cenfotec.ponto.entities.petitioner.PetitionerHomeActivity;

public class UserHomeActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        SharedPreferences myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String userType = myPrefs.getString("userType", "none");
        Intent intent;

        switch (userType) {
            case "bidder":
                intent = new Intent(this, BidderHomeActivity.class);
                startActivity(intent);
                break;
            case "petitioner":
                intent = new Intent(this, PetitionerHomeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
