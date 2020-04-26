package com.cenfotec.ponto.entities.contract;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Contract;
import com.cenfotec.ponto.data.model.ServicePetition;
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

import javax.annotation.Nullable;

import adapter.ContractCard_Adapter;

import static android.content.Context.MODE_PRIVATE;

public class ContractListFragment extends Fragment {
    private ContractCard_Adapter contractCard_adapter;
    private RecyclerView contractListRecycler;
    private List<Contract> contractList;
    private Map<String, ServicePetition> servicePetitionList;
    private TextView contractsListEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract_list, container, false);
        contractsListEmpty = (view).findViewById(R.id.contractsListEmpty);
        contractListRecycler = (view).findViewById(R.id.contractListRecycler);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        contractListRecycler.setLayoutManager(layoutManager);
        contractListRecycler.setItemAnimator(new DefaultItemAnimator());
        contractList = new ArrayList<>();
        servicePetitionList = new HashMap<>();
        getContractsFromDB();
        contractCard_adapter = new ContractCard_Adapter(getActivity(), contractList, servicePetitionList);
        contractListRecycler.setAdapter(contractCard_adapter);

        return view;
    }

    private void getContractsFromDB() {
        SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Query contractsQuery;
        final DatabaseReference contractsDBReference = FirebaseDatabase.getInstance().
                getReference("Contracts");

        if (myPrefs.getString("userType", "none").equals("bidder")) {
            contractsQuery = contractsDBReference.orderByChild("bidderId").
                    equalTo(myPrefs.getString("userId", "none"));
        } else {
            contractsQuery = contractsDBReference.orderByChild("petitionerId").
                    equalTo(myPrefs.getString("userId", "none"));
        }

        contractsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contractList.clear();
                for (DataSnapshot contractSnapshot : snapshot.getChildren()) {
                    contractList.add(contractSnapshot.getValue(Contract.class));
                }
                if(contractList.isEmpty()){
                    contractsListEmpty.setVisibility(View.VISIBLE);
                    contractListRecycler.setVisibility(View.GONE);
                }else{
                    contractsListEmpty.setVisibility(View.GONE);
                    contractListRecycler.setVisibility(View.VISIBLE);
                }
                loadPetitionsFromDB();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The offers read failed: " + databaseError.getCode());
            }
        });
    }

    private void loadPetitionsFromDB() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getServicePetitionQuery = databaseReference.child("ServicePetitions");
        getServicePetitionQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot servicePetitionSnapshot : snapshot.getChildren()) {
                    ServicePetition servicePetition = servicePetitionSnapshot.getValue(ServicePetition.class);
                    servicePetitionList.put(servicePetition.getId(), servicePetition);
                }
                contractCard_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
