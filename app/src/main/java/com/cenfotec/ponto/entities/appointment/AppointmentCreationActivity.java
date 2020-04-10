package com.cenfotec.ponto.entities.appointment;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    String selectedDate = "";
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_creation);
        initControls();

    }

    private void initControls(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        appointmentTitleEditText = findViewById(R.id.appointmentTitleEditText);
        appointmentLocationEditText = findViewById(R.id.appointmentLocationEditText);
        appointmentHourEditText = findViewById(R.id.appointmentHourEditText);
        appointmentDescriptionEditText = findViewById(R.id.appointmentDescriptionEditText);
        customDatePickerDialog = new CustomDatePickerDialog();

        if(getIntent().getExtras()!= null){
            selectedDate = getIntent().getStringExtra("dateSelected");
        }
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
                appointmentHourEditText.setText(shour);
            }
        }, currentHour, currentMinute, false);
        timePickerDialog.show();

    }

    public void createAppointment(View view){
        String hour = appointmentHourEditText.getText().toString();
        String longDate = selectedDate + " " + hour.substring(0, hour.length() -2);
        String appointmentId = databaseReference.push().getKey();
        Appointment appointment = new Appointment(appointmentId,
                appointmentTitleEditText.getText().toString(),
                appointmentLocationEditText.getText().toString(), longDate,
                appointmentDescriptionEditText.getText().toString(), petitionerId, bidderUserId);
        databaseReference.child(appointmentId).setValue(appointment);
        showToaster("Registro exitoso");
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
