package com.cenfotec.ponto.entities.offer;

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
import com.cenfotec.ponto.data.model.Offer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import adapter.OfferCard_Adapter;

import static android.content.Context.MODE_PRIVATE;

public class OffersListFragment extends Fragment {

    private OfferCard_Adapter offerCard_adapter;
    private RecyclerView recyclerview;
    private List<Offer> offerList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Query offersQuery;
        recyclerview = (view).findViewById(R.id.recycler5);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        final DatabaseReference offerDBReference = FirebaseDatabase.getInstance().getReference("Offers");
        offerList = new ArrayList<>();
        offerCard_adapter = new OfferCard_Adapter(getActivity(), offerList);

        if (myPrefs.getString("userType", "none").equals("bidder")) {
            offersQuery = offerDBReference.orderByChild("userId").equalTo(myPrefs.getString("userId", "none"));
        } else {
            offersQuery = offerDBReference.orderByChild("servicePetitionId").equalTo(myPrefs.getString("servicePetitionId", "none"));
        }



        offersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    offerList.add(data.getValue(Offer.class));
                }
                offerCard_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The offers read failed: " + databaseError.getCode());
            }
        });

        offerCard_adapter = new OfferCard_Adapter(getActivity(), offerList);
        recyclerview.setAdapter(offerCard_adapter);

        return view;
    }
}
