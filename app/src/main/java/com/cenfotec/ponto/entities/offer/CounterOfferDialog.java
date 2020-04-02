package com.cenfotec.ponto.entities.offer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.cenfotec.ponto.R;

import java.text.DecimalFormat;

public class CounterOfferDialog extends AppCompatDialogFragment {

    private EditText editTextCost;
    private TextView acceptButton;
    private TextView cancelButton;
    private CounterOfferDialogListener listener;
    final DecimalFormat costFormat = new DecimalFormat("###,###.###");
    TextView costInputLabel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_counter_offer_dialog, null);

        builder.setView(view);

        editTextCost = view.findViewById(R.id.counterOfferCost);
        editTextCost.setHint(costFormat.format(Double.parseDouble(((OfferDetailActivity)getActivity()).activeOffer.getCost().toString())));
        editTextCost.setHintTextColor(Color.parseColor("#808080"));
        acceptButton = view.findViewById(R.id.btnSaveCounterOfferDialog);
        cancelButton = view.findViewById(R.id.btnCancelCounterOfferDialog);
        costInputLabel = view.findViewById(R.id.costInputLabel);

        editTextCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    String cost = costFormat.format(Double.parseDouble(s.toString()));
                    costInputLabel.setText(cost);
                } else {
                    costInputLabel.setText(" ");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyNewCost()) {
                    String newCost = editTextCost.getText().toString();
                    listener.applyText(newCost);
                    dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    private boolean verifyNewCost() {
        if (!editTextCost.getText().toString().equals("0") && !editTextCost.getText().toString().equals("") && !Float.valueOf(editTextCost.getText().toString()).equals(((OfferDetailActivity)getActivity()).activeOffer.getCost())) {
            return true;
        } else {
            editTextCost.setBackgroundResource(R.drawable.edittext_error);
            return false;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (CounterOfferDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CounterOfferDialogListener");
        }
    }

    public interface CounterOfferDialogListener {
        void applyText(String newCost);
    }
}
