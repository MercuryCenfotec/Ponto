package com.cenfotec.ponto.entities.bidder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Scanner;

import adapter.ProfileAdapter;
import customfonts.MyTextView_SF_Pro_Display_Medium;
import model.ProfileModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BidderProfileFragment extends Fragment {

  private static SharedPreferences sharedpreferences;
  private String activeUserId;
  private ProfileAdapter profileAdapter;
  private RecyclerView recyclerview;
  private ArrayList<ProfileModel> profileModelArrayList;
  User user;
  Integer inbox[] = {R.drawable.ic_calendar, R.drawable.ic_star, R.drawable.ic_like, R.drawable.ic_paypal,
          R.drawable.ic_contract, R.drawable.ic_profile, R.drawable.ic_settings};
  Integer arrow = R.drawable.ic_chevron_right_black_24dp;
  String txttrades[] = {"Agenda de proyectos", "Membresias", "Cuenta interna", "Contratos",
          "Mi perfil", "Ajustes de Notificaciones", "Cerrar sesión"};
  String txthistory[] = {"Revise sus contrataciones",
          "Revise su membresia a la aplicacion", "Administe su cuenta interna", "Contratos realizados", "Cambie la información de su perfil", "Configure las notificaciones de la aplicaciòn en su dispositivo", "Cierre la sesión"};

  TextView profileFullName;
  TextView profileEmail;
  TextView profileRating;
  TextView profileBiography;
  ImageView profileImage;
  Bidder bidder;
  CustomDatePickerDialog customDatePickerDialog;
  View view;
  MyTextView_SF_Pro_Display_Medium offersNumberView;
  Integer offersNumber = 0;

  public BidderProfileFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_bidder_profile, container, false);
    initProfileControls();
    getActiveUserId();
    getUserByActiveUserId();
    showRecyclerViewOptions();
    getOffersNumber();
    return view;
  }

  private void getOffersNumber() {
    final DatabaseReference offerDBReference = FirebaseDatabase.getInstance().getReference("Offers");
    Query query = offerDBReference.orderByChild("userId").equalTo(activeUserId);

    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot data : snapshot.getChildren()) {
          offersNumber++;
        }
        offersNumberView.setText("" + offersNumber);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        System.out.println("The offers read failed: " + databaseError.getCode());
      }
    });
  }

  private void initProfileControls() {
    offersNumberView = view.findViewById(R.id.offersNumberView);
    profileFullName = view.findViewById(R.id.bidderFullNameProfile);
    profileEmail = view.findViewById(R.id.bidderMailProfile);
    profileRating = view.findViewById(R.id.bidderRatingProfile);
    profileBiography = view.findViewById(R.id.bidderBiographyProfile);
    profileImage = view.findViewById(R.id.imgBidderProfile);
    bidder = new Bidder();
    user = new User();
    customDatePickerDialog = new CustomDatePickerDialog();
  }

  private void showRecyclerViewOptions() {
    recyclerview = view.findViewById(R.id.bidderProfileOptions);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerview.setLayoutManager(layoutManager);
    recyclerview.setItemAnimator(new DefaultItemAnimator());

    profileModelArrayList = new ArrayList<>();

    for (int i = 0; i < inbox.length; i++) {
      ProfileModel view = new ProfileModel(inbox[i], arrow, txttrades[i], txthistory[i],
              activeUserId, "bidder");
      profileModelArrayList.add(view);
    }

    profileAdapter = new ProfileAdapter(getActivity(), profileModelArrayList);
    recyclerview.setAdapter(profileAdapter);
  }

  private void getActiveUserId() {
    sharedpreferences = getActivity().getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
    activeUserId = sharedpreferences.getString("userId", "");
  }

  private void getUserByActiveUserId() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    ref.child(activeUserId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        user = dataSnapshot.getValue(User.class);
        getBidderByUserId();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }


  private void getBidderByUserId() {
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Query getBidderByIdQuery =
            databaseReference.child("Bidders").orderByChild("userId").equalTo(activeUserId);
    getBidderByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
          bidder = bidderSnapshot.getValue(Bidder.class);
          showBidderProfileInformation();
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getCode());
      }
    });
  }

  private void showBidderProfileInformation() {
    // Convert first letter to capital
    StringBuilder capitalized = new StringBuilder();
    Scanner lineScan = new Scanner(user.getFullName().toLowerCase());
    while (lineScan.hasNext()) {
      String word = lineScan.next();
      capitalized.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
    }

    if (!user.getProfileImageUrl().equals("")) {
      Picasso.get().load(user.getProfileImageUrl()).into(profileImage);
    }
    profileFullName.setText(capitalized);
    profileEmail.setText(user.getEmail());
    profileRating.setText(String.valueOf(user.getRating()));
    profileBiography.setText(bidder.getBiography());
  }
}
