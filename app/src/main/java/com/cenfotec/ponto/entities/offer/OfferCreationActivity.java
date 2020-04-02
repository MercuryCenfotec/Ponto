package com.cenfotec.ponto.entities.offer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Offer;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class OfferCreationActivity extends AppCompatActivity {

    DatabaseReference offerDBReference;
    DatabaseReference bidderDBReference;
    EditText costInput;
    EditText durationInput;
    EditText descriptionInput;
    TextView costInputLabel;
    Offer offer = new Offer();
    public static final String MY_PREFERENCES = "MyPrefs";
    final DecimalFormat costFormat = new DecimalFormat("###,###.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_creation);
        offerDBReference = FirebaseDatabase.getInstance().getReference("Offers");
        bidderDBReference = FirebaseDatabase.getInstance().getReference("Bidders");
        costInput = findViewById(R.id.costEditText);
        costInputLabel = findViewById(R.id.costInputLabel);
        durationInput = findViewById(R.id.durationEditText);
        descriptionInput = findViewById(R.id.descriptionEditText);
        offer.setDurationType("hour");

        costInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    String cost = costFormat.format(Double.parseDouble(s.toString()));
                    costInputLabel.setText(cost);
                } else {
                    costInputLabel.setText(" ");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
            String key = offerDBReference.push().getKey();

            offer.setId(key);
            offer.setAccepted("pending");
            offer.setCounterOffer(false);
            offer.setCounterOfferCost(0f);
            offer.setBidderName(myPrefs.getString("userName","none"));
            offer.setDescription(descriptionInput.getText().toString());
            offer.setCost(Float.parseFloat(costInput.getText().toString()));
            offer.setUserId(myPrefs.getString("userId", "none"));
            offer.setDuration(Float.parseFloat(durationInput.getText().toString()));
            offer.setServicePetitionId(myPrefs.getString("servicePetitionId","none"));
            offer.setServicePetitionTitle(myPrefs.getString("servicePetitionTitle", "none"));
            offer.setBidderImageUrl(myPrefs.getString("userImageUrl", "none"));

            offerDBReference.child(key).setValue(offer);
            goToHome();
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

    public void finishActivity(View view) {
        finish();
    }
}
