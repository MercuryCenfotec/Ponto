package com.cenfotec.ponto.entities.appointment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.cenfotec.ponto.R;

public class AppointmentDeletionConfirmDialog extends AppCompatDialogFragment {
    private TextView acceptButton;
    private TextView cancelButton;
    private AppointmentDeletionConfirmDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_confirm_appointment_deletion_dialog, null);

        builder.setView(view);

        acceptButton = view.findViewById(R.id.btnAcceptOfferDialog);
        cancelButton = view.findViewById(R.id.btnCancelOfferDialog);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogAppointmentDeletionConfirmed();
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
            listener = (AppointmentDeletionConfirmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CounterOfferDialogListener");
        }
    }

    public interface AppointmentDeletionConfirmDialogListener {
        void dialogAppointmentDeletionConfirmed();
    }
}
