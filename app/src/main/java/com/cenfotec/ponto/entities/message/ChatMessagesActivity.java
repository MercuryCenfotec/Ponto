package com.cenfotec.ponto.entities.message;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Appointment;
import com.cenfotec.ponto.data.model.Chat;
import com.cenfotec.ponto.entities.appointment.AppointmentCreationActivity;
import com.cenfotec.ponto.entities.appointment.AppointmentDetailActivity;
import com.cenfotec.ponto.utils.GeneralActivity;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.Calendar;

import adapter.MessageCard_Adapter;

public class ChatMessagesActivity extends GeneralActivity {

  private String userId, userType, chatId;
  private RecyclerView recyclerMessages;
  private Chat chat;
  private Appointment appointment;
  private MessageCard_Adapter messageCardAdapter;
  private ImageView btnImageReturn, btnImageOption;
  private TextView txtReceiver, txtPetitionName, txtServiceType, btnCreateMeeting, txtMeeting, btnImgSend;
  private ConstraintLayout layoutMeetingInfo, btnGoToMeeting;
  private EditText inputNewMessage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_messages);
    initComponents();
    setComponents();

  }

  @Override
  protected void initComponents() {
    super.initComponents();
    chat = new Chat();

    userId = sharedPreferences.getString("userId", "");
    userType = sharedPreferences.getString("userType", "");
    chatId = sharedPreferences.getString("chatId", "");

    recyclerMessages = findViewById(R.id.recyclerMessages);

    btnImageReturn = findViewById(R.id.btnImageReturn);
    btnImageOption = findViewById(R.id.btnImageOption);

    txtReceiver = findViewById(R.id.txtReceiver);
    txtPetitionName = findViewById(R.id.txtPetitionName);
    txtServiceType = findViewById(R.id.txtServiceType);
    btnCreateMeeting = findViewById(R.id.btnCreateMeeting);
    txtMeeting = findViewById(R.id.txtMeeting);
    btnImgSend = findViewById(R.id.btnImgSend);

    layoutMeetingInfo = findViewById(R.id.layoutMeetingInfo);
    btnGoToMeeting = findViewById(R.id.btnGoToMeeting);

    inputNewMessage = findViewById(R.id.inputNewMessage);
  }


  private void setComponents() {
  }

  @Override
  protected void chargeAdapterViews() {
    RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    messageCardAdapter = new MessageCard_Adapter(this, chat, userId);

    recyclerMessages.setLayoutManager(layoutManager);
    recyclerMessages.setItemAnimator(new DefaultItemAnimator());
    recyclerMessages.setAdapter(messageCardAdapter);

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
    ref.child("id").equalTo(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        chat = dataSnapshot.getValue(Chat.class);
        setUserName();
        messageCardAdapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
    chargeAppointment();
  }

  private void chargeAppointment() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Appointments");
    Query query;
    if (userType.equals("petitioner")) {
      query = ref.orderByChild("petitionerId").equalTo(userId);
    } else {
      query = ref.orderByChild("bidderId").equalTo(userId);
    }
    query.addValueEventListener(new ValueEventListener() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          if (appointment == null && LocalDateTime.now().compareTo(LocalDateTime.parse(snapshot.getValue(Appointment.class).getStartDateTime())) > 0) {
            snapshot.getValue(Appointment.class);
          } else {
            Appointment tempApp = snapshot.getValue(Appointment.class);
            if (LocalDateTime.parse(appointment.getStartDateTime()).compareTo(LocalDateTime.parse(tempApp.getStartDateTime())) < 0) {
              snapshot.getValue(Appointment.class);
            }
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }


  private void setUserName() {
    if (userType.equals("petitioner")) {
      txtReceiver.setText(chat.getPetitionerName());
    } else {
      txtReceiver.setText(chat.getBidderName());
    }
  }

  @Override
  protected void setTabs() {

  }

  private void validateMeetingInfo() {
    if (appointment == null && userType.equals("petitioner")) {
      btnCreateMeeting.setVisibility(View.VISIBLE);
      btnCreateMeeting.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          redirectTo(AppointmentCreationActivity.class);
        }
      });
    } else {
      layoutMeetingInfo.setVisibility(View.VISIBLE);
      txtMeeting.setText(appointment.getStartDateTime());
      btnGoToMeeting.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          goToAppointmentDetail();
        }
      });
    }

  }

  private void goToAppointmentDetail() {
    int colorOfEvent = 0;
    for (BaseCalendarEvent baseCalendarEvent : tempEventList) {
      if (baseCalendarEvent.getTitle().equals(event.getTitle())) {
        colorOfEvent = baseCalendarEvent.getColor();
      }
    }
    String newDay;
    String newMonth;
    if (LocalDateTime.parse(appointment.getStartTime()).get(Calendar.DAY_OF_MONTH) < 10) {
      newDay = "0" + appointment.getStartTime().get(Calendar.DAY_OF_MONTH);
    } else {
      newDay = "" + appointment.getStartTime().get(Calendar.DAY_OF_MONTH);
    }
    if (appointment.getStartTime().get(Calendar.MONTH) + 1 < 10) {
      newMonth = "0" + (appointment.getStartTime().get(Calendar.MONTH) + 1);
    } else {
      newMonth = "" + (appointment.getStartTime().get(Calendar.MONTH) + 1);
    }
    String formattedLongDate = newDay + "/" + newMonth + "/" + appointment.getStartTime().get(Calendar.YEAR)
            + " " + appointment.getStartTime().get(Calendar.HOUR_OF_DAY) + ":"
            + appointment.getStartTime().get(Calendar.MINUTE);
    Intent intent = new Intent(this, AppointmentDetailActivity.class);
    intent.putExtra("dateSelected", formattedLongDate);
    intent.putExtra("appointmentTitle", appointment.getTitle());
    intent.putExtra("colorToDisplay", colorOfEvent);
    intent.putExtra("petitionerId", appointment.getPetitionerId());
    intent.putExtra("bidderId", appointment.getBidderId());
    startActivity(intent);
  }

  private void sendMessage() {

  }
}
