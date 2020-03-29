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

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OfferDetailActivity extends AppCompatActivity {

    DatabaseReference offerDBReference;
    DatabaseReference bidderDBReference;
    public static final String MY_PREFERENCES = "MyPrefs";
    TextView costText;
    TextView durationTypeText;
    TextView durationText;
    TextView descriptionText;
    SharedPreferences myPrefs;
    User user;
    TextView userName;
    ImageView userImage;
    LinearLayout bidderDetail;

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
        bidderDetail.setVisibility(View.INVISIBLE);

        String userId = myPrefs.getString("userId", "none");
//        String servicePetitionId = myPrefs.getString("servicePetitionId","none");
        final String servicePetitionId = myPrefs.getString("servicePetitionId", "-M2oldNjlxrtUUywl3pc");

        loadOfferData(userId, servicePetitionId);

    }

    private void loadOfferData(final String userId, String petitionId) {
        Query offerQuery = offerDBReference.orderByChild("servicePetitionId").equalTo(petitionId);

        offerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("userId").getValue().toString().equals(userId)) {
                        costText.setText("₡" + data.child("cost").getValue().toString());
                        descriptionText.setText(data.child("description").getValue().toString());
                        durationText.setText(data.child("duration").getValue().toString() + (data.child("durationType").getValue().toString().equals("hour") ? " horas" : " días"));
                        durationTypeText.setText(data.child("durationType").getValue().toString().equals("hour") ? "Por hora" : "Por día");
                        myPrefs.edit().putString("offerId",data.child("id").getValue().toString()).commit();

                        if (!data.child("userId").getValue().toString().equals(userId)) {
                            bidderDetail.setVisibility(View.VISIBLE);
                            loadUserData(userId);
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

    private void loadUserData(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();

                userName.setText(dataSnapshot.child("fullName").getValue().toString());
                if(!imageUrl.equals("")){
                    Picasso.get().load(imageUrl).into(userImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void goToOfferUpdate(View view) {
        Intent intent = new Intent(this, OfferUpdateActivity.class);
        startActivity(intent);
    }
}
