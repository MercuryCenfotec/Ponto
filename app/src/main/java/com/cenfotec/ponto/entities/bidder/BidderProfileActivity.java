package com.cenfotec.ponto.entities.bidder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.MainActivity;
import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BidderProfileActivity extends AppCompatActivity {

    String token;
    TextView profileFullName;
    TextView profileBirthDate;
    TextView profileEmail;
    TextView profileIdentification;
    TextView profileBiography;
    TextView actualModificationTextView;
    EditText actualModificationEditText;
    Button btnDeleteBidder;
    Button btnBidderModificationDialog;
    Button btnBidderCancelDialog;
    View popupBidderModificationDialogView;
    Bidder bidder;
    Bidder temporalBidder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_profile);
        catchIntent();
        initProfileControls();
        getBidderByToken();
        initBidderDeletionButtonListener();
    }

    //This can be replaced by the login token
    private void catchIntent() {
        Intent intent = getIntent();
        token = intent.getExtras().getString("token");
    }

    private void initProfileControls() {
        profileFullName = findViewById(R.id.profileFullName);
        profileBirthDate = findViewById(R.id.profileBirthDate);
        profileEmail = findViewById(R.id.profileEmail);
        profileIdentification = findViewById(R.id.profileIdentification);
        profileBiography = findViewById(R.id.profileBiography);
        btnDeleteBidder = findViewById(R.id.btnDeleteBidder);
        bidder = new Bidder();
    }

    public void getBidderByToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderQuery = databaseReference.child("Bidders").orderByChild("id").equalTo(token);
        getBidderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    bidder = bidderSnapshot.getValue(Bidder.class);
                    showBidderProfileInformation();
                    initBidderInformationControls();
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
    private void initBidderInformationControls() {
        profileFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDynamicBidderDialog("Nombre completo",
                        profileFullName.getText().toString(), "fullName");
            }
        });

        profileBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDynamicBidderDialog("Fecha de nacimiento",
                        profileBirthDate.getText().toString(), "birthDate");
            }
        });

        profileEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDynamicBidderDialog("Email",
                        profileEmail.getText().toString(), "email");
            }
        });

        profileIdentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDynamicBidderDialog("Identificación",
                        profileIdentification.getText().toString(), "identificationNumber");
            }
        });

        profileBiography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDynamicBidderDialog("Biografía",
                        profileBiography.getText().toString(), "biography");
            }
        });
    }

    private void generateDynamicBidderDialog(String label, String input, final String type) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BidderProfileActivity.this);
        alertDialogBuilder.setTitle("Editar información");
        alertDialogBuilder.setCancelable(false);

        initPopupViewControls(label, input);

        alertDialogBuilder.setView(popupBidderModificationDialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        btnBidderModificationDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyInputBasedOnType(type);
                alertDialog.cancel();
            }
        });

        btnBidderCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    private void initPopupViewControls(String label, String input) {
        LayoutInflater layoutInflater = LayoutInflater.from(BidderProfileActivity.this);

        popupBidderModificationDialogView = layoutInflater.inflate(R.layout.bidder_modification_dialog, null);
        btnBidderModificationDialog = popupBidderModificationDialogView.findViewById(R.id.btnBidderModificationDialog);
        btnBidderCancelDialog = popupBidderModificationDialogView.findViewById(R.id.btnBidderCancelDialog);
        actualModificationEditText = popupBidderModificationDialogView.findViewById(R.id.actualModificationEditText);
        actualModificationTextView = popupBidderModificationDialogView.findViewById(R.id.actualModificationTextView);

        initEditTextBidderDataInPopupDialog(label, input);
    }

    private void initEditTextBidderDataInPopupDialog(String label, String input) {
        actualModificationEditText.setText(input);
        actualModificationTextView.setText(label);
    }

    private void modifyInputBasedOnType(String type) {
        temporalBidder = bidder;
        switch (type) {
            case "fullName":
                temporalBidder.setFullName(actualModificationEditText.getText().toString());
                break;
            case "birthDate":
                temporalBidder.setBirthDate(actualModificationEditText.getText().toString());
                break;
            case "email":
                temporalBidder.setEmail(actualModificationEditText.getText().toString());
                break;
            case "identificationNumber":
                temporalBidder.setIdentificationNumber(actualModificationEditText.getText().toString());
                break;
            case "biography":
                temporalBidder.setBiography(actualModificationEditText.getText().toString());
                break;
            default:
                break;
        }
        updateBidder();
    }

    private void updateBidder() {
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("Bidders").child(token);
        UpdateReference.setValue(temporalBidder);
        Toast.makeText(getApplicationContext(), "Bidder Updated", Toast.LENGTH_LONG).show();
        showBidderProfileInformation();
    }

    //Delete statements start here
    private void initBidderDeletionButtonListener() {
        btnDeleteBidder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBidder();
            }
        });
    }

    private void deleteBidder() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderQuery = databaseReference.child("Bidders").orderByChild("id").equalTo(token);
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

}
