package com.cenfotec.ponto.entities.offer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Offer;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OfferCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "cardTitle";
    private static final String ARG_PARAM2 = "cardDuration";
    private static final String ARG_PARAM3 = "cardCost";
    private static final String ARG_PARAM4 = "cardDescription";

    // TODO: Rename and change types of parameters
    private String offerId;
    View view;
    private String cardTitle;
    private String cardDuration;
    private String cardCost;
    private String cardDescription;

    public OfferCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param offer offer object to be displayed.
     * @return A new instance of fragment OfferCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public OfferCardFragment newInstance(Offer offer) {
        OfferCardFragment fragment = new OfferCardFragment();
        Bundle args = new Bundle();
        SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        args.putString(ARG_PARAM1, myPrefs.getString("userType", "none").equals("bidder") ? offer.getServicePetitionTitle() : offer.getBidderName());
        args.putString(ARG_PARAM2, offer.getDuration() + (offer.getDurationType().equals("hour") ? " horas" : " días"));
        args.putString(ARG_PARAM3, "₡ "+offer.getCost());
        args.putString(ARG_PARAM4, offer.getDescription());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardTitle = getArguments().getString(ARG_PARAM1);
            cardDuration = getArguments().getString(ARG_PARAM2);
            cardCost = getArguments().getString(ARG_PARAM2);
            cardDescription = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offer_card, container, false);
        view.findViewById(R.id.offerCard).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToOfferDetail();
            }
        });
        return view;
    }

    public void goToOfferDetail(){
        SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        myPrefs.edit().putString("offerId",offerId).commit();
        Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
        startActivity(intent);
    }
}
