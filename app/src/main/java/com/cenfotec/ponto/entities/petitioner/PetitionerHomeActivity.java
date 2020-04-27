package com.cenfotec.ponto.entities.petitioner;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Notification;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.notification.NotificationFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.TabLayoutAdapter_PetitionerHome;

public class PetitionerHomeActivity extends AppCompatActivity {

  ViewPager viewPager;
  TabLayout tabLayout;
  TabLayoutAdapter_PetitionerHome adapter;
  List<Notification> notificationList = new ArrayList<>();
  User user;
  Map notificationHistory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_petitioner_home);
    notificationHistory  = new HashMap<>();
    //SharedPreferences myPrefs = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    //myPrefs.edit().putString("notificationsIdsPetitioner", "").apply();
    bindContent();
    initContent();
    catchIntent();
    chargeUser();
    Locale spanish = new Locale("es", "ES");
    Locale.setDefault(spanish);
  }

  private void chargeUser() {
    SharedPreferences myPrefs = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    String userId = myPrefs.getString("userId", "none");

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    Query query = ref.child(userId);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        user = dataSnapshot.getValue(User.class);
        assert user != null;
        if (user.isAllowsPushNotifications()) {
          chargeNotification();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void bindContent() {
    viewPager = findViewById(R.id.homeView);
    tabLayout = findViewById(R.id.petitionerHomeNavbar);
  }

  private void catchIntent() {
    if (getIntent().getExtras() != null) {
      TabLayout.Tab tab = tabLayout.getTabAt(3);
      tab.select();
      changeView(3);

    }
  }

  private void initContent() {
    adapter = new TabLayoutAdapter_PetitionerHome(getSupportFragmentManager(), tabLayout.getTabCount());
    viewPager.setAdapter(adapter);
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        changeView(tab.getPosition());
        tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });
  }

  public void changeView(int position) {
    adapter.setActViewPos(position);
    viewPager.setAdapter(adapter);
  }


  public void chargeNotification() {
    final SharedPreferences myPrefs = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    String userId = myPrefs.getString("userId", "none");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
    Query query = ref.orderByChild("userId").equalTo(userId);
    query.addValueEventListener(new ValueEventListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        notificationList.clear();
        chargeNotificationHistory();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          Notification notification = snapshot.getValue(Notification.class);
          assert notification != null;
          if (notification.getUserId().equals(myPrefs.getString("userId", "none"))) {
            if (!notification.isShow() && !notificationHistory.containsKey(notification.getId())) {
              //notificationHistory.put(notification.getId(), notification);
              notificationList.add(notification);
            }
          }
        }
        if (notificationList.size() > 0) {
          showNotification();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }


  @RequiresApi(api = Build.VERSION_CODES.N)
  public void showNotification() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
    SharedPreferences myPrefs = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    final Map notificationMap = new HashMap<>();
    String s = "";
    if (!myPrefs.getString("notificationsIdsPetitioner", "").equals("")) {
      s = myPrefs.getString("notificationsIdsPetitioner", "");
    }

    for (Notification notification : notificationList) {
        NotificationFactory.createNotificationWithoutExtras(this, notification);
        notificationMap.put(notification.getId() + "/show", true);
        s = s + " " + notification.getId();
    }
    myPrefs.edit().putString("notificationsIdsPetitioner", s).apply();
    ref.updateChildren(notificationMap);
  }

  private void chargeNotificationHistory() {
    SharedPreferences myPrefs = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    String s = "";
    String[] splitedString;
    notificationHistory = new HashMap();
    if (!myPrefs.getString("notificationsIdsPetitioner", "").equals("")) {
      s = myPrefs.getString("notificationsIdsPetitioner", "");
      splitedString = s.split(" ");

      for (String string : splitedString) {
        notificationHistory.put(string, "");
      }
    }
  }
}
