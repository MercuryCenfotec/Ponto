package com.cenfotec.ponto.entities.user;

import androidx.appcompat.app.AppCompatActivity;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.utils.LogoutHelper;

import android.os.Bundle;
import android.view.View;

public class UnverifiedUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unverified_user);
    }

    public void logout(View view) {
        LogoutHelper.logout(this);
        finish();
    }
}
