package com.cenfotec.ponto.entities.offer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cenfotec.ponto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OfferDetailActivity extends AppCompatActivity {

    DatabaseReference offerDBReference;
    DatabaseReference bidderDBReference;
    public static final String MY_PREFERENCES = "MyPrefs";
    TextView costText;
    TextView durationTypeText;
    TextView durationText;
    TextView descriptionText;
    SharedPreferences myPrefs;

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
        String userId = myPrefs.getString("userId", "none");
//        String servicePetitionId = myPrefs.getString("servicePetitionId","none");
        final String servicePetitionId = myPrefs.getString("servicePetitionId", "-M2oldNjlxrtUUywl3pc");

        Query bidderQuery = bidderDBReference.orderByChild("userId").equalTo(userId);

        bidderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String bidderId = data.child("id").getValue().toString();
                    loadOfferData(bidderId, servicePetitionId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The bidder read failed: " + databaseError.getCode());
            }
        });

    }

    private void loadOfferData(final String bidderId, String petitionId) {
        Query offerQuery = offerDBReference.orderByChild("servicePetitionId").equalTo(petitionId);

        offerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("bidderId").getValue().toString().equals(bidderId)) {
                        costText.setText("₡" + data.child("cost").getValue().toString());
                        descriptionText.setText(data.child("description").getValue().toString());
                        durationText.setText(data.child("duration").getValue().toString() + (data.child("durationType").getValue().toString().equals("hour") ? " horas" : " días"));
                        durationTypeText.setText(data.child("durationType").getValue().toString().equals("hour") ? "Por hora" : "Por día");
                        myPrefs.edit().putString("offerId",data.child("id").getValue().toString()).commit();
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
}
