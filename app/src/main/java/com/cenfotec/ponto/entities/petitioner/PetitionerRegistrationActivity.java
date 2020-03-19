package com.cenfotec.ponto.entities.petitioner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cenfotec.ponto.R;

public class PetitionerRegistrationActivity extends AppCompatActivity {

    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petitioner_registration);

        findViews();
    }

    private void findViews() {
        passwordEditText = findViewById(R.id.txtPassword);
    }

    public void showHidePass(View view) {
        if (view.getId() == R.id.imgViewPassword) {
            if(passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_hide);
                //Show Password
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye);
                //Hide Password
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}
