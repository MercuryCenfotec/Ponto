package com.cenfotec.ponto.entities.bidder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.MainActivity;
import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import customfonts.MyTextView_SF_Pro_Display_Medium;

public class BidderProfileActivity extends AppCompatActivity {

    String intentToken;
    TextView profileFullName;
    TextView profileBirthDate;
    TextView profileEmail;
    TextView profileIdentification;
    TextView profileBiography;
    TextView modificationTextView;
    EditText modificationEditText;
    Button btnDeleteBidder;
    Button btnSwapVerification;
    MyTextView_SF_Pro_Display_Medium btnSaveBidderDialog;
    MyTextView_SF_Pro_Display_Medium btnCancelBidderDialog;
    View bidderModificationDialogView;
    Bidder bidder;
    Bidder temporalBidder;
    DatePickerDialog.OnDateSetListener birthDateSetListener;
    CustomDatePickerDialog customDatePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_profile);
        catchIntentContent();
        initProfileControls();
        getBidderByIntentToken();
        initBidderDeletionButtonListener();
    }

    //This can be replaced by the login token
    private void catchIntentContent() {
        Intent intent = getIntent();
        intentToken = intent.getExtras().getString("token");
    }

    private void initProfileControls() {
        profileFullName = findViewById(R.id.profileFullName);
        profileBirthDate = findViewById(R.id.profileBirthDate);
        profileEmail = findViewById(R.id.profileEmail);
        profileIdentification = findViewById(R.id.profileIdentification);
        profileBiography = findViewById(R.id.profileBiography);
        btnDeleteBidder = findViewById(R.id.btnDeleteBidder);
        btnSwapVerification = findViewById(R.id.btnSwapVerification);
        bidder = new Bidder();
        customDatePickerDialog = new CustomDatePickerDialog();
    }

    private void getBidderByIntentToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByIdQuery = databaseReference.child("Bidders").orderByChild("id").equalTo(intentToken);
        getBidderByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    bidder = bidderSnapshot.getValue(Bidder.class);
                    showBidderProfileInformation();
                    preInitBidderInformationControls();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void showBidderProfileInformation() {
        profileFullName.setText(bidder.getFullName());
        profileBirthDate.setText(bidder.getBirthDate());
        profileEmail.setText(bidder.getEmail());
        profileIdentification.setText(bidder.getIdentificationNumber());
        profileBiography.setText(bidder.getBiography());
    }

    //Update statements start here
    private void preInitBidderInformationControls() {
        initBidderProfileLabelListener(profileFullName, "Nombre completo",
                profileFullName.getText().toString(), "fullName");

        initBidderProfileLabelListener(profileBirthDate, "Fecha de nacimiento",
                profileBirthDate.getText().toString(), "birthDate");

        initBidderProfileLabelListener(profileEmail, "Email",
                profileEmail.getText().toString(), "email");

        initBidderProfileLabelListener(profileIdentification, "Identificación",
                profileIdentification.getText().toString(), "identificationNumber");

        initBidderProfileLabelListener(profileBiography, "Biografía",
                profileBiography.getText().toString(), "biography");

    }

    private void initBidderProfileLabelListener(TextView profileComponent, final String displayLabel,
                                                final String profileText, final String textType) {
        profileComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDynamicBidderDialog(displayLabel, profileText, textType);
            }
        });
    }

    private void generateDynamicBidderDialog(String label, String input, final String type) {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(BidderProfileActivity.this);
        alertDialogBuilder.setCancelable(false);

        initDialogViewControls(label, input);
        alertDialogBuilder.setView(bidderModificationDialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        initDialogButtonListener(alertDialog, type);
        if (type.equals("birthDate")) {
            initDateControls();
        }
    }

    private void initDialogButtonListener(final AlertDialog alertDialog, final String type) {
        btnSaveBidderDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showErrorOnBlankSpace()) {
                    if (type.equals("email")) {
                        if (isValidEmail()) {
                            preBidderModification(alertDialog, type);
                        }
                    } else {
                        preBidderModification(alertDialog, type);
                    }
                }
            }
        });

        btnCancelBidderDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    private void preBidderModification(final AlertDialog alertDialog, final String type) {
        FirebaseDatabase.getInstance().getReference().child("Bidders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean bidderFound = false;
                        for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                            if (modificationEditText.getText().toString().equals(bidderSnapshot
                                    .child("identificationNumber").getValue().toString())) {
                                bidderFound = true;
                                showToaster("Identificación existente");
                            } else if (modificationEditText.getText().toString().
                                    equals(bidderSnapshot.child("email").getValue().toString())) {
                                bidderFound = true;
                                showToaster("Email existente");
                            }
                        }
                        if (!bidderFound) {
                            modifyAttributeBasedOnType(type);
                            alertDialog.cancel();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
    }

    private void initDialogViewControls(String label, String input) {
        LayoutInflater layoutInflater = LayoutInflater.from(BidderProfileActivity.this);

        bidderModificationDialogView = layoutInflater.inflate(R.layout.bidder_modification_dialog, null);
        btnSaveBidderDialog = bidderModificationDialogView.findViewById(R.id.btnSaveBidderDialog);
        btnCancelBidderDialog = bidderModificationDialogView.findViewById(R.id.btnCancelBidderDialog);
        modificationEditText = bidderModificationDialogView.findViewById(R.id.modificationEditText);
        modificationTextView = bidderModificationDialogView.findViewById(R.id.modificationTextView);

        initEditTextBidderDataInPopupDialog(label, input);
    }

    private void initEditTextBidderDataInPopupDialog(String label, String input) {
        modificationEditText.setText(input);
        modificationEditText.setHint(label);
        modificationTextView.setText(label);
    }

    private void modifyAttributeBasedOnType(String type) {
        temporalBidder = bidder;
        switch (type) {
            case "fullName":
                temporalBidder.setFullName(modificationEditText.getText().toString());
                break;
            case "birthDate":
                temporalBidder.setBirthDate(modificationEditText.getText().toString());
                break;
            case "email":
                temporalBidder.setEmail(modificationEditText.getText().toString());
                break;
            case "identificationNumber":
                temporalBidder.setIdentificationNumber(modificationEditText.getText().toString());
                break;
            case "biography":
                temporalBidder.setBiography(modificationEditText.getText().toString());
                break;
            default:
                break;
        }
        updateBidder();
    }

    private void updateBidder() {
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().
                getReference("Bidders").child(intentToken);
        UpdateReference.setValue(temporalBidder);
        showToaster("Cambio guardado");
        showBidderProfileInformation();
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //Delete statements start here
    private void initBidderDeletionButtonListener() {
        btnDeleteBidder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBidder();
            }
        });

        btnSwapVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapVerification();
            }
        });
    }

    private void swapVerification(){
        Intent intent = new Intent(BidderProfileActivity.this, BidderVerification.class);
        startActivity(intent);
    }

    private void deleteBidder() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderQuery = databaseReference.child("Bidders").orderByChild("id").equalTo(intentToken);
        getBidderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    node.getRef().removeValue();
                    initMainView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void initMainView() {
        Intent intent = new Intent(BidderProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Validation statements start here
    private boolean showErrorOnBlankSpace() {
        if (modificationEditText.getText().toString().equals("")) {
            modificationEditText.setHintTextColor(Color.parseColor("#c0392b"));
            modificationEditText.setBackgroundResource(R.drawable.edittext_error);
            return true;
        } else {
            modificationEditText.setHintTextColor(Color.parseColor("#ffffff"));
            modificationEditText.setBackgroundResource(R.drawable.rect);
            return false;
        }
    }

    private boolean isValidEmail() {
        String email = modificationEditText.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            modificationEditText.setHintTextColor(Color.parseColor("#ffffff"));
            modificationEditText.setBackgroundResource(R.drawable.rect);
            return true;
        } else {
            modificationEditText.setHintTextColor(Color.parseColor("#c0392b"));
            modificationEditText.setBackgroundResource(R.drawable.edittext_error);
            return false;
        }
    }

    private void initDateControls() {
        modificationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDatePickerDialog.openDateDialog(modificationEditText,
                        BidderProfileActivity.this, birthDateSetListener);
            }
        });

        birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String formatDate = dayOfMonth + "/" + month + "/" + year;

                modificationEditText.setText(formatDate);
            }
        };
    }
}
