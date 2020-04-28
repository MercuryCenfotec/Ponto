package com.cenfotec.ponto.entities.servicePetition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
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

import adapter.ServicePetitionCard_Adapter;
import customfonts.EditText__SF_Pro_Display_Regular;
import customfonts.MyTextView_SF_Pro_Display_Semibold;


public class ServicePetitionsList extends Fragment {

  public static final String MY_PREFERENCES = "MyPrefs";
  private String userId;
  private SharedPreferences sharedPreferences;
  private ServicePetitionCard_Adapter servicePetitionCard_adapter;
  private RecyclerView recyclerview;
  private Boolean isPetitioner = false;
  private List<ServicePetition> servicePetitionArrayList;
  private Map<String, ServiceType> serviceTypesList;
  private EditText__SF_Pro_Display_Regular searchInput;
  private Boolean isSearching = false;
  private String searchValue;
  private MyTextView_SF_Pro_Display_Semibold searchResults;
  TextView listEmptyTextView;
  private View view;

  public ServicePetitionsList(boolean isPetitioner) {
    this.isPetitioner = isPetitioner;
  }

  public ServicePetitionsList() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_service_petitions_list, container, false);
    initContent();
    setContent();
    chargeServicePetitions();

    return view;

  }

  private void chargeServicePetitions() {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Query getServicePetitionsQuery;
    if (isPetitioner) {
      databaseReference = FirebaseDatabase.getInstance().getReference("ServicePetitions");
      getServicePetitionsQuery = databaseReference.orderByChild("petitionerId").equalTo(userId);
    } else {
      getServicePetitionsQuery = databaseReference.child("ServicePetitions");
    }

    getServicePetitionsQuery.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        servicePetitionArrayList.clear();
        for (DataSnapshot servicePetitionSnapshot : snapshot.getChildren()) {
          ServicePetition servicePetition = servicePetitionSnapshot.getValue(ServicePetition.class);
          if ((isSearching && servicePetition.getName().toLowerCase().contains(searchValue.toLowerCase())) || !isSearching) {
            if (isPetitioner && !servicePetition.getFinished()) {
              servicePetitionArrayList.add(servicePetition);
            } else {
              if (servicePetition.getAcceptedOfferId() != null) {
                if (servicePetition.getAcceptedOfferId().equals("") && !servicePetition.getFinished()) {
                  servicePetitionArrayList.add(servicePetition);
                }
              }
            }
          }

        }
        if (isSearching) {
          searchResults.setVisibility(View.VISIBLE);
          searchResults.setText((servicePetitionArrayList.size() + " Resultados..."));
        } else {
          searchResults.setVisibility(View.INVISIBLE);
        }
        if (serviceTypesList.isEmpty()) {
          chargeTypes();
        } else {
          servicePetitionCard_adapter.notifyDataSetChanged();
          if (!isSearching) {
            if (servicePetitionArrayList.size() == 0) {
              recyclerview.setVisibility(View.GONE);
              listEmptyTextView.setVisibility(View.VISIBLE);
            } else {
              recyclerview.setVisibility(View.VISIBLE);
              listEmptyTextView.setVisibility(View.GONE);
            }
          } else {
            recyclerview.setVisibility(View.VISIBLE);
            listEmptyTextView.setVisibility(View.GONE);
          }
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getCode());
      }
    });
  }

  private void setContent() {
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    layoutManager.setReverseLayout(true);
    servicePetitionCard_adapter = new ServicePetitionCard_Adapter(getActivity(), servicePetitionArrayList, serviceTypesList, isPetitioner);

    recyclerview.setLayoutManager(layoutManager);
    recyclerview.setItemAnimator(new DefaultItemAnimator());
    recyclerview.setAdapter(servicePetitionCard_adapter);
    searchInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals("") && s.length() > 0) {
          isSearching = true;
          searchValue = s.toString();
          chargeServicePetitions();
        } else {
          isSearching = false;
          searchValue = s.toString();
          chargeServicePetitions();
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });

  }


  private void initContent() {
    listEmptyTextView = view.findViewById(R.id.listEmptyTextView);
    searchInput = view.findViewById(R.id.searchInput);
    recyclerview = (view).findViewById(R.id.recycler5);
    searchResults = view.findViewById(R.id.searchResults);
    sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
    userId = sharedPreferences.getString("userId", "");
    servicePetitionArrayList = new ArrayList<>();
    serviceTypesList = new HashMap<>();

  }

  private void chargeTypes() {
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Query getServiceTypesQuery = databaseReference.child("ServiceTypes");
    getServiceTypesQuery.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        for (DataSnapshot servicePetitionSnapshot : snapshot.getChildren()) {
          ServiceType serviceType = servicePetitionSnapshot.getValue(ServiceType.class);
          serviceTypesList.put(serviceType.getId(), serviceType);
        }
        if (!isSearching) {
          if (servicePetitionArrayList.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            listEmptyTextView.setVisibility(View.VISIBLE);
          } else {
            recyclerview.setVisibility(View.VISIBLE);
            listEmptyTextView.setVisibility(View.GONE);
          }
        }
        servicePetitionCard_adapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getCode());
      }
    });
  }

  public void goToBidderProfile2(View view) {
    Intent intent = new Intent(getActivity(), BidderProfileActivity.class);
    startActivity(intent);
  }
}

