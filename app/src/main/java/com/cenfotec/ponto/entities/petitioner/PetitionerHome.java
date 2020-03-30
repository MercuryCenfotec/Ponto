package com.cenfotec.ponto.entities.petitioner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionCreationActivity;
import com.cenfotec.ponto.utils.LogoutHelper;

public class PetitionerHome extends Fragment {

    View view;
    Button btnLO;
    Button btnCreatePetition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_petitioner_home,
                container, false);
        initContent();
        return view;
    }

    private void initContent() {
        //Get the buttons by the id
        btnLO = (Button) view.findViewById(R.id.btnLO);
        btnCreatePetition = (Button) view.findViewById(R.id.btnCreatePetition);
        setContent();

    }

    private void setContent() {
        //Set the button function by id
        view.findViewById(R.id.btnLO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //you can call the button method here
                logout(v);
            }
        });

        btnCreatePetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToServicePetition(v);
            }
        });
    }

    public void goToServicePetition(View view){
        Intent intent = new Intent(getActivity(), ServicePetitionCreationActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        LogoutHelper.logout(getActivity());
    }
}
