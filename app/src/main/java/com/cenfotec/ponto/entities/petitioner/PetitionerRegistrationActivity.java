package com.cenfotec.ponto.entities.petitioner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Account;
import com.cenfotec.ponto.data.model.BCrypt;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.cenfotec.ponto.data.model.Petitioner;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PetitionerRegistrationActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference accountDBReference;
    EditText identificationEditText;
    EditText fullNameEditText;
    EditText birthDateEditText;
    EditText emailEditText;
    EditText passwordEditText;
    DatePickerDialog.OnDateSetListener birthDateSetListener;
    CustomDatePickerDialog customDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petitioner_registration);

        findViews();
        initBidderRegistrationControlsListener();
    }

    private void initBidderRegistrationControlsListener() {
        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDatePickerDialog.openDateDialog(birthDateEditText,
                        PetitionerRegistrationActivity.this, birthDateSetListener);
            }
        });

        birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String formatDate = dayOfMonth + "/" + month + "/" + year;

                birthDateEditText.setText(formatDate);
            }
        };
    }

    private void findViews() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        accountDBReference = FirebaseDatabase.getInstance().getReference("Accounts");
        fullNameEditText = findViewById(R.id.fullNamePetEditText);
        birthDateEditText = findViewById(R.id.birthDatePetEditText);
        emailEditText = findViewById(R.id.emailPetEditText);
        identificationEditText = findViewById(R.id.identificationPetEditText);
        passwordEditText = findViewById(R.id.txtPassword);
        customDatePickerDialog = new CustomDatePickerDialog();
    }

    public void prePetitionerRegistration(View view) {
        if (!showErrorOnBlankSpaces() && isValidEmail()) {
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean petitionerFound = false;
                            for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                                if (identificationEditText.getText().toString().equals(bidderSnapshot
                                        .child("identificationNumber").getValue().toString())) {
                                    petitionerFound = true;
                                    showToaster("Identificación existente");
                                } else if (emailEditText.getText().toString().equals(bidderSnapshot
                                        .child("email").getValue().toString())) {
                                    petitionerFound = true;
                                    showToaster("Email existente");
                                }
                            }

                            if (!petitionerFound) {
                                registerPetitioner();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });

        } else {
            showToaster("Verificar campos");
        }
    }

    public void registerPetitioner() {
        String password = passwordEditText.getText().toString();
        String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Account creation start
        String accountNumber = accountDBReference.push().getKey();
        Account userAccount = new Account(accountNumber, (float) 0);
        accountDBReference.child(accountNumber).setValue(userAccount);
        // Account creation end

        String id = databaseReference.push().getKey();
        User user = new User(id,
                fullNameEditText.getText().toString(),
                birthDateEditText.getText().toString(),
                emailEditText.getText().toString(),
                identificationEditText.getText().toString(),
                generatedSecuredPasswordHash,
                1,
                0,
                true,
                1,
                "",
                accountNumber);

        if (id != null)
            databaseReference.child(id).setValue(user);

        clearPetitionRegistrationInputs();
        showToaster("Registro exitoso");
        openPetitionerProfile();
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void clearPetitionRegistrationInputs() {
        fullNameEditText.setText("");
        birthDateEditText.setText("");
        emailEditText.setText("");
        identificationEditText.setText("");
        passwordEditText.setText("");
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

    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{fullNameEditText, birthDateEditText,
                emailEditText, identificationEditText, passwordEditText};
        for (int matchKey = 0; matchKey < editTextsList.length; matchKey++) {
            if (editTextsList[matchKey].getText().toString().equals("")) {
//                editTextsList[matchKey].setHintTextColor(Color.parseColor("#c0392b"));
                editTextsList[matchKey].setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editTextsList[matchKey].setBackgroundResource(R.drawable.rect);
                editTextsList[matchKey].setHintTextColor(Color.parseColor("#ffffff"));
            }
        }
        return isEmpty;
    }

    private boolean isValidEmail() {
        String email = emailEditText.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setBackgroundResource(R.drawable.rect);
            emailEditText.setHintTextColor(Color.parseColor("#ffffff"));
            return true;
        } else {
            emailEditText.setBackgroundResource(R.drawable.edittext_error);
//            emailEditText.setHintTextColor(Color.parseColor("#c0392b"));
            return false;
        }
    }

    private void openPetitionerProfile() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
