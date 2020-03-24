package com.cenfotec.ponto.entities.servicePetition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;

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

    // TODO: Rename and change types of parameters
    private String servicePetitionImage;
    private String servicePetitionName;
    private String servicePetitionDescription;
    private String servicePetitionServiceType;

    public ServicePetitionCard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param servicePetition service petition object.
     * @return A new instance of fragment ServicePetitionCard.
     */
    // TODO: Rename and change types and number of parameters
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
        return inflater.inflate(R.layout.fragment_service_petition_card, container, false);
    }
}
