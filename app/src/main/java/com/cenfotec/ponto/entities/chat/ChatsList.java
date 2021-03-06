package com.cenfotec.ponto.entities.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.ChatCard_Adapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsList extends Fragment {

  public static final String MY_PREFERENCES = "MyPrefs";
  private String userId;
  private String userType;
  private SharedPreferences sharedPreferences;
  String filter;
  View view;
  private RecyclerView recyclerview;
  private List<Chat> chatList;
  private ChatCard_Adapter chatCard_adapter;
  private TextView chatsListEmpty;

  public ChatsList() {
  }

  public ChatsList(String filter) {
    this.filter = filter;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_chats_list, container, false);
    // Inflate the layout for this fragment
    initComponents();
    chargeContent();
    chargeChats();
    return view;
  }

  private void initComponents() {
    chatList = new ArrayList<>();
    recyclerview = view.findViewById(R.id.recycler);
    sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
    userId = sharedPreferences.getString("userId", "");
    userType = sharedPreferences.getString("userType", "");
    chatsListEmpty = view.findViewById(R.id.chatsListEmpty);

  }

  private void chargeContent() {
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    chatCard_adapter = new ChatCard_Adapter(getActivity(), chatList, userId);

    recyclerview.setLayoutManager(layoutManager);
    recyclerview.setItemAnimator(new DefaultItemAnimator());
    recyclerview.setAdapter(chatCard_adapter);

  }

  private void chargeChats() {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
    Query getChatQuery;
    if (userType.equals("petitioner")) {
      getChatQuery = databaseReference.orderByChild("petitionerId").equalTo(userId);
    } else {
      getChatQuery = databaseReference.orderByChild("bidderId").equalTo(userId);
    }

    getChatQuery.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        chatList.clear();
        for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
          Chat chat = chatSnapshot.getValue(Chat.class);
          if (chat.getState().equals(filter)) {
            chatList.add(chat);
          }
        }

        Collections.reverse(chatList);
        chatCard_adapter.notifyDataSetChanged();
        if (chatList.isEmpty()) {
          chatsListEmpty.setVisibility(View.VISIBLE);
          recyclerview.setVisibility(View.GONE);
        } else {
          recyclerview.setVisibility(View.VISIBLE);
          chatsListEmpty.setVisibility(View.GONE);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getCode());
      }
    });
  }
}
