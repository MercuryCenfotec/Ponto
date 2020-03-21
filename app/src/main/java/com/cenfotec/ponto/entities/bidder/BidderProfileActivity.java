package com.cenfotec.ponto.entities.bidder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.cenfotec.ponto.entities.user.LoginActivity;
import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.cenfotec.ponto.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Scanner;

import adapter.ProfileAdapter;
import model.ProfileModel;

public class BidderProfileActivity extends AppCompatActivity {

    private static SharedPreferences sharedpreferences;
    private String activeUserId;
    private ProfileAdapter profileAdapter;
    private RecyclerView recyclerview;
    private ArrayList<ProfileModel> profileModelArrayList;
    User user;
    Integer inbox[] = {R.drawable.ic_calendar,R.drawable.ic_like, R.drawable.ic_star,
            R.drawable.ic_contract, R.drawable.ic_profile,R.drawable.ic_settings};
    Integer arrow = R.drawable.ic_chevron_right_black_24dp;
    String txttrades[] = {"Agenda de proyectos", "Recomendaciones", "Reseñas", "Contratos",
            "Mi Perfil", "Ajustes"};
    String txthistory[] = {"Revisá tus contrataciones", "Administrá tus recomendaciones",
            "Tu colección", "Tu colección", "Cambiá la información de tu perfil", "Ajustes"};
    TextView profileFullName;
    TextView profileEmail;
    TextView profileRating;
    TextView profileBiography;
    Bidder bidder;
    CustomDatePickerDialog customDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_profile);
        initProfileControls();
        getActiveUserId();
        getUserByActiveUserId();
        showRecyclerViewOptions();
    }

    private void showRecyclerViewOptions() {
        recyclerview = findViewById(R.id.bidderProfileOptions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        profileModelArrayList = new ArrayList<>();

        for (int i = 0; i < inbox.length; i++) {
            ProfileModel view = new ProfileModel(inbox[i], arrow, txttrades[i], txthistory[i],
                    activeUserId,"bidder");
            profileModelArrayList.add(view);
        }

        profileAdapter = new ProfileAdapter(this, profileModelArrayList);
        recyclerview.setAdapter(profileAdapter);
    }

    private void getActiveUserId() {
        sharedpreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        activeUserId = sharedpreferences.getString("userId", "");
    }

    private void getUserByActiveUserId() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(activeUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                getBidderByUserId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initProfileControls() {
        profileFullName = findViewById(R.id.bidderFullNameProfile);
        profileEmail = findViewById(R.id.bidderMailProfile);
        profileRating = findViewById(R.id.bidderRatingProfile);
        profileBiography = findViewById(R.id.bidderBiographyProfile);
        bidder = new Bidder();
        user = new User();
        customDatePickerDialog = new CustomDatePickerDialog();
    }

    private void getBidderByUserId() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByIdQuery =
                databaseReference.child("Bidders").orderByChild("userId").equalTo(activeUserId);
        getBidderByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    bidder = bidderSnapshot.getValue(Bidder.class);
                    showBidderProfileInformation();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void showBidderProfileInformation() {

        // Convert first letter to capital
        StringBuilder capitalized = new StringBuilder();
        Scanner lineScan = new Scanner(user.getFullName().toLowerCase());
        while(lineScan.hasNext()) {
            String word = lineScan.next();
            capitalized.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }

        profileFullName.setText(capitalized);
        profileEmail.setText(user.getEmail());
        profileRating.setText(String.valueOf(user.getRating()));
        profileBiography.setText(bidder.getBiography());
    }


    //Update statements start here
    /*private void preInitBidderInformationControls() {
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
        FirebaseDatabase.getInstance().getReference().child("Users")
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
        temporalUser = user;
        switch (type) {
            case "fullName":
                temporalUser.setFullName(modificationEditText.getText().toString());
                break;
            case "birthDate":
                temporalUser.setBirthDate(modificationEditText.getText().toString());
                break;
            case "email":
                temporalUser.setEmail(modificationEditText.getText().toString());
                break;
            case "identificationNumber":
                temporalUser.setIdentificationNumber(modificationEditText.getText().toString());
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
        DatabaseReference updateUserReference = FirebaseDatabase.getInstance().
                getReference("Users").child(activeUserId);
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().
                getReference("Bidders").child(bidder.getId());
        updateUserReference.setValue(temporalUser);
        UpdateReference.setValue(temporalBidder);
        showToaster("Cambio guardado");
        showBidderProfileInformation();
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }*/

    /*//Validation statements start here
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
    }*/
}
