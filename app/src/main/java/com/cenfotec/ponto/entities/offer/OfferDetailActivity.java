package com.cenfotec.ponto.entities.offer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OfferDetailActivity extends AppCompatActivity implements CounterOfferDialog.CounterOfferDialogListener {

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

    public void acceptOffer(View view) {
        String servicePetitionId = myPrefs.getString("servicePetitionId", "none");
        final String offerId = myPrefs.getString("offerId", "none");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Offers");

        Query offersQuery = ref.orderByChild("servicePetitionId").equalTo(servicePetitionId);

        offersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot offerSS : dataSnapshot.getChildren()) {
                    ref.child(offerSS.child("id").getValue().toString()).child("accepted")
                            .setValue(offerSS.child("id").equals(offerId) ?
                                    "accepted" :
                                    "cancelled");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
}
