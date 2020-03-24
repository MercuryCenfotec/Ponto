package com.cenfotec.ponto.entities.bidder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.BCrypt;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.cenfotec.ponto.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import customfonts.MyTextView_SF_Pro_Display_Medium;

public class BidderRegistrationActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference bidderDataReference;
    EditText fullNameEditText;
    EditText birthDateEditText;
    EditText emailEditText;
    EditText identificationEditText;
    EditText passwordEditText;
    EditText biographyEditText;
    MyTextView_SF_Pro_Display_Medium btnBidderRegistration;
    DatePickerDialog.OnDateSetListener birthDateSetListener;
    CustomDatePickerDialog customDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_registration);
        initFormControls();
        initBidderRegistrationControlsListener();
    }

    private void initFormControls() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        bidderDataReference = FirebaseDatabase.getInstance().getReference("Bidders");
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        biographyEditText = findViewById(R.id.biographyEditText);
        btnBidderRegistration = findViewById(R.id.btnBidderRegistration);
        identificationEditText = findViewById(R.id.identificationEditText);
        customDatePickerDialog = new CustomDatePickerDialog();
    }

    private void initBidderRegistrationControlsListener() {
        btnBidderRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preBidderRegistration();
            }
        });

        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDatePickerDialog.openDateDialog(birthDateEditText,
                        BidderRegistrationActivity.this, birthDateSetListener);
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

    //create statements start here
    private void preBidderRegistration() {
        if (!showErrorOnBlankSpaces() && isValidEmail()) {
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean bidderFound = false;
                            for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                                if (identificationEditText.getText().toString().equals(bidderSnapshot
                                        .child("identificationNumber").getValue().toString())) {
                                    bidderFound = true;
                                    showToaster("Identificación existente");
                                } else if (emailEditText.getText().toString().equals(bidderSnapshot
                                        .child("email").getValue().toString())) {
                                    bidderFound = true;
                                    showToaster("Email existente");
                                }
                            }

                            if (!bidderFound) {
                                registerBidderToDB();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });

        } else {
            showToaster("Error");
        }
    }

    private void registerBidderToDB() {
        String password = passwordEditText.getText().toString();
        String generatedSecuredPasswordHash = BCrypt.hashpw(password,
                BCrypt.gensalt(12));

        String userId = databaseReference.push().getKey();
        User user = new User(userId,fullNameEditText.getText().toString(),
                birthDateEditText.getText().toString(),
                emailEditText.getText().toString(),
                identificationEditText.getText().toString(),
                generatedSecuredPasswordHash, 1, 0, true, 2);

        databaseReference.child(userId).setValue(user);
        afterUserIsAdded(userId);
        //initBidderProfileView(bidderId);
    }

    private void afterUserIsAdded(String userId){
        String bidderId = bidderDataReference.push().getKey();
        Bidder bidder = new Bidder(bidderId, biographyEditText.getText().toString(), userId);
        bidderDataReference.child(bidderId).setValue(bidder);
        clearBidderRegistrationInputs();
        showToaster("Oferente agregado");

    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /*private void initBidderProfileView(String id) {
        Intent intent = new Intent(BidderRegistrationActivity.this,
                BidderProfileActivity.class);
        intent.putExtra("token", id);
        startActivity(intent);
    }*/

    //validation statements start here
    private void clearBidderRegistrationInputs() {
        fullNameEditText.setText("");
        birthDateEditText.setText("");
        emailEditText.setText("");
        identificationEditText.setText("");
        passwordEditText.setText("");
        biographyEditText.setText("");
    }

    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{fullNameEditText, birthDateEditText,
                emailEditText, identificationEditText, passwordEditText, biographyEditText};
        for (EditText editText : editTextsList) {
            if (editText.getText().toString().equals("")) {
                editText.setHintTextColor(Color.parseColor("#c0392b"));
                editText.setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editText.setBackgroundResource(R.drawable.rect);
                editText.setHintTextColor(Color.parseColor("#ffffff"));
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
            emailEditText.setHintTextColor(Color.parseColor("#c0392b"));
            return false;
        }
    }
}
