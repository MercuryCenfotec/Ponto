package com.cenfotec.ponto.entities.bidder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BidderUpdateActivity extends AppCompatActivity {

    DatabaseReference userDatabaseReference;
    DatabaseReference bidderDatabaseReference;
    EditText updateIdentificationEditText;
    EditText updateFullNameEditText;
    EditText updateBirthDateEditText;
    EditText updateEmailEditText;
    EditText updateBiographyEditText;
    User updatedUser;
    Bidder updatedBidder;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_update);
        initViewControls();
        getUserByIntentToken();
    }

    //Initialization statements start here
    private void initViewControls() {
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        bidderDatabaseReference = FirebaseDatabase.getInstance().getReference("Bidders");
        updateFullNameEditText = findViewById(R.id.fullNameUpdBidEditText);
        updateBirthDateEditText = findViewById(R.id.birthDateUpdBidEditText);
        updateEmailEditText = findViewById(R.id.emailUpdBidEditText);
        updateIdentificationEditText = findViewById(R.id.idUpdBidEditText);
        updateBiographyEditText = findViewById(R.id.biographyUpdBidEditText);

        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("bidderId");
        }
    }

    private void fillUpdateFields() {
        updateFullNameEditText.setText(updatedUser.getFullName());
        updateBirthDateEditText.setText(updatedUser.getBirthDate());
        updateEmailEditText.setText(updatedUser.getEmail());
        updateIdentificationEditText.setText(updatedUser.getIdentificationNumber());
        updateBiographyEditText.setText(updatedBidder.getBiography());
    }

    private void getUserByIntentToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getUserByIdQuery = databaseReference.child("Users").orderByChild("id").equalTo(userId);
        getUserByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    updatedUser = userSnapshot.getValue(User.class);
                    getBidderByUserId();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getBidderByUserId(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByUserIdQuery =
                databaseReference.child("Bidders").orderByChild("userId").equalTo(userId);
        getBidderByUserIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bidderSnapshot : dataSnapshot.getChildren()) {
                    updatedBidder = bidderSnapshot.getValue(Bidder.class);
                    fillUpdateFields();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Update statements start here
    public void preBidderModification(View view) {
        if(!showErrorOnBlankSpaces() && isValidEmail()){
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean userFound = false;
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                if (updateEmailEditText.getText().toString().
                                        equals(userSnapshot.child("email").getValue().toString())) {
                                    userFound = true;
                                    showToaster("Email existente");
                                }
                            }
                            if (!userFound) {
                                updateUserBidder();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
        }else{
            showToaster("Verificar campos");
        }
    }

    private void updateUserBidder() {
        DatabaseReference updateUserReference = FirebaseDatabase.getInstance().
                getReference("Users").child(userId);
        DatabaseReference updateBidderReference = FirebaseDatabase.getInstance().
                getReference("Bidders").child(updatedBidder.getId());
        updatedUser.setFullName(updateFullNameEditText.getText().toString());
        updatedUser.setEmail(updateEmailEditText.getText().toString());
        updatedBidder.setBiography(updateBiographyEditText.getText().toString());
        updateUserReference.setValue(updatedUser);
        updateBidderReference.setValue(updatedBidder);
        showToaster("Actualización exitosa");
        openBidderProfile();
    }

    //Other method statements start here
    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBack(View view) {
        finish();
    }

    private void openBidderProfile() {
        Intent intent = new Intent(BidderUpdateActivity.this,
                BidderProfileActivity.class);
        startActivity(intent);
    }

    //Validation statements start here
    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{updateFullNameEditText, updateEmailEditText,
                updateBiographyEditText};
        for (EditText editText : editTextsList) {
            if (editText.getText().toString().equals("")) {
                editText.setHintTextColor(Color.parseColor("#c0392b"));
                editText.setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editText.setBackgroundResource(R.drawable.rect_black);
                editText.setHintTextColor(Color.parseColor("#000000"));
            }
        }
        return isEmpty;
    }

    private boolean isValidEmail() {
        String email = updateEmailEditText.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            updateEmailEditText.setBackgroundResource(R.drawable.rect_black);
            updateEmailEditText.setHintTextColor(Color.parseColor("#000000"));
            return true;
        } else {
            updateEmailEditText.setBackgroundResource(R.drawable.edittext_error);
            updateEmailEditText.setHintTextColor(Color.parseColor("#c0392b"));
            return false;
        }
    }
}
