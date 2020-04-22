package com.cenfotec.ponto;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cenfotec.ponto.entities.notification.NotificationFactory;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.cenfotec.ponto.entities.user.UserHomeActivity;

public class MainActivity extends AppCompatActivity {
  public static final String MY_PREFERENCES = "MyPrefs";
  Button btnPetitionList;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SharedPreferences myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
    String userId = myPrefs.getString("userId", "none");
    Intent intent;

    if (userId.equals("none")) {
      intent = new Intent(this, LoginActivity.class);
      startActivity(intent);
      finish();
    } else {
      intent = new Intent(this, UserHomeActivity.class);
      startActivity(intent);
      finish();
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = NotificationFactory.NOTIFICATION_CHANNEL_NAME;
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(NotificationFactory.NOTIFICATION_CHANNEL_ID, name, importance);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

}
