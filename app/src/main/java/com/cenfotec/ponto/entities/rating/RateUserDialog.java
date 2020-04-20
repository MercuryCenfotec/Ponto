package com.cenfotec.ponto.entities.rating;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionPetitionerDetailActivity;

public class RateUserDialog extends AppCompatDialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
    private RatingBar ratingBar;
    private EditText ratingDescription;
    private RateUserDialogConfirmListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rate_user_dialog, null);

        builder.setView(view);

        acceptButton = view.findViewById(R.id.btnSaveRateDialog);
        cancelButton = view.findViewById(R.id.btnCancelRateDialog);
        ratingBar = view.findViewById(R.id.ratingBarDialog);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar.getRating() > 0) {
                    listener.dialogConfirmRating(ratingBar.getRating());
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Por favor califique al usuario", Toast.LENGTH_LONG).show();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RateUserDialogConfirmListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RateUserDialogConfirmListener");
        }
    }

    public interface RateUserDialogConfirmListener {
        void dialogConfirmRating(float rating);
    }
}
