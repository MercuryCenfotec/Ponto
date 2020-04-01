package com.cenfotec.ponto.entities.offer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.cenfotec.ponto.R;

public class AcceptOfferDialog extends AppCompatDialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
    private AcceptOfferDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_accept_offer_dialog, null);

        builder.setView(view);

        acceptButton = view.findViewById(R.id.btnAcceptOfferDialog);
        cancelButton = view.findViewById(R.id.btnCancelOfferDialog);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogOfferAccepted();
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
            listener = (AcceptOfferDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CounterOfferDialogListener");
        }
    }

    public interface AcceptOfferDialogListener {
        void dialogOfferAccepted();
    }
}
