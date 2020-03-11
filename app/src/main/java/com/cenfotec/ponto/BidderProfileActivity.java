package com.cenfotec.ponto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_profile);
        catchIntent();
        initProfileLabels();
        getBidderByToken();
        initBidderDeletionButtonListener();
    }

    //This can be replaced by the login token
    private void catchIntent() {
        Intent intent = getIntent();
        token = intent.getExtras().getString("token");
    }

    private void initProfileLabels() {
        profileFullName = findViewById(R.id.profileFullName);
        profileBirthDate = findViewById(R.id.profileBirthDate);
        profileEmail = findViewById(R.id.profileEmail);
        profileIdentification = findViewById(R.id.profileIdentification);
        profileBiography = findViewById(R.id.profileBiography);
        btnDeleteBidder = findViewById(R.id.btnDeleteBidder);

    }

    public void getBidderByToken() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderQuery = databaseReference.child("Bidders").orderByChild("id").equalTo(token);
        getBidderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot node : snapshot.getChildren()) {
                    profileFullName.setText(node.child("fullName").getValue().toString());
                    profileBirthDate.setText(node.child("birthDate").getValue().toString());
                    profileEmail.setText(node.child("email").getValue().toString());
                    profileIdentification.setText(node.child("identificationNumber").getValue().toString());
                    profileBiography.setText(node.child("biography").getValue().toString());
                    showBidderInformationDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    //Update statements start here
    private void showBidderInformationDialog() {
        profileFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BidderProfileActivity.this);
                alertDialogBuilder.setTitle("Editar información");
                alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                alertDialogBuilder.setCancelable(false);

                initPopupViewControls("Nombre completo", profileFullName.getText().toString());

                alertDialogBuilder.setView(popupBidderModificationDialogView);

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                btnBidderModificationDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
        });
    }

    private void initPopupViewControls(String label, String input) {
        LayoutInflater layoutInflater = LayoutInflater.from(BidderProfileActivity.this);

        popupBidderModificationDialogView = layoutInflater.inflate(R.layout.bidder_modification_dialog, null);
        btnBidderModificationDialog = popupBidderModificationDialogView.findViewById(R.id.btnBidderModificationDialog);
        btnBidderCancelDialog = popupBidderModificationDialogView.findViewById(R.id.btnBidderCancelDialog);
        actualModificationEditText = popupBidderModificationDialogView.findViewById(R.id.actualModificationEditText);
        actualModificationTextView = popupBidderModificationDialogView.findViewById(R.id.actualModificationTextView);

        initEditTextUserDataInPopupDialog(label, input);
    }

    private void initEditTextUserDataInPopupDialog(String label, String input) {
        actualModificationEditText.setText(input);
        actualModificationTextView.setText(label);
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
