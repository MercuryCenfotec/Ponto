package com.cenfotec.ponto.entities.bidder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.BCrypt;
import com.cenfotec.ponto.data.model.Bidder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class BidderRegistrationActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText fullNameEditText;
    EditText birthDateEditText;
    EditText emailEditText;
    EditText identificationEditText;
    EditText passwordEditText;
    EditText biographyEditText;
    Button btnBidderRegistration;
    TextInputLayout fullNameInputLayout;
    TextInputLayout birthDateInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout identificationInputLayout;
    TextInputLayout passwordInputLayout;
    TextInputLayout biographyInputLayout;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_registration);
        initFormControls();
        initBidderRegistrationButtonListener();
    }

    private void initFormControls() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Bidders");
        fullNameEditText = findViewById(R.id.fullNameEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        emailEditText = findViewById(R.id.emailEditText);
        identificationEditText = findViewById(R.id.identificationEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        biographyEditText = findViewById(R.id.biographyEditText);
        btnBidderRegistration = findViewById(R.id.btnBidderRegistration);
        fullNameInputLayout = findViewById(R.id.fullNameInputLayout);
        birthDateInputLayout = findViewById(R.id.birthDateInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        identificationInputLayout = findViewById(R.id.identificationInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        biographyInputLayout = findViewById(R.id.biographyInputLayout);
    }

    private void initBidderRegistrationButtonListener() {
        btnBidderRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preBidderRegistration();
            }
        });

        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialog();
            }
        });

        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String sDate = dayOfMonth + "/" + month + "/" + year;

                birthDateEditText.setText(sDate);
            }
        };
    }

    //create statements start here
    private void preBidderRegistration() {
        if (!showErrorOnBlankSpaces() && isValidEmail()) {
            FirebaseDatabase.getInstance().getReference().child("Bidders")
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

        String id = databaseReference.push().getKey();
        Bidder bidder = new Bidder(id, fullNameEditText.getText().toString(),
                birthDateEditText.getText().toString(),
                emailEditText.getText().toString(),
                identificationEditText.getText().toString(),
                generatedSecuredPasswordHash,
                biographyEditText.getText().toString(), 1);
        databaseReference.child(id).setValue(bidder);

        clearBidderRegistrationInputs();
        showToaster("Oferente agregado");
        initBidderProfileView(id);
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void initBidderProfileView(String id) {
        Intent intent = new Intent(BidderRegistrationActivity.this,
                BidderProfileActivity.class);
        intent.putExtra("token", id);
        startActivity(intent);
    }

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
        TextInputLayout[] textInputLayouts = new TextInputLayout[]{fullNameInputLayout,
                birthDateInputLayout, emailInputLayout, identificationInputLayout,
                passwordInputLayout, biographyInputLayout};

        for (int matchKey = 0; matchKey < editTextsList.length; matchKey++) {
            if (editTextsList[matchKey].getText().toString().equals("")) {
                textInputLayouts[matchKey].setError("Campo requerido");
                isEmpty = true;
            } else {
                textInputLayouts[matchKey].setError(null);
            }
        }
        return isEmpty;
    }

    private boolean isValidEmail() {
        String email = emailEditText.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError(null);
            return true;
        } else {
            emailInputLayout.setError("Email inválido");
            return false;
        }
    }

    private void openDateDialog() {
        int year;
        int month;
        int day;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);

        if (TextUtils.isEmpty(birthDateEditText.getText().toString())) {
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            day = Integer.parseInt(birthDateEditText.getText().toString().split("/")[0]);
            month = Integer.parseInt(birthDateEditText.getText().toString().split("/")[1])-1;
            year = Integer.parseInt(birthDateEditText.getText().toString().split("/")[2]);
        }

        DatePickerDialog dialog = new DatePickerDialog(
                BidderRegistrationActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                mDateSetListener,
                year, month, day);
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", dialog);
        dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        dialog.show();
    }
}
