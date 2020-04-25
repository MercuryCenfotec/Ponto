package com.cenfotec.ponto.entities.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Notification;
import com.cenfotec.ponto.entities.rating.RateUserDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import adapter.NotificationCard_Adapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationListFragment extends Fragment implements NotificationCard_Adapter.NotificationClickListener {
  private View view;
  public static final String MY_PREFERENCES = "MyPrefs";
  private String userId;
  private SharedPreferences sharedPreferences;
  private List<Notification> notificationList = new ArrayList<>();
  private NotificationCard_Adapter notificationCard_adapter;
  private RecyclerView recyclerview;
  private DatabaseReference notifDBReference;

  public NotificationListFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_notification_list, container, false);
    initContent();
    setContent();
    chargeNotifications();

    return view;
  }

  private void chargeNotifications() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
    Query query = ref.orderByChild("userId").equalTo(userId);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        notificationList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          Notification notification = snapshot.getValue(Notification.class);
          notificationList.add(notification);
        }
        notificationCard_adapter.notifyDataSetChanged();
        setNotificationRead();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void setNotificationRead() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
    final Map notificationMap = new HashMap<>();

    notificationList.forEach(new Consumer<Notification>() {
      @Override
      public void accept(Notification notification) {
        if(!notification.isRead()){
          notificationMap.put(notification.getId() + "/read", true);
        }
      }
    });
    ref.updateChildren(notificationMap);
  }


  private void setContent() {
    RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    notificationCard_adapter = new NotificationCard_Adapter(getActivity(), notificationList, (NotificationCard_Adapter.NotificationClickListener) this);

    recyclerview.setLayoutManager(layoutManager);
    recyclerview.setItemAnimator(new DefaultItemAnimator());
    recyclerview.setAdapter(notificationCard_adapter);
  }

  private void initContent() {

    notifDBReference = FirebaseDatabase.getInstance().getReference("Notifications");

    recyclerview = (view).findViewById(R.id.recycler);
    sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
    userId = sharedPreferences.getString("userId", "");

  }

  @Override
  public void onNotificationClicked(final String bidderId, final String petitionerId, final String notificationId) {

    notifDBReference.child(notificationId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (!dataSnapshot.getValue(Notification.class).isDone()) {
          RateUserDialog rateUserDialog = new RateUserDialog(bidderId, petitionerId, notificationId);
          rateUserDialog.show(getChildFragmentManager(), "membership acquisition dialog");
        } else {
          Toast.makeText(getContext(), "Ya calificó al usuario", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
}
