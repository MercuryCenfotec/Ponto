package com.cenfotec.ponto.entities.servicePetition;

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
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.BCrypt;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Locale;

import customfonts.MyTextView_SF_Pro_Display_Medium;

public class ServicePetitionCreationActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText serviceTypeEditText;
    MyTextView_SF_Pro_Display_Medium btnPostPetition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_creation);
        initFormControls();
        initServicePetitionCreationControlsListener();
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
    }

    private void initFormControls() {
        databaseReference = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        titleEditText = findViewById(R.id.petitionTitleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        serviceTypeEditText = findViewById(R.id.serviceTypeEditText);
        btnPostPetition = findViewById(R.id.btnPetitionCreation);
    }

    private void initServicePetitionCreationControlsListener() {
        btnPostPetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prePetitionCreation();
            }
        });
//
//        birthDateEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                customDatePickerDialog.openDateDialog(birthDateEditText,
//                        BidderRegistrationActivity.this, birthDateSetListener);
//            }
//        });
//
//        birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                month += 1;
//                String formatDate = dayOfMonth + "/" + month + "/" + year;
//
//                birthDateEditText.setText(formatDate);
//            }
//        };
    }

    //create statements start here
    private void prePetitionCreation() {
        if (!showErrorOnBlankSpaces() ) {
            FirebaseDatabase.getInstance().getReference().child("ServicePetitions")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            registerServicePetitionToDB();
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

    private void registerServicePetitionToDB() {

        String id = databaseReference.push().getKey();
        ServicePetition servicePetition = new ServicePetition(
                id,
                "1234",
                titleEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                serviceTypeEditText.getText().toString(),
                false
        );
        databaseReference.child(id).setValue(servicePetition);

        clearServicePetitionInputs();
        showToaster("Solicitud de servicio creada!");
//        initBidderProfileView(id);
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

//    private void initBidderProfileView(String id) {
//        Intent intent = new Intent(ServicePetitionCreationActivity.this,
//                BidderProfileActivity.class);
//        intent.putExtra("token", id);
//        startActivity(intent);
//    }

    //validation statements start here
    private void clearServicePetitionInputs() {
        titleEditText.setText("");
        descriptionEditText.setText("");
        serviceTypeEditText.setText("");
    }

    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{titleEditText, descriptionEditText,
                serviceTypeEditText};
        for (int matchKey = 0; matchKey < editTextsList.length; matchKey++) {
            if (editTextsList[matchKey].getText().toString().equals("")) {
                editTextsList[matchKey].setHintTextColor(Color.parseColor("#c0392b"));
                editTextsList[matchKey].setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editTextsList[matchKey].setBackgroundResource(R.drawable.rect);
                editTextsList[matchKey].setHintTextColor(Color.parseColor("#ffffff"));
            }
        }
        return isEmpty;
    }

}
