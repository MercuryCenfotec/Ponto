package com.cenfotec.ponto.entities.petitioner;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Notification;
import com.cenfotec.ponto.entities.notification.NotificationFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.TabLayoutAdapter_PetitionerHome;

public class PetitionerHomeActivity extends AppCompatActivity {

  ViewPager viewPager;
  TabLayout tabLayout;
  TabLayoutAdapter_PetitionerHome adapter;
  List<Notification> notificationList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_petitioner_home);
    bindContent();
    initContent();
    catchIntent();
    chargeNotification();
    Locale spanish = new Locale("es", "ES");
    Locale.setDefault(spanish);
  }

  private void bindContent() {
    viewPager = findViewById(R.id.homeView);
    tabLayout = findViewById(R.id.petitionerHomeNavbar);
  }

  private void catchIntent() {
    if (getIntent().getExtras() != null) {
      TabLayout.Tab tab = tabLayout.getTabAt(4);
      tab.select();
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
    SharedPreferences myPrefs = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    String userId = myPrefs.getString("userId", "none");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
    Query query = ref.orderByChild("userId").equalTo(userId);
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        notificationList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          Notification notification = snapshot.getValue(Notification.class);
          if (!notification.isShow()) {
            notificationList.add(notification);
          }
        }
        showNotification();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  public void showNotification() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
    DatabaseReference query;

    for (Notification notification : notificationList) {
      notification.setShow(true);
      NotificationFactory.createNotificationWithoutExtras(this, notification);
//    ref.child(notification.getId()).setValue(notification);
    }

  }
}
