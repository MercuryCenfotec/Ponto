package com.cenfotec.ponto.entities.bidder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.data.model.SpinnerItem;
import com.cenfotec.ponto.entities.offer.OfferCreationActivity;
import com.cenfotec.ponto.entities.offer.OfferDetailActivity;
import com.cenfotec.ponto.utils.LogoutHelper;
import com.cenfotec.ponto.utils.SearchableSpinnerHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BidderHome extends Fragment {
    View view;//this one it's necessary in a new fragment

    public BidderHome() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //the layout is the fragment name
        view = inflater.inflate(R.layout.fragment_bidder_home,
                container, false);
        initContent();
        setContent();

        return view;
    }

    private void initContent() {
        //Get the buttons by the id
    }

    private void setContent() {
        //Set the button function by id
    }

    //    here goes the activity methods
}
