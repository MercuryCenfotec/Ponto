package com.cenfotec.ponto.entities.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cenfotec.ponto.R;

import customfonts.EditText__SF_Pro_Display_Medium;

public class FetchBalanceFromAccountActivity extends AppCompatActivity {

    String code = "KS - WI - PS";
    EditText__SF_Pro_Display_Medium firstCodeSection;
    EditText__SF_Pro_Display_Medium secondCodeSection;
    EditText__SF_Pro_Display_Medium thirdCodeSection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_balance_from_account);
        firstCodeSection = findViewById(R.id.cbeejktwekjt);
        secondCodeSection = findViewById(R.id.dhgrtoiyth);
        thirdCodeSection = findViewById(R.id.ncdfytuyioi);

    }

    public void confirmFetch(View view) {
        if () {

        }
    }
}
