package com.cenfotec.ponto.entities.appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Appointment;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AppointmentCreationActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    String petitionerId = "-M4QlQIvXVkax3q4yClP";
    String bidderUserId = "-M4Qle-f_LPhyhCdOzhH";
    EditText appointmentTitleEditText;
    EditText appointmentLocationEditText;
    EditText appointmentHourEditText;
    EditText appointmentDescriptionEditText;
    CustomDatePickerDialog customDatePickerDialog;
    String selectedDate;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_creation);
        initControls();
        catchIntent();
    }

    private void initControls() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        appointmentTitleEditText = findViewById(R.id.appointmentTitleEditText);
        appointmentLocationEditText = findViewById(R.id.appointmentLocationEditText);
        appointmentHourEditText = findViewById(R.id.appointmentHourEditText);
        appointmentDescriptionEditText = findViewById(R.id.appointmentDescriptionEditText);
        customDatePickerDialog = new CustomDatePickerDialog();
        selectedDate = "";
    }

    private void catchIntent() {
        if (getIntent().getExtras() != null) {
            selectedDate = getIntent().getStringExtra("dateSelected");
        }
    }

    //TimePickerDialog statements start here
    public void startAppointmentInput(View view) {
        calendar = Calendar.getInstance();
        if (appointmentHourEditText.getText().toString().equals("")) {
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
        } else {
            String currentString = appointmentHourEditText.getText().toString();
            String[] separated = currentString.split(":");
            currentHour = Integer.parseInt(separated[0]);
            String newMinute = separated[1].substring(0, separated[1].length() - 2);
            currentMinute = Integer.parseInt(newMinute);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                String longHourFormat = String.format("%02d:%02d", hourOfDay, minute) + amPm;
                appointmentHourEditText.setText(longHourFormat);
            }
        }, currentHour, currentMinute, false);
        timePickerDialog.show();
    }

    //Create statements start here
    public void preCreateAppointment(View view) {
        if (!showErrorOnBlankSpaces()) {
            Query getAppointmentByTitleQuery = databaseReference.orderByChild("title")
                    .equalTo(appointmentTitleEditText.getText().toString());
            getAppointmentByTitleQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean appointmentFound = false;
                    for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                        if (appointmentSnapshot != null) {
                            appointmentFound = true;
                            showToaster("Cita con nombre existente");
                        }
                    }
                    if (!appointmentFound) {
                        checkIfAppoDateTimeExists();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        } else {
            showToaster("Verificar campos");
        }
    }

    private void checkIfAppoDateTimeExists() {
        String hour = appointmentHourEditText.getText().toString();
        final String longDate = selectedDate + " " + hour.substring(0, hour.length() - 2);
        Query getAppointmentByDateTimeQuery = databaseReference.orderByChild("startDateTime").equalTo(longDate);
        getAppointmentByDateTimeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean appointmentFound = false;
                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    if (appointmentSnapshot != null) {
                        appointmentFound = true;
                        showToaster("Cita con fecha existente");
                    }
                }
                if (!appointmentFound) {
                    createAppointment(longDate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void createAppointment(String longDate) {
        String appointmentId = databaseReference.push().getKey();
        Appointment appointment = new Appointment(appointmentId,
                appointmentTitleEditText.getText().toString(),
                appointmentLocationEditText.getText().toString(), longDate,
                appointmentDescriptionEditText.getText().toString(), petitionerId, bidderUserId);
        databaseReference.child(appointmentId).setValue(appointment);
        showToaster("Registro exitoso");
    }

    //Other statements start here
    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBackFromAppoCreation(View view) {
        finish();
    }

    //Validation statements start here
    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{appointmentTitleEditText, appointmentLocationEditText,
                appointmentHourEditText, appointmentDescriptionEditText};
        for (EditText editText : editTextsList) {
            if (editText.getText().toString().equals("")) {
                editText.setHintTextColor(Color.parseColor("#c0392b"));
                editText.setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editText.setBackgroundResource(R.drawable.rect_black);
                editText.setHintTextColor(Color.parseColor("#000000"));
            }
        }
        return isEmpty;
    }

}
