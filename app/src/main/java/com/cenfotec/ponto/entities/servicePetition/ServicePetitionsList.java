package com.cenfotec.ponto.entities.servicePetition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cenfotec.ponto.R;

import adapter.ServicePetitionCard_Adapter;

import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
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


public class ServicePetitionsList extends Fragment {

    public static final String MY_PREFERENCES = "MyPrefs";
    private String userId;
    SharedPreferences sharedPreferences;
    private ServicePetitionCard_Adapter servicePetitionCard_adapter;
    private RecyclerView recyclerview;
    private Boolean isPetitioner = false;
    private List<ServicePetition> servicePetitionArrayList;
    private Map<String, ServiceType> serviceTypesList;

    public ServicePetitionsList(boolean isPetitioner) {
        this.isPetitioner = isPetitioner;
    }

    public ServicePetitionsList() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_petitions_list, container, false);


        recyclerview = (view).findViewById(R.id.recycler5);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getServicePetitionsQuery;
        if(isPetitioner){
            databaseReference = FirebaseDatabase.getInstance().getReference("ServicePetitions");
            getServicePetitionsQuery = databaseReference.orderByChild("petitionerId").equalTo(userId);
        }else{
            getServicePetitionsQuery = databaseReference.child("ServicePetitions");
        }
        servicePetitionArrayList = new ArrayList<>();
        serviceTypesList = new HashMap<>();
        getServicePetitionsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                servicePetitionArrayList.clear();
                for (DataSnapshot servicePetitionSnapshot : snapshot.getChildren()) {
                    ServicePetition servicePetition = servicePetitionSnapshot.getValue(ServicePetition.class);
                    if(isPetitioner){
                        servicePetitionArrayList.add(servicePetitionSnapshot.getValue(ServicePetition.class));
                    }else {
                        if (servicePetition.getAcceptedOfferId() != null) {
                            if (servicePetition.getAcceptedOfferId().equals("")) {
                                servicePetitionArrayList.add(servicePetition);
                            }
                        }
                    }
                }
//                recyclerview.setLayoutManager(new StaggeredGridLayoutManager(servicePetitionArrayList.size(), StaggeredGridLayoutManager.HORIZONTAL));
                chargeTypes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        servicePetitionCard_adapter = new ServicePetitionCard_Adapter(getActivity(), servicePetitionArrayList, serviceTypesList,isPetitioner);
        recyclerview.setAdapter(servicePetitionCard_adapter);

        return view;

    }

    private void chargeTypes() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getServiceTypesQuery = databaseReference.child("ServiceTypes");
        getServiceTypesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot servicePetitionSnapshot : snapshot.getChildren()) {
                    ServiceType serviceType = servicePetitionSnapshot.getValue(ServiceType.class);
                    serviceTypesList.put(serviceType.getId(),serviceType);
                }
//                recyclerview.setLayoutManager(new StaggeredGridLayoutManager(servicePetitionArrayList.size(), StaggeredGridLayoutManager.HORIZONTAL));
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

