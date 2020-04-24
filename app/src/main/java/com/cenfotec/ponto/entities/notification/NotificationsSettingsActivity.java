package com.cenfotec.ponto.entities.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NotificationsSettingsActivity extends AppCompatActivity {

  Switch switchNotification;
  ImageView btnImageReturn;
  String userId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notifications_settings);
    setComponent();
  }

  private void setComponent() {

    switchNotification = findViewById(R.id.switchNotification);
    btnImageReturn = findViewById(R.id.btnImageReturn);
    btnImageReturn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        closeActivity();
      }
    });
    switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateUser(isChecked);
      }
    });
    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    userId = sharedPreferences.getString("userId", "");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    Query query = ref.child(userId);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        switchNotification.setChecked(user.isAllowsPushNotifications());
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void updateUser(boolean isChecked) {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    ref.child(userId).child("allowsPushNotifications").setValue(isChecked);
  }

  private void closeActivity() {
    finish();
  }
}
