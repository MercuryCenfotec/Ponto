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
import com.cenfotec.ponto.data.model.Contract;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.contract.GeneratedContractActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OfferDetailActivity extends AppCompatActivity implements CounterOfferDialog.CounterOfferDialogListener, AcceptOfferDialog.AcceptOfferDialogListener {

    private static SharedPreferences sharedpreferences;
    DatabaseReference offerDBReference;
    DatabaseReference bidderDBReference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);
        offerDBReference = FirebaseDatabase.getInstance().getReference("Offers");
        bidderDBReference = FirebaseDatabase.getInstance().getReference("Bidders");
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

        String userId = myPrefs.getString("userId", "none");
        offerId = myPrefs.getString("offerId","none");

        loadOfferData(userId, offerId);

    }

    private void loadOfferData(final String userId, String offerId) {
        Query offerQuery = offerDBReference.orderByChild("id").equalTo(offerId);

        offerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    activeOffer = data.getValue(Offer.class);

                    costText.setText("₡" + data.child("cost").getValue().toString());
                    descriptionText.setText(data.child("description").getValue().toString());
                    durationText.setText(data.child("duration").getValue().toString() + (data.child("durationType").getValue().toString().equals("hour") ? " horas" : " días"));
                    durationTypeText.setText(data.child("durationType").getValue().toString().equals("hour") ? "Por hora" : "Por día");
                    viewTitle.setText(data.child("servicePetitionTitle").getValue().toString());

                    if (data.child("counterOffer").getValue() != null) {
                        hasCounterOffer = data.child("counterOffer").getValue().toString().equals("true");
                    }

                    if (!data.child("userId").getValue().toString().equals(userId)) {
                        bidderDetail.setVisibility(View.VISIBLE);
                        counterOfferButton.setVisibility(View.VISIBLE);
                        createOfferButton.setVisibility(View.GONE);
                        userName.setText(data.child("bidderName").getValue().toString());
                        if (!data.child("bidderImageUrl").getValue().toString().equals("")) {
                            Picasso.get().load(data.child("bidderImageUrl").getValue().toString()).into(userImage);
                        }
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

    public void acceptOffer() {
        String servicePetitionId = myPrefs.getString("servicePetitionId", "none");
        final String offerId = myPrefs.getString("offerId", "none");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Offers");
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("ServicePetitions");

        Query offersQuery = ref.orderByChild("servicePetitionId").equalTo(servicePetitionId);

        offersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot offerSS : dataSnapshot.getChildren()) {
                    ref.child(offerSS.child("id").getValue().toString()).child("accepted")
                            .setValue(offerSS.child("id").getValue().toString().equals(offerId) ?
                                    "accepted" :
                                    "cancelled");
                }

                Toast.makeText(OfferDetailActivity.this, "La oferta fue aceptada", Toast.LENGTH_LONG).show();
                registerContractToDB();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref2.child(servicePetitionId).child("acceptedOfferId").setValue(offerId);

    }

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
        databaseReference.child(contractId).setValue(contract);



        myPrefs.edit().putString("contractId", contractId).commit();
        Intent intent = new Intent(this, GeneratedContractActivity.class);
        intent.putExtra("petitionerId", contract.getPetitionerId());
        intent.putExtra("bidderUserId", contract.getBidderId());
        intent.putExtra("contractId", contract.getId());
        startActivity(intent);
        finish();
    }

    public void createCounterOffer(String newCost) {
        activeOffer.setCounterOffer(true);
        activeOffer.setCounterOfferCost(Float.parseFloat(newCost));
        offerDBReference.child(offerId).setValue(activeOffer);
        hasCounterOffer = true;
        Toast.makeText(this, "Contraoferta exitosa", Toast.LENGTH_LONG).show();
    }

    @Override
    public void applyText(String newCost) {
        createCounterOffer(newCost);
    }

    public void openCounterOfferDialog(View view) {
        if (!hasCounterOffer) {
            CounterOfferDialog counterDialog = new CounterOfferDialog();
            counterDialog.show(getSupportFragmentManager(), "counter offer dialog");
        } else {
            Toast.makeText(this, "Ya hizo una contraoferta", Toast.LENGTH_LONG).show();
        }
    }

    public void openAcceptOfferDialog(View view) {
        AcceptOfferDialog offerDialog = new AcceptOfferDialog();
        offerDialog.show(getSupportFragmentManager(), "accept offer dialog");
    }

    @Override
    public void dialogOfferAccepted() {
        acceptOffer();
    }
}
