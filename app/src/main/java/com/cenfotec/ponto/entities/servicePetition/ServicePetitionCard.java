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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "servicePetitionImage";
    private static final String ARG_PARAM2 = "servicePetitionName";
    private static final String ARG_PARAM3 = "servicePetitionDescription";
    private static final String ARG_PARAM4 = "servicePetitionServiceType";
    View view;

    // TODO: Rename and change types of parameters
    private String servicePetitionId;
    private String servicePetitionImage;
    private String servicePetitionName;
    private String servicePetitionDescription;
    private String servicePetitionServiceType;

    public ServicePetitionCard() {
        // Required empty public constructor
    }

    public static ServicePetitionCard newInstance(ServicePetition servicePetition) {
        ServicePetitionCard fragment = new ServicePetitionCard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, servicePetition.getName());
        args.putString(ARG_PARAM3, servicePetition.getDescription());
        args.putString(ARG_PARAM4, servicePetition.getServiceTypeId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            servicePetitionImage = getArguments().getString(ARG_PARAM1);
            servicePetitionName = getArguments().getString(ARG_PARAM2);
            servicePetitionDescription = getArguments().getString(ARG_PARAM3);
            servicePetitionServiceType = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service_petition_bidder,
                container, false);
        view.findViewById(R.id.petitionCard).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToPetitionDetail();
            }
        });

        return view;
    }

    public void goToPetitionDetail(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        Intent intent = new Intent(getActivity(), ServicePetitionDetailActivity.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("servicePetitionId", servicePetitionId);
        editor.commit();
        startActivity(intent);
    }
}
