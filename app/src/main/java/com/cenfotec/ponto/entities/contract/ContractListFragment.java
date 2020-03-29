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

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Contract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import adapter.ContractCard_Adapter;
import adapter.OfferCard_Adapter;

import static android.content.Context.MODE_PRIVATE;


public class ContractListFragment extends Fragment {
    private ContractCard_Adapter contractCard_adapter;
    private RecyclerView recyclerview;
    private List<Contract> contractList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contract_list, container, false);
        SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Query offersQuery;
        recyclerview = (view).findViewById(R.id.contractListRecycler);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        final DatabaseReference offerDBReference = FirebaseDatabase.getInstance().getReference("Contracts");
        contractList = new ArrayList<>();
        contractCard_adapter = new ContractCard_Adapter(getActivity(), contractList);

        if (myPrefs.getString("userType", "none").equals("bidder")) {
            offersQuery = offerDBReference.orderByChild("bidderId").equalTo(myPrefs.getString("userId", "none"));
        } else {
            offersQuery = offerDBReference.orderByChild("petitionerId").equalTo(myPrefs.getString("userId", "none"));
        }

        offersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    contractList.add(data.getValue(Contract.class));
                }
                contractCard_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The offers read failed: " + databaseError.getCode());
            }
        });

        contractCard_adapter = new ContractCard_Adapter(getActivity(), contractList);
        recyclerview.setAdapter(contractCard_adapter);

        return view;
    }
}
