package com.cenfotec.ponto.entities.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
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

    DatabaseReference databaseReference;
    EditText appointmentTitleUpdEditText;
    EditText appointmentLocationUpdEditText;
    EditText appointmentHourUpdEditText;
    EditText appointmentDescriptionUpdEditText;
    CustomDatePickerDialog customDatePickerDialog;
    String selectedDate = "";
    String appointmentTitle = "";
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    Appointment updatedAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_update);
        initControls();
        fillSpaces();
    }

    private void initControls(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        appointmentTitleUpdEditText = findViewById(R.id.appointmentTitleUpdEditText);
        appointmentLocationUpdEditText = findViewById(R.id.appointmentLocationUpdEditText);
        appointmentHourUpdEditText = findViewById(R.id.appointmentHourUpdEditText);
        appointmentDescriptionUpdEditText = findViewById(R.id.appointmentDescriptionUpdEditText);
        customDatePickerDialog = new CustomDatePickerDialog();

        if(getIntent().getExtras()!= null){
            selectedDate = getIntent().getStringExtra("dateSelected");
            appointmentTitle = getIntent().getStringExtra("appointmentTitle");
        }
    }

    private void fillSpaces(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getUserByIdQuery = databaseReference.child("Appointments").orderByChild("title").equalTo(appointmentTitle);
        getUserByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    updatedAppointment = userSnapshot.getValue(Appointment.class);
                    appointmentTitleUpdEditText.setText(updatedAppointment.getTitle());
                    appointmentLocationUpdEditText.setText(updatedAppointment.getLocation());
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    try {
                        calendar.setTime(sdf.parse(selectedDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(calendar.get(Calendar.HOUR_OF_DAY) >= 12){
                        amPm = "PM";
                    }else{
                        amPm = "AM";
                    }
                    String shour = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)) + amPm;
                    appointmentHourUpdEditText.setText(shour);
                    appointmentDescriptionUpdEditText.setText(updatedAppointment.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startAppointmentInput(View view){

        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay >= 12){
                    amPm = "PM";
                }else{
                    amPm = "AM";
                }
                String shour = String.format("%02d:%02d", hourOfDay, minute) + amPm;
                appointmentHourUpdEditText.setText(shour);
            }
        }, currentHour, currentMinute, false);
        timePickerDialog.show();

    }

    public void updateAppointment(View view){
        DatabaseReference updateUserReference = FirebaseDatabase.getInstance().
                getReference("Appointments").child(updatedAppointment.getId());
        String hour = appointmentHourUpdEditText.getText().toString();
        String newDate = selectedDate.substring(0, selectedDate.length() -6);
        String longDate = newDate + " " + hour.substring(0, hour.length() -2);
        updatedAppointment.setTitle(appointmentTitleUpdEditText.getText().toString());
        updatedAppointment.setLocation(appointmentLocationUpdEditText.getText().toString());
        updatedAppointment.setStartDateTime(longDate);
        updatedAppointment.setDescription(appointmentDescriptionUpdEditText.getText().toString());
        updateUserReference.setValue(updatedAppointment);
        showToaster("Actualización exitosa");
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
