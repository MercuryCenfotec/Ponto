package com.cenfotec.ponto.entities.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Appointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Medium;

public class AppointmentDetailActivity extends AppCompatActivity
        implements AppointmentDeletionConfirmDialog.AppointmentDeletionConfirmDialogListener {

    DatabaseReference databaseReference;
    SharedPreferences sharedpreferences;
    String MY_PREFERENCES;
    Appointment updatedAppointment;
    ConstraintLayout topContainer;
    MyTextView_SF_Pro_Display_Bold appoDetailTitle;
    MyTextView_SF_Pro_Display_Medium appoDetailDate;
    MyTextView_SF_Pro_Display_Medium appoDetailLocation;
    MyTextView_SF_Pro_Display_Medium appoDetailDescription;
    String appointmentTitle;
    String activeUserId;
    String activeUserType;
    String petitionerId;
    String bidderId;
    String selectedDate;
    int colorToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        initControls();
        catchIntent();
        getAppointmentByTitle();
    }

    private void initControls(){
        MY_PREFERENCES = "MyPrefs";
        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        activeUserId = sharedpreferences.getString("userId", "");
        activeUserType = sharedpreferences.getString("userType", "");
        topContainer = findViewById(R.id.topContainer);
        appoDetailTitle = findViewById(R.id.appoDetailTitle);
        appoDetailDate = findViewById(R.id.appoDetailDate);
        appoDetailLocation = findViewById(R.id.appoDetailLocation);
        appoDetailDescription = findViewById(R.id.appoDetailDescription);
    }

    private void catchIntent(){
        if (getIntent().getExtras() != null) {
            selectedDate = getIntent().getStringExtra("dateSelected");
            appointmentTitle = getIntent().getStringExtra("appointmentTitle");
            colorToDisplay = getIntent().getIntExtra("colorToDisplay",0);
            petitionerId = getIntent().getStringExtra("petitionerId");
            bidderId = getIntent().getStringExtra("bidderId");
        }
    }

    //Appointment detail information statements start here
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

    private void fillAppointmentData(){
        topContainer.setBackgroundColor(colorToDisplay);
        appoDetailTitle.setText(updatedAppointment.getTitle());
        String fullDateTime = updatedAppointment.getStartDateTime();
        String amPm;
        String[] separated = fullDateTime.split(" ");
        String[] separatedTime = separated[1].split(":");
        int hour = Integer.parseInt(separatedTime[0]);
        if (hour >= 12) {
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        String finalDateTime = fullDateTime + amPm;
        appoDetailDate.setText(finalDateTime);
        appoDetailLocation.setText(updatedAppointment.getLocation());
        appoDetailDescription.setText(updatedAppointment.getDescription());
    }

    //Other statements start here
    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBackFromAppoDetail(View view){
        finish();
        Intent iac = new Intent(this, AppointmentAgendaActivity.class);
        iac.putExtra("userId", activeUserId);
        iac.putExtra("userType", activeUserType);
        iac.putExtra("petitionerId", petitionerId);
        iac.putExtra("bidderId", bidderId);
        startActivity(iac);
    }

    public void openAppoUpdate(View view){
        finish();
        Intent intent = new Intent(this, AppointmentUpdateActivity.class);
        intent.putExtra("dateSelected", selectedDate);
        intent.putExtra("appointmentTitle", updatedAppointment.getTitle());
        intent.putExtra("colorToDisplay", colorToDisplay);
        intent.putExtra("petitionerId", petitionerId);
        intent.putExtra("bidderId", bidderId);
        startActivity(intent);
    }

    //AppointmentDeletionConfirmDialog statements start here
    public void openAppoDelete(View view) {
        AppointmentDeletionConfirmDialog appoDeleteDialog = new AppointmentDeletionConfirmDialog();
        appoDeleteDialog.show(getSupportFragmentManager(), "delete appointment dialog");
    }

    //Delete statements start here
    @Override
    public void dialogAppointmentDeletionConfirmed() {
        DatabaseReference db = databaseReference.child(updatedAppointment.getId());
        db.removeValue();
        finish();
        Intent iac = new Intent(this, AppointmentAgendaActivity.class);
        iac.putExtra("userId", activeUserId);
        iac.putExtra("userType", activeUserType);
        iac.putExtra("petitionerId", petitionerId);
        iac.putExtra("bidderId", bidderId);
        startActivity(iac);
        showToaster("Cita eliminada exitosamente");
    }
}
