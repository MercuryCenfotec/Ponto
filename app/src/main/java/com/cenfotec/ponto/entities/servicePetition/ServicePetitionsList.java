package com.cenfotec.ponto.entities.servicePetition;

import android.content.Intent;
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
import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ServicePetitionsList extends Fragment {

    private ServicePetitionCard_Adapter servicePetitionCard_adapter;
    private RecyclerView recyclerview;
    private List<ServicePetition> servicePetitionArrayList;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_service_petitions_list,container,false);



        recyclerview = (view).findViewById(R.id.recycler5);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getServicePetitionsQuery = databaseReference.child("ServicePetitions");
        servicePetitionArrayList = new ArrayList<>();
        servicePetitionCard_adapter = new ServicePetitionCard_Adapter(getActivity(),servicePetitionArrayList);
        getServicePetitionsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot servicePetitionSnapshot : snapshot.getChildren()) {
                    servicePetitionArrayList.add(servicePetitionSnapshot.getValue(ServicePetition.class));
                }
//                recyclerview.setLayoutManager(new StaggeredGridLayoutManager(servicePetitionArrayList.size(), StaggeredGridLayoutManager.HORIZONTAL));
                servicePetitionCard_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        servicePetitionCard_adapter = new ServicePetitionCard_Adapter(getActivity(),servicePetitionArrayList);
        recyclerview.setAdapter(servicePetitionCard_adapter);

        return view;

        }
    public void goToBidderProfile2(View view) {
        Intent intent = new Intent(getActivity(), BidderProfileActivity.class);
        startActivity(intent);
    }
    }

