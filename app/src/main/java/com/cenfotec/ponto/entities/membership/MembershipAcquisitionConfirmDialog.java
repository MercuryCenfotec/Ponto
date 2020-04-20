package com.cenfotec.ponto.entities.membership;

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

public class MembershipAcquisitionConfirmDialog extends AppCompatDialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
    private MembershipAcquireDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_confirm_membership_dialog, null);

        builder.setView(view);

        acceptButton = view.findViewById(R.id.btnAcquireMembershipDialog);
        cancelButton = view.findViewById(R.id.btnCancelMembershipDialog);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogConfirmAcquireMembership();
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
            listener = (MembershipAcquireDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MembershipAcquireDialogListener");
        }
    }

    public interface MembershipAcquireDialogListener {
        void dialogConfirmAcquireMembership();
    }
}
