package com.cenfotec.ponto.entities.offer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Account;
import com.cenfotec.ponto.data.model.Appointment;
import com.cenfotec.ponto.data.model.Contract;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.account.AccountActivity;
import com.cenfotec.ponto.entities.account.NotEnoughFundsDialog;
import com.cenfotec.ponto.entities.appointment.AppointmentAgendaActivity;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.cenfotec.ponto.entities.contract.GeneratedContractActivity;
import com.cenfotec.ponto.entities.petitioner.PetitionerHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OfferDetailActivity extends AppCompatActivity implements CounterOfferDialog.CounterOfferDialogListener, AcceptOfferDialog.AcceptOfferDialogListener, CounterOfferConfirmDialog.CounterOfferDialogConfirmListener, NotEnoughFundsDialog.GoToAccountNoFundsDialogListener {

    private static SharedPreferences sharedpreferences;
    DatabaseReference offerDBReference;
    DatabaseReference bidderDBReference;
    DatabaseReference accountDBReference;
    public static final String MY_PREFERENCES = "MyPrefs";
    TextView costText;
    TextView durationTypeText;
    TextView durationText;
    TextView descriptionText;
    SharedPreferences myPrefs;
    TextView userName;
    ImageView userImage;
    LinearLayout bidderDetail;
    TextView createOfferButton;
    TextView viewTitle;
    boolean hasCounterOffer;
    String offerId;
    Offer activeOffer;
    TextView counterOfferButton;
    TextView acceptOfferButton;
    final DecimalFormat costFormat = new DecimalFormat("###,###.###");

    // Counter offer
    View divisorLineOfferDetail;
    ImageView counterOfferIconDetail;
    TextView counterOfferTextDetail;
    TextView counterOfferDescDetail;
    TextView counterOfferCostTitleDetail;
    TextView counterOfferCostDetail;
    TextView btnAcceptCounterOffer;

    TextView petitionerCostTitle;
    TextView petitionerCostDetail;
    // Counter offer end

    String userId;

    // ## Appointment ##
    TextView btnCreateAppointment;
    // ## Appointment end ##

    // Account start
    Account userAccount;
    // Account end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);
        offerDBReference = FirebaseDatabase.getInstance().getReference("Offers");
        bidderDBReference = FirebaseDatabase.getInstance().getReference("Bidders");
        accountDBReference = FirebaseDatabase.getInstance().getReference("Accounts");
        costText = findViewById(R.id.costValue);
        durationTypeText = findViewById(R.id.durationTypeValue);
        durationText = findViewById(R.id.durationValue);
        descriptionText = findViewById(R.id.descriptionValue);
        myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        userImage = findViewById(R.id.offerUserImage);
        userName = findViewById(R.id.offerUserName);
        bidderDetail = findViewById(R.id.bidderDetail);
        bidderDetail.setVisibility(View.GONE);
        createOfferButton = findViewById(R.id.createOfferButton);
        viewTitle = findViewById(R.id.viewTitle);
        counterOfferButton = findViewById(R.id.btnOfferCreation);
        acceptOfferButton = findViewById(R.id.acceptOfferButton);

        // Counter offer
        divisorLineOfferDetail = findViewById(R.id.divisorLineOfferDetail);
        counterOfferIconDetail = findViewById(R.id.counterOfferIconDetail);
        counterOfferTextDetail = findViewById(R.id.counterOfferTextDetail);
        counterOfferDescDetail = findViewById(R.id.counterOfferDescDetail);
        counterOfferCostTitleDetail = findViewById(R.id.counterOfferCostTitleDetail);
        counterOfferCostDetail = findViewById(R.id.counterOfferCostDetail);
        btnAcceptCounterOffer = findViewById(R.id.btnAcceptCounterOffer);

        petitionerCostTitle = findViewById(R.id.petCounterOfferDetail);
        petitionerCostDetail = findViewById(R.id.petCounterOfferCostDetail);
        // Counter offer end

        btnCreateAppointment = findViewById(R.id.btnCreateAppointment);

        userId = myPrefs.getString("userId", "none");
        offerId = myPrefs.getString("offerId","none");

        loadOfferData(userId, offerId);

        // ## Appointment ##
        checkIfServiceHasAcceptedOffer();
        // ## Appointment end ##

        getActiveUser(userId);
    }

    private void getActiveUser(String userId) {

        final DatabaseReference userDBReference = FirebaseDatabase.getInstance().getReference();
        Query getUserByIdQuery = userDBReference.child("Users").orderByChild("id").equalTo(userId);
        getUserByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    getUserAccount(snapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserAccount(User activeUser) {
        final DatabaseReference accountDBReference = FirebaseDatabase.getInstance().getReference();
        Query getUserAccountQuery = accountDBReference.child("Accounts").orderByChild("accountNumber").equalTo(activeUser.getUserAccount());
        getUserAccountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                    userAccount = accountSnapshot.getValue(Account.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadOfferData(final String userId, String offerId) {
        Query offerQuery = offerDBReference.orderByChild("id").equalTo(offerId);

        offerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    activeOffer = data.getValue(Offer.class);

                    costText.setText("₡" + costFormat.format(Double.parseDouble(data.child("cost").getValue().toString())));
                    descriptionText.setText(data.child("description").getValue().toString());
                    durationText.setText(data.child("duration").getValue().toString() + (data.child("durationType").getValue().toString().equals("hour") ? " horas" : " días"));
                    durationTypeText.setText(data.child("durationType").getValue().toString().equals("hour") ? "Por hora" : "Por día");
                    viewTitle.setText(data.child("servicePetitionTitle").getValue().toString());

                    // Counter offer
                    counterOfferCostDetail.setText("₡" + costFormat.format(Double.parseDouble(data.child("counterOfferCost").getValue().toString())));

                    if (data.child("counterOffer").getValue() != null) {
                        hasCounterOffer = data.child("counterOffer").getValue().toString().equals("true");

                        if (hasCounterOffer && data.child("userId").getValue().toString().equals(userId)) {
                            // BIDDER WITH COUNTER OFFER
                            divisorLineOfferDetail.setVisibility(View.VISIBLE);
                            counterOfferIconDetail.setVisibility(View.VISIBLE);
                            counterOfferTextDetail.setVisibility(View.VISIBLE);
                            counterOfferDescDetail.setVisibility(View.VISIBLE);
                            counterOfferDescDetail.setVisibility(View.VISIBLE);
                            counterOfferCostTitleDetail.setVisibility(View.VISIBLE);
                            counterOfferCostDetail.setVisibility(View.VISIBLE);
                            btnAcceptCounterOffer.setVisibility(View.VISIBLE);

                        } else if (hasCounterOffer && !data.child("userId").getValue().toString().equals(userId)) {
                            // PETITIONER WITH COUNTER OFFER
                            counterOfferIconDetail.setVisibility(View.GONE);
                            counterOfferTextDetail.setVisibility(View.GONE);
                            counterOfferDescDetail.setVisibility(View.GONE);
                            counterOfferCostTitleDetail.setVisibility(View.GONE);
                            counterOfferCostDetail.setVisibility(View.GONE);
                            btnAcceptCounterOffer.setVisibility(View.GONE);

                            counterOfferButton.setText("Modificar contraoferta");
                            petitionerCostDetail.setText("₡" + costFormat.format(Double.parseDouble(data.child("counterOfferCost").getValue().toString())));
                            divisorLineOfferDetail.setVisibility(View.VISIBLE);
                            petitionerCostTitle.setVisibility(View.VISIBLE);
                            petitionerCostDetail.setVisibility(View.VISIBLE);

                        } else if (!hasCounterOffer) {
                            counterOfferIconDetail.setVisibility(View.GONE);
                            counterOfferTextDetail.setVisibility(View.GONE);
                            counterOfferDescDetail.setVisibility(View.GONE);
                            counterOfferCostTitleDetail.setVisibility(View.GONE);
                            counterOfferCostDetail.setVisibility(View.GONE);
                            btnAcceptCounterOffer.setVisibility(View.GONE);
                            divisorLineOfferDetail.setVisibility(View.GONE);
                            petitionerCostTitle.setVisibility(View.GONE);
                            petitionerCostDetail.setVisibility(View.GONE);
                        }

                    }

                    if (data.child("counterOfferCost").getValue().equals(data.child("cost").getValue())) {
                        counterOfferTextDetail.setText("Aceptó una contraoferta");
                        counterOfferDescDetail.setText("Usted aceptó la contraoferta que realizó el creador de la solicitud.");
                        btnAcceptCounterOffer.setVisibility(View.GONE);
                    } else {
                        counterOfferTextDetail.setText("Tiene una contraoferta");
                        counterOfferDescDetail.setText("El creador de la solicitud está interesado en su oferta, pero realizó una contraoferta.");
                    }

                    // Counter offer end

                    if (!data.child("userId").getValue().toString().equals(userId)) {
                        // Petitioner
                        if (!data.child("accepted").getValue().toString().equals("accepted")) {
                            counterOfferButton.setVisibility(View.VISIBLE);
                            acceptOfferButton.setVisibility(View.VISIBLE);
                        }
                        bidderDetail.setVisibility(View.VISIBLE);
                        createOfferButton.setVisibility(View.GONE);
                        userName.setText(data.child("bidderName").getValue().toString());
                        if (!data.child("bidderImageUrl").getValue().toString().equals("")) {
                            Picasso.get().load(data.child("bidderImageUrl").getValue().toString()).into(userImage);
                        }
                    } else if (data.child("accepted").getValue().toString().equals("accepted")) {
                        createOfferButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The offer read failed: " + databaseError.getCode());
            }
        });
    }

    public void goToOfferUpdate(View view) {
        Intent intent = new Intent(this, OfferUpdateActivity.class);
        startActivity(intent);
    }

    public void  acceptOffer() {
        String servicePetitionId = myPrefs.getString("servicePetitionId", "none");
        final String offerId = myPrefs.getString("offerId", "none");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Offers");
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("ServicePetitions");

        Query offersQuery = ref.orderByChild("servicePetitionId").equalTo(servicePetitionId);

        offersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot offerSS : dataSnapshot.getChildren()) {

                    if (hasSufficientFunds()) {
                        ref.child(offerSS.child("id").getValue().toString()).child("accepted")
                                .setValue(offerSS.child("id").getValue().toString().equals(offerId) ?
                                        "accepted" :
                                        "cancelled");

                        Toast.makeText(OfferDetailActivity.this, "La oferta fue aceptada", Toast.LENGTH_LONG).show();
                        registerContractToDB();

                    } else {
                        openNoFundsDialog();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref2.child(servicePetitionId).child("acceptedOfferId").setValue(offerId);

    }

    @Override
    public void dialogNoFundsGoToAccount() {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void openNoFundsDialog() {
        NotEnoughFundsDialog noFundsDialog = new NotEnoughFundsDialog();
        noFundsDialog.show(getSupportFragmentManager(), "insufficient funds dialog");
    }

    private boolean hasSufficientFunds() {
        if (userAccount.getBalance() >= activeOffer.getCost()) {
            accountDBReference.child(userAccount.getAccountNumber()).child("balance").setValue(userAccount.getBalance() - activeOffer.getCost());
            return true;

        } else {
            return false;
        }
    }

    // ## Contract statements start here ##
    private void registerContractToDB(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contracts");
        String contractId = databaseReference.push().getKey();
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String petitionerId = sharedpreferences.getString("userId", "");
        String bidderUserId = activeOffer.getUserId();
        Contract contract = new Contract(contractId, petitionerId, bidderUserId,
                "", "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();
        String timestamp = sdf.format(now);
        String timestamp2 = sdf2.format(now);
        contract.setName("CTR-" + timestamp);
        contract.setDateCreated(timestamp2);
        contract.setServicePetitionId(activeOffer.getServicePetitionId());
        contract.setOfferId(activeOffer.getId());
        contract.setFinalCost(activeOffer.getCost());
        databaseReference.child(contractId).setValue(contract);

        myPrefs.edit().putString("contractId", contractId).commit();
        Intent intent = new Intent(this, GeneratedContractActivity.class);
        intent.putExtra("petitionerId", contract.getPetitionerId());
        intent.putExtra("bidderUserId", contract.getBidderId());
        intent.putExtra("contractId", contract.getId());
        intent.putExtra("finalCost", contract.getFinalCost());
        startActivity(intent);
        finish();
    }
    // ## Contract statements end ##

    // ## Appointment statements start here ##
    public void goToAppointmentAgenda(View view) {
        Intent iac = new Intent(this, AppointmentAgendaActivity.class);
        iac.putExtra("userId", myPrefs.getString("userId", ""));
        iac.putExtra("userType", myPrefs.getString("userType", ""));
        iac.putExtra("petitionerId", myPrefs.getString("userId", ""));
        iac.putExtra("bidderId", activeOffer.getUserId());
        startActivity(iac);
    }

    private void checkIfServiceHasAcceptedOffer() {
        String servicePetitionId = myPrefs.getString("servicePetitionId", "none");
        final DatabaseReference servicePetitionsRef = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        Query getServicePetitionByIdQuery = servicePetitionsRef.orderByChild("id").equalTo(servicePetitionId);
        getServicePetitionByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    ServicePetition tempService = serviceSnapshot.getValue(ServicePetition.class);
                    if(!tempService.getAcceptedOfferId().equals("")) {
                        btnCreateAppointment.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    // ## Appointment statements end ##

    public void createCounterOffer(String newCost) {
        activeOffer.setCounterOffer(true);
        activeOffer.setCounterOfferCost(Float.parseFloat(newCost));
        offerDBReference.child(offerId).setValue(activeOffer);
        hasCounterOffer = true;

        counterOfferButton.setText("Modificar contraoferta");
        petitionerCostDetail.setText(newCost);
        divisorLineOfferDetail.setVisibility(View.VISIBLE);
        petitionerCostTitle.setVisibility(View.VISIBLE);
        petitionerCostDetail.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Contraoferta exitosa", Toast.LENGTH_LONG).show();
    }

    @Override
    public void applyText(String newCost) {
        createCounterOffer(newCost);
    }

    public void openCounterOfferDialog(View view) {
        CounterOfferDialog counterDialog = new CounterOfferDialog();
        counterDialog.show(getSupportFragmentManager(), "counter offer dialog");
    }

    public void openAcceptOfferDialog(View view) {
        AcceptOfferDialog offerDialog = new AcceptOfferDialog();
        offerDialog.show(getSupportFragmentManager(), "accept offer dialog");
    }

    public void openConfirmCounterOfferDialog(View view) {
        CounterOfferConfirmDialog counterOfferConfirmDialogfferDialog = new CounterOfferConfirmDialog();
        counterOfferConfirmDialogfferDialog.show(getSupportFragmentManager(), "accept counter offer dialog");
    }

    @Override
    public void dialogOfferAccepted() {
        acceptOffer();
    }

    public void goToHome(View view) {
        if (myPrefs.getString("userType","none").equals("bidder")) {
            Intent intent = new Intent(this, BidderHomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PetitionerHomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void dialogConfirmCounterOffer() {
        acceptCounterOffer();
    }

    public void acceptCounterOffer() {
        offerDBReference.child(activeOffer.getId()).child("cost").setValue(activeOffer.getCounterOfferCost());
        btnAcceptCounterOffer.setVisibility(View.GONE);
        Toast.makeText(this, "Se aceptó la contraoferta", Toast.LENGTH_LONG).show();
    }
}
