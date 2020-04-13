package com.cenfotec.ponto.entities.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppointmentUpdateActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    String MY_PREFERENCES;
    DatabaseReference databaseReference;
    EditText appointmentTitleUpdEditText;
    EditText appointmentLocationUpdEditText;
    EditText appointmentHourUpdEditText;
    EditText appointmentDescriptionUpdEditText;
    CustomDatePickerDialog customDatePickerDialog;
    String selectedDate;
    String appointmentTitle;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    Appointment updatedAppointment;
    String activeUserId;
    String activeUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_update);
        initControls();
        catchIntent();
        getAppointmentByTitle();
    }

    private void initControls() {
        MY_PREFERENCES = "MyPrefs";
        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        appointmentTitleUpdEditText = findViewById(R.id.appointmentTitleUpdEditText);
        appointmentLocationUpdEditText = findViewById(R.id.appointmentLocationUpdEditText);
        appointmentHourUpdEditText = findViewById(R.id.appointmentHourUpdEditText);
        appointmentDescriptionUpdEditText = findViewById(R.id.appointmentDescriptionUpdEditText);
        customDatePickerDialog = new CustomDatePickerDialog();
        selectedDate = "";
        appointmentTitle = "";
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        activeUserId = sharedpreferences.getString("userId", "");
        activeUserType = sharedpreferences.getString("userType", "");
    }

    private void catchIntent() {
        if (getIntent().getExtras() != null) {
            selectedDate = getIntent().getStringExtra("dateSelected");
            appointmentTitle = getIntent().getStringExtra("appointmentTitle");
        }
    }

    //Form data statements start here
    private void getAppointmentByTitle() {
        Query getAppointmentByTitleQuery = databaseReference.orderByChild("title").equalTo(appointmentTitle);
        getAppointmentByTitleQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    updatedAppointment = appointmentSnapshot.getValue(Appointment.class);
                    fillAppointmentData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillAppointmentData() {
        appointmentTitleUpdEditText.setText(updatedAppointment.getTitle());
        appointmentLocationUpdEditText.setText(updatedAppointment.getLocation());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            calendar.setTime(sdf.parse(selectedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 12) {
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        String longHourFormat = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)) + amPm;
        appointmentHourUpdEditText.setText(longHourFormat);
        appointmentDescriptionUpdEditText.setText(updatedAppointment.getDescription());
    }

    //TimePickerDialog statements start here
    public void startAppointmentInput(View view) {
        calendar = Calendar.getInstance();
        if (appointmentHourUpdEditText.getText().toString().equals("")) {
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
        } else {
            String currentString = appointmentHourUpdEditText.getText().toString();
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
                appointmentHourUpdEditText.setText(longHourFormat);
            }
        }, currentHour, currentMinute, false);
        timePickerDialog.show();
    }

    //Update statements start here
    public void preUpdateAppointment(View view) {
        if (!showErrorOnBlankSpaces()) {
            if (isCurrentAppointmentTitle()) {
                checkIfAppoDateTimeExists();
            } else {
                Query getAppointmentByTitleQuery = databaseReference.orderByChild("title")
                        .equalTo(appointmentTitleUpdEditText.getText().toString());
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
            }
        } else {
            showToaster("Verificar campos");
        }
    }

    private void checkIfAppoDateTimeExists() {
        final String longDate = getFormattedLongDate();
        if (isCurrentAppointmentDate()) {
            updateAppointment(longDate);
        } else {
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
                        updateAppointment(longDate);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    public void updateAppointment(String longDate) {
        DatabaseReference updateUserReference = FirebaseDatabase.getInstance().
                getReference("Appointments").child(updatedAppointment.getId());
        updatedAppointment.setTitle(appointmentTitleUpdEditText.getText().toString());
        updatedAppointment.setLocation(appointmentLocationUpdEditText.getText().toString());
        updatedAppointment.setStartDateTime(longDate);
        updatedAppointment.setDescription(appointmentDescriptionUpdEditText.getText().toString());
        updateUserReference.setValue(updatedAppointment);
        showToaster("Actualización exitosa");
        openAppointmentAgenda();
    }

    //Other statements start here
    private void openAppointmentAgenda() {
        finish();
        Intent iac = new Intent(this, AppointmentAgendaActivity.class);
        iac.putExtra("userId", activeUserId);
        iac.putExtra("userType", activeUserType);
        startActivity(iac);
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBackFromAppoUpdate(View view) {
        finish();
    }

    private String getFormattedLongDate() {
        String hour = appointmentHourUpdEditText.getText().toString();
        String currentString = selectedDate;
        String[] separated = currentString.split(" ");
        String newDate = separated[0];
        return newDate + " " + hour.substring(0, hour.length() - 2);
    }

    //Validation statements start here
    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{appointmentTitleUpdEditText, appointmentLocationUpdEditText,
                appointmentHourUpdEditText, appointmentDescriptionUpdEditText};
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

    private boolean isCurrentAppointmentTitle() {
        String title = appointmentTitleUpdEditText.getText().toString();
        return title.equals(updatedAppointment.getTitle());
    }

    private boolean isCurrentAppointmentDate() {
        String longDate = getFormattedLongDate();
        return longDate.equals(updatedAppointment.getStartDateTime());
    }
}
