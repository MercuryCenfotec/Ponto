package com.cenfotec.ponto.entities.offer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.data.model.Task;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OfferCreationActivity extends AppCompatActivity {

    DatabaseReference offerDBReference;
    DatabaseReference bidderDBReference;
    TextView createOfferButton;
    EditText costInput;
    EditText durationInput;
    EditText descriptionInput;
    Offer offer = new Offer();
    public static final String MY_PREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_creation);
        offerDBReference = FirebaseDatabase.getInstance().getReference("Offers");
        bidderDBReference = FirebaseDatabase.getInstance().getReference("Bidders");
        createOfferButton = findViewById(R.id.createOfferButton);
        costInput = findViewById(R.id.costEditText);
        durationInput = findViewById(R.id.durationEditText);
        descriptionInput = findViewById(R.id.descriptionEditText);
        offer.setDurationType("hour");
    }

    public void changeDurationType(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String type = "";

        switch (view.getId()) {
            case R.id.radioHour:
                if (checked) type = "hour";
                break;
            case R.id.radioDay:
                if (checked) type = "day";
                break;
            default:
                break;
        }
        offer.setDurationType(type);
    }

    public void createOffer(View view) {
        if (validForm()) {

            SharedPreferences myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
            String userId = myPrefs.getString("userId", "none");
            final String servicePetitionId = myPrefs.getString("servicePetitionId","none");

            Query bidderQuery = bidderDBReference.orderByChild("userId").equalTo(userId);

            bidderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String key = offerDBReference.push().getKey();
                        String bidderIdKey = data.child("id").getValue().toString();

                        offer.setBidderId(bidderIdKey);
                        offer.setDuration(Float.parseFloat(durationInput.getText().toString()));
                        offer.setDescription(descriptionInput.getText().toString());
                        offer.setCost(Float.parseFloat(costInput.getText().toString()));
                        offer.setCounterOffer(false);
                        offer.setAccepted(false);
                        offer.setId(key);
                        offer.setServicePetitionId(servicePetitionId);

                        offerDBReference.child(key).setValue(offer);
                        goToHome();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The bidder read failed: " + databaseError.getCode());
                }
            });
        }
    }

    private void goToHome(){
        showToaster("Oferta realizada con éxito");
        Intent intent = new Intent(this, BidderHomeActivity.class);
        startActivity(intent);
    }

    private boolean validForm() {
        if (costInput.getText().toString().equals("") || durationInput.getText().toString().equals("") || descriptionInput.getText().toString().equals("")) {
            showToaster("Campos vacíos.");
            return false;
        }
        return true;
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
