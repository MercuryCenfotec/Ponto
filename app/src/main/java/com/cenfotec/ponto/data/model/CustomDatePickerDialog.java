package com.cenfotec.ponto.data.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class CustomDatePickerDialog {

    public CustomDatePickerDialog() {

    }

    public void openDateDialog(EditText inputToChange, Activity currentActivity,
                               DatePickerDialog.OnDateSetListener dateSetListener) {
        int year;
        int month;
        int day;
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);

        if (TextUtils.isEmpty(inputToChange.getText().toString())) {
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            day = Integer.parseInt(inputToChange.getText().toString().split("/")[0]);
            month = Integer.parseInt(inputToChange.getText().toString().split("/")[1]) - 1;
            year = Integer.parseInt(inputToChange.getText().toString().split("/")[2]);
        }

        DatePickerDialog dialog = new android.app.DatePickerDialog(
                currentActivity,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                dateSetListener,
                year, month, day);
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", dialog);
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancelar", dialog);
        dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        dialog.show();
    }

    public void openAgendaDateDialog(EditText inputToChange, Activity currentActivity,
                               DatePickerDialog.OnDateSetListener dateSetListener) {
        int year;
        int month;
        int day;
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.YEAR, -18);

        if (TextUtils.isEmpty(inputToChange.getText().toString())) {
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            day = Integer.parseInt(inputToChange.getText().toString().split("/")[0]);
            month = Integer.parseInt(inputToChange.getText().toString().split("/")[1]) - 1;
            year = Integer.parseInt(inputToChange.getText().toString().split("/")[2]);
        }

        DatePickerDialog dialog = new android.app.DatePickerDialog(
                currentActivity,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                dateSetListener,
                year, month, day);
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", dialog);
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancelar", dialog);
        dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        dialog.show();
    }
}
