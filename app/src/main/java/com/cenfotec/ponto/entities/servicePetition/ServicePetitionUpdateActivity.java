package com.cenfotec.ponto.entities.servicePetition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import customfonts.MyTextView_SF_Pro_Display_Medium;

public class ServicePetitionUpdateActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    DatabaseReference databaseReference;
    String intentToken;
    ServicePetition servicePetition;
    SharedPreferences sharedPreferences;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText serviceTypeEditText;
    MyTextView_SF_Pro_Display_Medium btnPostPetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_update);
        catchIntentContent();
        initFormControls();
        getServicePetitionByIntentToken();
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
    }
    //This can be replaced by the service petition id
    private void catchIntentContent() {
        intentToken = "-M2oldNjlxrtUUywl3pc";
    }


    private void initFormControls() {
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        titleEditText = findViewById(R.id.petitionTitleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        serviceTypeEditText = findViewById(R.id.serviceTypeEditText);
        btnPostPetition = findViewById(R.id.btnPetitionCreation);

    }

    private void initBidderUpdateControlsListener() {
        btnPostPetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prePetitionUpdate();
            }
        });
    }

    private void showFormInformation() {
        titleEditText.setText(servicePetition.getName());
        descriptionEditText.setText(servicePetition.getDescription());
        serviceTypeEditText.setText(servicePetition.getServiceTypeId());
    }

    private void getServicePetitionByIntentToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByIdQuery = databaseReference.child("ServicePetitions").orderByChild("id").equalTo(intentToken);
        getBidderByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    servicePetition = bidderSnapshot.getValue(ServicePetition.class);
                    showFormInformation();
                    initBidderUpdateControlsListener();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    //Update statements start here
    private void prePetitionUpdate() {
        if (!showErrorOnBlankSpaces() ) {
            FirebaseDatabase.getInstance().getReference().child("ServicePetitions")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            updateServicePetitionToDB();
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

    private void updateServicePetitionToDB() {

        servicePetition.setName(titleEditText.getText().toString());
        servicePetition.setDescription(descriptionEditText.getText().toString());
        servicePetition.setServiceTypeId(serviceTypeEditText.getText().toString());
        ServicePetition updatedServicePetition = servicePetition;
        databaseReference.child(servicePetition.getId()).setValue(updatedServicePetition);

        showToaster("Solicitud de servicio modificada!");
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
