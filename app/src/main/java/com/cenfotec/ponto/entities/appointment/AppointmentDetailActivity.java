package com.cenfotec.ponto.entities.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Appointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;

import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Medium;

public class AppointmentDetailActivity extends AppCompatActivity
        implements AppointmentDeletionConfirmDialog.AppointmentDeletionConfirmDialogListener {

    DatabaseReference databaseReference;
    SharedPreferences sharedpreferences;
    String MY_PREFERENCES;
    Appointment updatedAppointment;
    ConstraintLayout topContainer;
    ConstraintLayout btnLocationSharing;
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
    ImageView imgAppoSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        initControls();
        catchIntent();
        getAppointmentByTitle();
    }

    // ## OnActivityCreation statements start here ##
    private void initControls() {
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
        imgAppoSettings = findViewById(R.id.imgAppoSettings);
        btnLocationSharing = findViewById(R.id.btnLocationSharing);
    }

    private void catchIntent() {
        if (getIntent().getExtras() != null) {
            selectedDate = getIntent().getStringExtra("dateSelected");
            appointmentTitle = getIntent().getStringExtra("appointmentTitle");
            colorToDisplay = getIntent().getIntExtra("colorToDisplay", 0);
            petitionerId = getIntent().getStringExtra("petitionerId");
            bidderId = getIntent().getStringExtra("bidderId");
        }

        if (activeUserType.equals("petitioner")) {
            imgAppoSettings.setVisibility(View.VISIBLE);
        } else {
            btnLocationSharing.setVisibility(View.VISIBLE);
        }
    }
    // ## OnActivityCreation statements start here ##

    // ## Appointment detail information statements start here ##
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
    // ## Appointment detail information statements end ##

    // ## Other statements start here ##
    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBackFromAppoDetail(View view) {
        finish();
        Intent iac = new Intent(this, AppointmentAgendaActivity.class);
        iac.putExtra("userId", activeUserId);
        iac.putExtra("userType", activeUserType);
        iac.putExtra("petitionerId", petitionerId);
        iac.putExtra("bidderId", bidderId);
        startActivity(iac);
    }

    public void openAppoUpdate() {
        finish();
        Intent intent = new Intent(this, AppointmentUpdateActivity.class);
        intent.putExtra("dateSelected", selectedDate);
        intent.putExtra("appointmentTitle", updatedAppointment.getTitle());
        intent.putExtra("colorToDisplay", colorToDisplay);
        intent.putExtra("petitionerId", petitionerId);
        intent.putExtra("bidderId", bidderId);
        startActivity(intent);
    }
    // ## Other statements end ##

    // ## AppointmentSettingsMenu statements start here ##
    public void openAppointmentSettings(View view) {
        PopupMenu popupMenu = new PopupMenu(AppointmentDetailActivity.this, imgAppoSettings);
        popupMenu.getMenuInflater().inflate(R.menu.appo_detail_settings, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settingsOptionModify:
                        openAppoUpdate();
                        return true;
                    case R.id.settingsOptionDelete:
                        openAppoDelete();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    // ## AppointmentSettingsMenu statements end ##

    // ## AppointmentDeletionConfirmDialog statements start here ##
    public void openAppoDelete() {
        AppointmentDeletionConfirmDialog appoDeleteDialog = new AppointmentDeletionConfirmDialog();
        appoDeleteDialog.show(getSupportFragmentManager(), "delete appointment dialog");
    }

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
    // ## AppointmentDeletionConfirmDialog statements end ##

    // ## Google Maps Share Location statements start here ##
    public void openLocationSharing(View view) {
        String googleMapsUrlString = "https://www.google.com/maps/search/?api=1&query="
                + appoDetailLocation.getText().toString();
        URL mapsURL = null;
        try {
            mapsURL = new URL(googleMapsUrlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapsURL.toString()));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
    // ## Google Maps Share Location statements end ##
}
