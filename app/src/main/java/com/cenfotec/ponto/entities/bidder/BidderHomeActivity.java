package com.cenfotec.ponto.entities.bidder;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Notification;
import com.cenfotec.ponto.entities.notification.NotificationFactory;
import com.cenfotec.ponto.utils.GeneralActivity;
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

import adapter.TabLayoutAdapter_BidderHome;

public class BidderHomeActivity extends GeneralActivity {


  protected TabLayoutAdapter_BidderHome viewPagerAdapter;
  TabLayoutAdapter_BidderHome adapter;
  List<Notification> notificationList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bidder_home);
    super.initComponents(R.id.homeView, R.id.bidderHomeNavbar);
    setTabs();
    bindContent();
    initContent();
    catchIntent();
    chargeNotification();
    Locale spanish = new Locale("es", "ES");
    Locale.setDefault(spanish);
  }

  private void bindContent() {
    activityViewPager = findViewById(R.id.homeView);
    tabLayout = findViewById(R.id.bidderHomeNavbar);
  }

  private void catchIntent() {
    if (getIntent().getExtras() != null) {
      TabLayout.Tab tab = tabLayout.getTabAt(4);
      tab.select();

    }
  }

  private void initContent() {
    adapter = new TabLayoutAdapter_BidderHome(getSupportFragmentManager(), tabLayout.getTabCount());
    activityViewPager.setAdapter(adapter);
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        changeView(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });
  }

  public void chargeNotification() {
    SharedPreferences myPrefs = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    String userId = myPrefs.getString("userId", "none");

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
    Query query = ref.orderByChild("userId").equalTo(userId);
    query.addValueEventListener(new ValueEventListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
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


  @RequiresApi(api = Build.VERSION_CODES.N)
  public void showNotification() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
    final Map notificationMap = new HashMap<>();

    for (Notification notification : notificationList) {
      NotificationFactory.createNotificationWithoutExtras(this, notification);
      notificationMap.put(notification.getId() + "/show", true);
    }
    ref.updateChildren(notificationMap);
  }

  @Override
  protected void chargeAdapterViews() {
    viewPagerAdapter = new TabLayoutAdapter_BidderHome(getSupportFragmentManager());
    activityViewPager.setAdapter(viewPagerAdapter);
  }

  @Override
  protected void setTabs() {
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        changeView(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });
    catchIntent();
  }


  public void changeView(int position) {
    viewPagerAdapter.setActViewPos(position);
    activityViewPager.setAdapter(viewPagerAdapter);
  }
}
