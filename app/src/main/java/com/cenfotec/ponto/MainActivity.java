package com.cenfotec.ponto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.cenfotec.ponto.entities.user.UserHomeActivity;

public class MainActivity extends AppCompatActivity {

import android.view.View;
import android.widget.Button;

import com.cenfotec.ponto.entities.servicePetition.ServicePetitionListActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.cenfotec.ponto.entities.user.UserHomeActivity;

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFERENCES = "MyPrefs";
    Button btnPetitionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPetitionList = findViewById(R.id.btnPetitionList);
        SharedPreferences myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String userId = myPrefs.getString("userId", "none");
        Intent intent;

        btnPetitionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewList();
            }
        });


        if (userId.equals("none")) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, UserHomeActivity.class);
            startActivity(intent);
        }

    }

    private void viewList() {
        Intent intent = new Intent(this, ServicePetitionListActivity.class);
        startActivity(intent);
    }
}
