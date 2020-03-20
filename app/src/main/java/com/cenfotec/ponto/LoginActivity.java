package com.cenfotec.ponto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.data.model.BCrypt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    public EditText emailEditText;
    public EditText passwordEditText;
    public TextView loginButton;
    public TextView registerButton;
    SharedPreferences sharedPreferences;
    public boolean showPassword = false;
    public ImageView eyeIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        eyeIcon = findViewById(R.id.eyeIcon);
    }

    public void login(View view){
        if (validForm()) {
            final String email = emailEditText.getText().toString();
            final String password = passwordEditText.getText().toString();
            final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
            Query petitionersQuery = dbReference.child("Petitioners").orderByChild("email").equalTo(email);
            Query biddersQuery = dbReference.child("Bidders").orderByChild("email").equalTo(email);

            petitionersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (BCrypt.checkpw(password, data.child("password").getValue().toString())) {
                            editor.putString("userId", data.child("id").getValue().toString());
                            editor.putString("userType", "petitioner");
                            editor.commit();
                            showToaster("Hola, " + data.child("fullName").getValue().toString());
                            Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                            startActivity(intent);
                        } else {
                            showToaster("Datos incorrectos");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The petitioner read failed: " + databaseError.getCode());
                }
            });

            biddersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (BCrypt.checkpw(password, data.child("password").getValue().toString())) {
                            editor.putString("userId", data.child("id").getValue().toString());
                            editor.putString("userType", "bidder");
                            editor.commit();
                            showToaster("Hola, " + data.child("fullName").getValue().toString());
                            Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                            startActivity(intent);
                        } else {
                            showToaster("Datos incorrectos");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The bidder read failed: " + databaseError.getCode());
                }
            });
        }
    }

    private boolean validForm() {
        if (emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
            showToaster("Campos vacíos.");
            return false;
        }
        return true;
    }

    public void togglePassword(View view) {
        if (showPassword) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setSelection(passwordEditText.getText().length());
            eyeIcon.setBackgroundResource(R.drawable.ic_eye);
        } else {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setSelection(passwordEditText.getText().length());
            eyeIcon.setBackgroundResource(R.drawable.ic_hide);
        }
        showPassword = !showPassword;
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
