package com.cenfotec.ponto.entities.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

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

public class AppointmentAgendaActivity extends AppCompatActivity implements CalendarPickerController{

    AgendaCalendarView mAgendaCalendarView;
    String userId;
    String userType;
    final List<Appointment> appointmentList = new ArrayList<>();
    List<CalendarEvent> eventList = new ArrayList<>();
    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_agenda);

        if(getIntent().getExtras() != null){
            userId = getIntent().getStringExtra("userId");
            userType = getIntent().getStringExtra("userType");
        }

        if(userType.equals("petitioner")){
            getPetitionerAppointments();
        }else{
            getBidderAppointments();
        }

        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        mAgendaCalendarView = findViewById(R.id.agenda_calendar_view);
    }

    private void getPetitionerAppointments() {
        final DatabaseReference usersDBReference = FirebaseDatabase.getInstance().getReference("Appointments");
        Query query = usersDBReference.orderByChild("petitionerId").equalTo(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    //fill the calendar appointments list
                    appointmentList.add(data.getValue(Appointment.class));
                }
                //fill the calendar
                mockList(eventList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The appointments read failed: " + databaseError.getCode());
            }
        });
    }

    private void getBidderAppointments() {
        final DatabaseReference usersDBReference = FirebaseDatabase.getInstance().getReference("Appointments");
        Query query = usersDBReference.orderByChild("bidderId").equalTo(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    //fill the calendar appointments list
                    appointmentList.add(data.getValue(Appointment.class));
                }
                //fill the calendar
                mockList(eventList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The appointments read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        showToaster("day selected: " + dayItem);
        Intent intent = new Intent(this, AppointmentCreationActivity.class);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formatedDate = sdf.format(dayItem.getDate());

        intent.putExtra("dateSelected", formatedDate);
        startActivity(intent);
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Intent intent = new Intent(this, AppointmentUpdateActivity.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String df = event.getStartTime().get(Calendar.DAY_OF_MONTH) + "/" + (event.getStartTime().get(Calendar.MONTH) + 1) + "/"
                + event.getStartTime().get(Calendar.YEAR) + " " + event.getStartTime().get(Calendar.HOUR_OF_DAY) + ":"
                + event.getStartTime().get(Calendar.MINUTE);
        intent.putExtra("dateSelected", df);
        intent.putExtra("appointmentTitle", event.getTitle());
        startActivity(intent);
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void mockList(List<CalendarEvent> eventList) {
        try {
            for (Appointment appointment : appointmentList) {
                Calendar startTime = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                startTime.setTime(sdf.parse(appointment.getStartDateTime()));
                int startHour = startTime.get(Calendar.HOUR_OF_DAY);
                String amPm;
                if(startHour >= 12){
                    amPm = "PM";
                }else{
                    amPm = "AM";
                }
                String fs = String.format("%02d", startTime.get(Calendar.MINUTE));
                String fullTime = startHour + ":" + fs + amPm;
                BaseCalendarEvent event = new BaseCalendarEvent(appointment.getTitle(), appointment.getDescription(), fullTime,
                        randomColor(), startTime, startTime, true);
                eventList.add(event);

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
    }

    private int randomColor(){
        String[] colorsToPick = new String[]{"#F97304", "#D44206", "#023D67", "#03749C"};
        String randomStr = colorsToPick[new Random().nextInt(colorsToPick.length)];
        return Color.parseColor(randomStr);
    }
}
