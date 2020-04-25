package com.cenfotec.ponto.entities.bidder;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Notification;
import com.cenfotec.ponto.data.model.Rating;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.notification.NotificationFactory;
import com.cenfotec.ponto.entities.rating.RateUserDialog;
import com.cenfotec.ponto.utils.GeneralActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.TabLayoutAdapter_BidderHome;

public class BidderHomeActivity extends GeneralActivity implements RateUserDialog.RatePetDialogConfirmListener {

  private DatabaseReference ratingDBReference;
  private DatabaseReference userDBReference;
  private DatabaseReference notifDBReference;
  protected TabLayoutAdapter_BidderHome viewPagerAdapter;
  TabLayoutAdapter_BidderHome adapter;
  List<Notification> notificationList = new ArrayList<>();
  User user;
  private ArrayList<Rating> petitionerRatings;
  private boolean hasBeenRated = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bidder_home);
    super.initComponents(R.id.homeView, R.id.bidderHomeNavbar);
    setTabs();
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

    ratingDBReference = FirebaseDatabase.getInstance().getReference("Ratings");
    userDBReference = FirebaseDatabase.getInstance().getReference("Users");
    notifDBReference = FirebaseDatabase.getInstance().getReference("Notifications");
    petitionerRatings = new ArrayList<>();

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

  @Override
  public void dialogConfirmPetRating(float rating, String bidderId, String petitionerId, String notificationId) {
    getAllRatingsByUser(rating, petitionerId, bidderId, notificationId);
  }

  private void getAllRatingsByUser(final float rating, final String petId, final String bidId, final String notificationId) {

    ratingDBReference.orderByChild("userId").equalTo(petId).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          petitionerRatings.add(snapshot.getValue(Rating.class));
        }

        if (!hasBeenRated) {
          rateUser(rating, petId, bidId, notificationId);
          hasBeenRated = true;
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void rateUser(float rating, String petId, String bidId, String notificationId) {
    int totalRating = 0;
    int numRatings = petitionerRatings.size();

    // Según el total de rating que ha tenido el usuario a calificar, sumar la totalidad de las calificaciones
    for (int i = 0; i < petitionerRatings.size(); i++) {
      totalRating += petitionerRatings.get(i).getRating();
    }

    // Guardar en la base de datos en nuevo rating
    String ratingId = ratingDBReference.push().getKey();
    Rating myRating = new Rating(ratingId, petId, bidId, rating);
    ratingDBReference.child(ratingId).setValue(myRating);

    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setDecimalSeparator('.');
    DecimalFormat df = new DecimalFormat("###.##");
    df.setDecimalFormatSymbols(symbols);

    // Actualizar el rating del usuario tomando la totalidad que se sumó arriba (además del rating nuevo) y dividirla por la cantidad total de ratings que tiene el user
    Float newRating = (totalRating + rating) / (numRatings + 1);
    userDBReference.child(petId).child("rating").setValue(Float.parseFloat(df.format(newRating)));

    notifDBReference.child(notificationId).child("done").setValue(true);
  }

}
