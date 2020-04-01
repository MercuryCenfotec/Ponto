package com.cenfotec.ponto.entities.servicePetition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cenfotec.ponto.MainActivity;
import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.SpinnerItem;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import customfonts.MyTextView_SF_Pro_Display_Medium;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ServicePetitionUpdateActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    String intentToken;
    ServicePetition servicePetition;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText serviceTypeEditText;
    MyTextView_SF_Pro_Display_Medium btnPostPetition;
    DatabaseReference serviceTypesRef;
    ArrayList<String> spinnerKeys;
    ArrayList<String> spinnerValues;
    SpinnerDialog spinnerDialog;
    ImageView returnIcon;
    String serviceTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_update);
        initFormControls();
        catchIntentContent();
        getServicePetitionByIntentToken();
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
    }
    //This can be replaced by the service petition id
    private void catchIntentContent() {
        intentToken = sharedPreferences.getString("servicePetitionId", "");
    }


    private void initFormControls() {
        returnIcon = findViewById(R.id.returnIcon);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        titleEditText = findViewById(R.id.petitionTitleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        serviceTypeEditText = findViewById(R.id.serviceTypeEditText);
        btnPostPetition = findViewById(R.id.btnPetitionCreation);


        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPetitionDetail();
            }
        });
    }

    private void goToPetitionDetail() {

        Intent intent = new Intent(this, ServicePetitionPetitionerDetailActivity.class);
        startActivity(intent);
    }

    private void initSpinnerData() {
        serviceTypesRef = FirebaseDatabase.getInstance().getReference("ServiceTypes");
        spinnerKeys = new ArrayList<>();
        spinnerValues = new ArrayList<>();

        serviceTypesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot serviceType : dataSnapshot.getChildren()) {
                    spinnerValues.add(serviceType.child("id").getValue().toString());
                    spinnerKeys.add(serviceType.child("serviceType").getValue().toString());

                    if(serviceType.child("id").getValue().toString().equals(servicePetition.getServiceTypeId())) {
                        serviceTypeEditText.setText(serviceType.child("serviceType").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinnerDialog = new SpinnerDialog(this, spinnerKeys,"Buscar","Cancelar");


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                serviceTypeEditText.setText(item);
                serviceTypeId = spinnerValues.get(position);
            }
        });
    }

    public void showSpinnerDialog(View view) {
        spinnerDialog.showSpinerDialog();
    }

    private void initPetitionUpdateControlsListener() {
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
        initSpinnerData();
    }

    private void getServicePetitionByIntentToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getPetitionByIdQuery = databaseReference.child("ServicePetitions").orderByChild("id").equalTo(intentToken);
        getPetitionByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot petitionSnapshot : snapshot.getChildren()) {
                    servicePetition = petitionSnapshot.getValue(ServicePetition.class);
                    showFormInformation();
                    initPetitionUpdateControlsListener();
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
        servicePetition.setServiceTypeId(serviceTypeId);
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
