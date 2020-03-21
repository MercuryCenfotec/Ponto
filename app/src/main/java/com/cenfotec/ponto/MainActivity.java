package com.cenfotec.ponto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.cenfotec.ponto.entities.user.LoginActivity;
import com.cenfotec.ponto.entities.user.UserHomeActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSwapToBidderRegistration;
    Button btnPetitioner;
    public static final String MY_PREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String userId = myPrefs.getString("userId", "none");
        Intent intent;

        if (userId.equals("none")) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, UserHomeActivity.class);
            startActivity(intent);
        }

    }
}
