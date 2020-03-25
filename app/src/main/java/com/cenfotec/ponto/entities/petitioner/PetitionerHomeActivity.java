package com.cenfotec.ponto.entities.petitioner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.petitioner.PetitionerProfileActivity;
import com.cenfotec.ponto.entities.petitioner.PetitionerRegistrationActivity;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionCreationActivity;
import com.cenfotec.ponto.utils.LogoutHelper;

public class PetitionerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petitioner_home);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(this, PetitionerProfileActivity.class);
        startActivity(intent);
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, PetitionerRegistrationActivity.class);
        startActivity(intent);
    }

    public void goToServicePetition(View view){
        Intent intent = new Intent(this, ServicePetitionCreationActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        LogoutHelper.logout(this);
    }
}
