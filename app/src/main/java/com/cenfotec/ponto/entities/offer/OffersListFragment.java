package com.cenfotec.ponto.entities.offer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
  private TextView listEmptyTextView2;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
    final SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
    Query servicePetitionQuery = FirebaseDatabase.getInstance().getReference("ServicePetitions").orderByChild("id").equalTo(myPrefs.getString("servicePetitionId", "none"));
    final Query[] offersQuery = new Query[1];
    listEmptyTextView2 = view.findViewById(R.id.listEmptyTextView2);
    recyclerview = (view).findViewById(R.id.recycler5);
    RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    recyclerview.setLayoutManager(layoutManager);
    recyclerview.setItemAnimator(new DefaultItemAnimator());
    final DatabaseReference offerDBReference = FirebaseDatabase.getInstance().getReference("Offers");
    offerList = new ArrayList<>();
    offerCard_adapter = new OfferCard_Adapter(getActivity(), offerList);

    if (myPrefs.getString("userType", "none").equals("bidder")) {
      // Bidder
      offersQuery[0] = offerDBReference.orderByChild("userId").equalTo(myPrefs.getString("userId", "none"));
      loadBidderOffersForBidder(offersQuery[0]);
    } else {
      // Petitioner
      servicePetitionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          for (DataSnapshot petitionSnapshot : dataSnapshot.getChildren()) {
            String acceptedOfferId = petitionSnapshot.child("acceptedOfferId").getValue().toString();
            if (acceptedOfferId.equals("")) {
              offersQuery[0] = offerDBReference.orderByChild("servicePetitionId").equalTo(myPrefs.getString("servicePetitionId", "none"));
              loadOffers(offersQuery[0]);
            } else {
              offersQuery[0] = offerDBReference.orderByChild("id").equalTo(acceptedOfferId);
              loadOffers(offersQuery[0]);
            }
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    }

    offerCard_adapter = new OfferCard_Adapter(getActivity(), offerList);
    recyclerview.setAdapter(offerCard_adapter);

    return view;
  }

  private void loadOffers(Query query) {
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        offerList.clear();
        for (DataSnapshot data : snapshot.getChildren()) {
          if (!data.child("accepted").getValue().toString().equals("cancelled"))
            offerList.add(data.getValue(Offer.class));
        }
        offerCard_adapter.notifyDataSetChanged();
        if (offerList.isEmpty()) {
          listEmptyTextView2.setVisibility(View.VISIBLE);
          recyclerview.setVisibility(View.GONE);
        } else {
          recyclerview.setVisibility(View.VISIBLE);
          listEmptyTextView2.setVisibility(View.GONE);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        System.out.println("The offers read failed: " + databaseError.getCode());
      }
    });
  }

  private void loadBidderOffersForBidder(Query query) {
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        offerList.clear();
        for (DataSnapshot data : snapshot.getChildren()) {
          if (!data.child("accepted").getValue().toString().equals("cancelled")
                  && !data.child("accepted").getValue().toString().equals("accepted"))
            offerList.add(data.getValue(Offer.class));
        }
        offerCard_adapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        System.out.println("The offers read failed: " + databaseError.getCode());
      }
    });
  }
}
