package com.cenfotec.ponto.entities.offer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OfferUpdateActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences myPrefs;
    DatabaseReference offerDBReference;
    TextView updateOfferButton;
    EditText costInput;
    EditText durationInput;
    EditText descriptionInput;
    Offer offer = new Offer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        offerDBReference = FirebaseDatabase.getInstance().getReference("Offers");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_update);
        costInput = findViewById(R.id.costEditText);
        durationInput = findViewById(R.id.durationEditText);
        updateOfferButton = findViewById(R.id.createOfferButton);
        descriptionInput = findViewById(R.id.descriptionEditText);
        myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String offerId = myPrefs.getString("offerId", "none");

        Query offerQuery = offerDBReference.orderByChild("id").equalTo(offerId);

        offerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    costInput.setText(data.child("cost").getValue().toString());
                    durationInput.setText(data.child("duration").getValue().toString());
                    descriptionInput.setText(data.child("description").getValue().toString());
                    switch (data.child("durationType").getValue().toString()) {
                        case "hour":
                            RadioButton hourRadio = findViewById(R.id.radioHour);
                            hourRadio.setChecked(true);
                            break;
                        case "day":
                            RadioButton dayRadio = findViewById(R.id.radioDay);
                            dayRadio.setChecked(true);
                            break;
                    }
                    offer = data.getValue(Offer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The offer read failed: " + databaseError.getCode());
            }
        });
    }

    public void updateOffer(View view) {
        if (!validForm()) {
            offer.setDuration(Float.parseFloat(durationInput.getText().toString()));
            offer.setDescription(descriptionInput.getText().toString());
            offer.setCost(Float.parseFloat(costInput.getText().toString()));
            offerDBReference.child(offer.getId()).setValue(offer);
            goToOfferDetail();
        } else {
            showToaster("Verificar campos");
        }
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

    private boolean validForm() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{costInput, durationInput,
                descriptionInput};
        for (EditText editText : editTextsList) {
            if (editText.getText().toString().equals("")) {
                editText.setHintTextColor(Color.parseColor("#c0392b"));
                editText.setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editText.setBackgroundResource(R.drawable.rect_black);
                editText.setHintTextColor(Color.parseColor("#b6b6b6"));
            }
        }
        return isEmpty;
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void goToOfferDetail() {
        showToaster("Oferta modificada exitosamente");
        Intent intent = new Intent(this, OfferDetailActivity.class);
        startActivity(intent);
    }

    public void finishActivity(View view) {
        finish();
    }
}
