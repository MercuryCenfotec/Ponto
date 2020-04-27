package com.cenfotec.ponto.entities.message;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.cenfotec.ponto.data.model.Message;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.entities.appointment.AppointmentAgendaActivity;
import com.cenfotec.ponto.utils.GeneralActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import adapter.MessageCard_Adapter;

public class ChatMessagesActivity extends GeneralActivity {

  private String userId, userType, chatId;
  private RecyclerView recyclerMessages;
  private Chat chat = new Chat();
  private List<Message> messages;
  private Appointment appointment;
  private ServicePetition petition;
  private MessageCard_Adapter messageCardAdapter;
  private ImageView btnImageReturn;
  private TextView txtReceiver, txtPetitionName, txtServiceType, txtMeetingDate, btnImgSend;
  private ConstraintLayout layoutMeetingInfo;
  private FrameLayout btnGoToMeeting;
  private EditText inputNewMessage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_messages);
    initComponents();

  }

  @Override
  protected void initComponents() {
    super.initComponents();
    messages = new ArrayList<>();
    userId = sharedPreferences.getString("userId", "");
    userType = sharedPreferences.getString("userType", "");
    chatId = sharedPreferences.getString("chatId", "");

    recyclerMessages = findViewById(R.id.recyclerMessages);

    btnImageReturn = findViewById(R.id.btnImageReturn);

    txtReceiver = findViewById(R.id.txtReceiver);
    txtPetitionName = findViewById(R.id.txtPetitionName);
    txtServiceType = findViewById(R.id.txtServiceType);
    txtMeetingDate = findViewById(R.id.txtMeetingDate);
    btnImgSend = findViewById(R.id.btnImgSend);

    layoutMeetingInfo = findViewById(R.id.layoutMeetingInfo);
    btnGoToMeeting = findViewById(R.id.btnGoToMeeting);

    inputNewMessage = findViewById(R.id.inputNewMessage);
    chargeAdapterViews();
  }

  @Override
  protected void chargeAdapterViews() {
    RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    messageCardAdapter = new MessageCard_Adapter(this, chat, messages, userId);

    recyclerMessages.setLayoutManager(layoutManager);
    recyclerMessages.setItemAnimator(new DefaultItemAnimator());
    recyclerMessages.setAdapter(messageCardAdapter);

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
    ref.child(chatId).addValueEventListener(new ValueEventListener() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Chat newChat = dataSnapshot.getValue(Chat.class);
        chat.setId(newChat.getId());
        chat.setServicePetitionId(newChat.getServicePetitionId());
        chat.setServicePetitionName(newChat.getServicePetitionName());
        chat.setPetitionerId(newChat.getPetitionerId());
        chat.setPetitionerImgUrl(newChat.getPetitionerImgUrl());
        chat.setPetitionerName(newChat.getPetitionerName());
        chat.setBidderId(newChat.getBidderId());
        chat.setBidderImgUrl(newChat.getBidderImgUrl());
        chat.setBidderName(newChat.getBidderName());
        chat.setUnreadBidder(newChat.getUnreadBidder());
        chat.setUnreadPetitioner(newChat.getUnreadPetitioner());
        chat.setMessages(newChat.getMessages());
        chat.setState(newChat.getState());
        if (chat.getMessages() != null) {
          messages.clear();
          messages.addAll(chat.getMessages());
        }
        messageCardAdapter.notifyDataSetChanged();
        if (chat.getState().equals("closed")) {
          findViewById(R.id.linearLayout12).setVisibility(View.GONE);
        } else {
          findViewById(R.id.linearLayout12).setVisibility(View.VISIBLE);
        }
        recyclerMessages.scrollToPosition(messages.size() - 1);
        setUserName();
        chargeAppointment();
        chargePetition();
        setComponents();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void setUserName() {
    if (!userType.equals("petitioner")) {
      txtReceiver.setText(chat.getPetitionerName());
    } else {
      txtReceiver.setText(chat.getBidderName());
    }
  }


  @RequiresApi(api = Build.VERSION_CODES.O)
  private void chargeAppointment() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Appointments");
    Query query;
    final DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
    if (userType.equals("petitioner")) {
      query = ref.orderByChild("petitionerId").equalTo(userId);
    } else {
      query = ref.orderByChild("bidderId").equalTo(userId);
    }
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          Appointment tempApp = snapshot.getValue(Appointment.class);
          LocalDateTime tempLocalDateTime = LocalDateTime.parse(tempApp.getStartDateTime(), customFormatter);
          if (appointment == null && LocalDateTime.now().compareTo(tempLocalDateTime) < 0) {
            appointment = tempApp;
          } else if (appointment != null) {
            LocalDateTime appoLocalDateTime = LocalDateTime.parse(appointment.getStartDateTime(), customFormatter);
            if (appoLocalDateTime.compareTo(tempLocalDateTime) < 0) {
              appointment = tempApp;
            }
          }
        }
        validateMeetingInfo();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void validateMeetingInfo() {
    DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm");
    if (appointment != null) {
      layoutMeetingInfo.setVisibility(View.VISIBLE);
      txtMeetingDate.setText(LocalDateTime.parse(appointment.getStartDateTime(), customFormatter).format(DateTimeFormatter.ofPattern("d/MM/yyyy")).toString());
      btnGoToMeeting.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          goToAppointments();
        }
      });
    } else {
      layoutMeetingInfo.setVisibility(View.GONE);
    }

  }

  private void chargePetition() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ServicePetitions");
    Query query = ref.child(chat.getServicePetitionId());
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        petition = dataSnapshot.getValue(ServicePetition.class);
        setPetitionInfo();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void setPetitionInfo() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ServiceTypes");
    Query query = ref.child(petition.getServiceTypeId());
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ServiceType type = dataSnapshot.getValue(ServiceType.class);
        txtServiceType.setText(type.getServiceType());
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
    txtPetitionName.setText(petition.getName());
  }

  private void setComponents() {
    btnImgSend.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onClick(View v) {
        sendMessage();
      }
    });
    inputNewMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
          sendMessage();
          return true;
        }
        return false;
      }
    });
    inputNewMessage.setImeActionLabel("Enviar", KeyEvent.KEYCODE_ENTER);
    btnImageReturn.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }


  @Override
  protected void setTabs() {

  }


  private void goToAppointments() {
    Intent iac = new Intent(this, AppointmentAgendaActivity.class);
    iac.putExtra("userId", userId);
    iac.putExtra("petitionerId", "");
    iac.putExtra("bidderId", "");
    if (userType.equals("petitioner")) {
      iac.putExtra("userType", "petitioner");
    } else {
      iac.putExtra("userType", "bidder");
    }
    startActivity(iac);
  }


  @RequiresApi(api = Build.VERSION_CODES.O)
  private void sendMessage() {
    String text = inputNewMessage.getText().toString();
    List<Message> messages;
    DatabaseReference ref = FirebaseDatabase.getInstance().
            getReference("Chats").child(chat.getId());

    if (text.length() > 0 && !text.equals("")) {
      if (chat.getMessages() == null) {
        messages = new ArrayList<>();
      } else {
        messages = chat.getMessages();
      }

      messages.add(new Message((messages.size() + ""), text, userId, LocalDateTime.now().toString()));
      chat.setMessages(messages);
      ref.setValue(chat);
      inputNewMessage.setText("");
    }
  }
}
