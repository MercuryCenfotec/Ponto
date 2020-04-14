package com.cenfotec.ponto.entities.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Appointment;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AppointmentAgendaActivity extends AppCompatActivity implements CalendarPickerController {
    DatabaseReference databaseReference;
    AgendaCalendarView mAgendaCalendarView;
    String userId;
    String userType;
    List<Appointment> appointmentList;
    List<CalendarEvent> eventList;
    List<BaseCalendarEvent> tempEventList;
    Calendar minDate;
    Calendar maxDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_agenda);
        initControls();
        catchIntent();
    }

    private void initControls() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        appointmentList = new ArrayList<>();
        eventList = new ArrayList<>();
        tempEventList = new ArrayList<>();
        minDate = Calendar.getInstance();
        maxDate = Calendar.getInstance();
        // minimum and maximum date of the calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);
        mAgendaCalendarView = findViewById(R.id.agenda_calendar_view);
    }

    private void catchIntent() {
        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("userId");
            userType = getIntent().getStringExtra("userType");
        }

        if (userType.equals("petitioner")) {
            getUserAppointments("petitionerId");
        } else {
            getUserAppointments("bidderId");
        }
    }

    //CalendarView statements start here
    private void getUserAppointments(String refUserId) {
        Query getUserAppointmentsQuery = databaseReference.orderByChild(refUserId).equalTo(userId);
        getUserAppointmentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userAppointmentSnapshot : snapshot.getChildren()) {
                    appointmentList.add(userAppointmentSnapshot.getValue(Appointment.class));
                }
                mockEventsToCalendar(eventList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The appointments read failed: " + databaseError.getCode());
            }
        });
    }

    //CalendarPickerController statements start here
    @Override
    public void onDaySelected(DayItem dayItem) {
        openAppointmentCreation(dayItem);
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        openAppointmentDetail(event);
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    //Other statements start here
    public void goBackFromAppoAgenda(View view){
        finish();
    }

    private void openAppointmentCreation(DayItem dayItem) {
        Intent intent = new Intent(this, AppointmentCreationActivity.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(dayItem.getDate());
        intent.putExtra("dateSelected", formattedDate);
        startActivity(intent);
    }

    private void openAppointmentDetail(CalendarEvent event) {
        int colorOfEvent = 0;
        for (BaseCalendarEvent baseCalendarEvent : tempEventList) {
            if (baseCalendarEvent.getTitle().equals(event.getTitle())) {
                colorOfEvent = baseCalendarEvent.getColor();
            }
        }
        String newDay;
        String newMonth;
        if (event.getStartTime().get(Calendar.DAY_OF_MONTH) < 10) {
            newDay = "0" + event.getStartTime().get(Calendar.DAY_OF_MONTH);
        } else {
            newDay = "" + event.getStartTime().get(Calendar.DAY_OF_MONTH);
        }
        if (event.getStartTime().get(Calendar.MONTH) + 1 < 10) {
            newMonth = "0" + (event.getStartTime().get(Calendar.MONTH) + 1);
        } else {
            newMonth = "" + (event.getStartTime().get(Calendar.MONTH) + 1);
        }
        String formattedLongDate = newDay + "/" + newMonth + "/" + event.getStartTime().get(Calendar.YEAR)
                + " " + event.getStartTime().get(Calendar.HOUR_OF_DAY) + ":"
                + event.getStartTime().get(Calendar.MINUTE);
        finish();
        Intent intent = new Intent(this, AppointmentDetailActivity.class);
        intent.putExtra("dateSelected", formattedLongDate);
        intent.putExtra("appointmentTitle", event.getTitle());
        intent.putExtra("colorToDisplay", colorOfEvent);
        startActivity(intent);
    }

    private void mockEventsToCalendar(List<CalendarEvent> eventList) {
        try {
            for (Appointment appointment : appointmentList) {
                Calendar startTime = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                startTime.setTime(sdf.parse(appointment.getStartDateTime()));
                int startHour = startTime.get(Calendar.HOUR_OF_DAY);
                String amPm;
                if (startHour >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                String formattedMinutes = String.format("%02d", startTime.get(Calendar.MINUTE));
                String fullTime = startHour + ":" + formattedMinutes + amPm;
                BaseCalendarEvent event = new BaseCalendarEvent(appointment.getTitle(),
                        appointment.getDescription(), fullTime, pickRandomColor(), startTime,
                        startTime, true);
                eventList.add(event);
                tempEventList.add(event);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
    }

    private int pickRandomColor() {
        String[] colorsToPick = new String[]{"#F97304", "#D44206", "#023D67", "#03749C"};
        String randomStr = colorsToPick[new Random().nextInt(colorsToPick.length)];
        return Color.parseColor(randomStr);
    }
}
