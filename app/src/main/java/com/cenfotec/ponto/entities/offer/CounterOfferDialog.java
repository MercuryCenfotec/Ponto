package com.cenfotec.ponto.entities.offer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.cenfotec.ponto.R;

public class CounterOfferDialog extends AppCompatDialogFragment {

    private EditText editTextCost;
    private TextView acceptButton;
    private TextView cancelButton;
    private CounterOfferDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_counter_offer_dialog, null);

        builder.setView(view);

        editTextCost = view.findViewById(R.id.counterOfferCost);
        acceptButton = view.findViewById(R.id.btnSaveCounterOfferDialog);
        cancelButton = view.findViewById(R.id.btnCancelCounterOfferDialog);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCost = editTextCost.getText().toString();
                listener.applyText(newCost);
                dismiss();
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
