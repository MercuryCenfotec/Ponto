package com.cenfotec.ponto.entities.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cenfotec.ponto.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationCardFragment extends Fragment {

  public NotificationCardFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_notification_card, container, false);
  }
}
