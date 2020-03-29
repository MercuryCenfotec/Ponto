package com.cenfotec.ponto.entities.servicePetition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.entities.user.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicePetitionCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicePetitionCard extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service_petition_bidder,
                container, false);

        return view;
    }

}
