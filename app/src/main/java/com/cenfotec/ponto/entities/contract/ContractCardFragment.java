package com.cenfotec.ponto.entities.contract;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Contract;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContractCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContractCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "contractCardTitle";
    private static final String ARG_PARAM2 = "contractCardDate";
    private static final String ARG_PARAM3 = "contractCardService";

    // TODO: Rename and change types of parameters
    private String contractId;
    View view;
    private String contractCardTitle;
    private String contractCardDate;
    private String contractCardService;

    public ContractCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contract contract object to be displayed.
     * @return A new instance of fragment OfferCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public ContractCardFragment newInstance(Contract contract) {
        ContractCardFragment fragment = new ContractCardFragment();
        Bundle args = new Bundle();
        SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        args.putString(ARG_PARAM1, contract.getName());
        args.putString(ARG_PARAM2, contract.getDateCreated());
        args.putString(ARG_PARAM3, contract.getServicePetitionId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contractCardTitle = getArguments().getString(ARG_PARAM1);
            contractCardDate = getArguments().getString(ARG_PARAM2);
            contractCardService = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contract_card, container, false);
        view.findViewById(R.id.contractCardView).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToContractDetail();
            }
        });
        return view;
    }

    public void goToContractDetail(){
        SharedPreferences myPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        myPrefs.edit().putString("contractId", contractId).commit();
        Intent intent = new Intent(getActivity(), GeneratedContractActivity.class);
        startActivity(intent);
    }
}
