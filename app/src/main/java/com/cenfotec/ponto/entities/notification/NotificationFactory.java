package com.cenfotec.ponto.entities.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.entities.account.AccountActivity;

import java.util.HashMap;
import java.util.Map;

public class NotificationFactory {

  //Simple notification that does not goes to Activity when pressed
  public static void createNotificationWithoutExtras(Context actualActivity, String title, String description) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(actualActivity)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(title)
            .setContentText(description);

    NotificationManager manager = (NotificationManager) actualActivity.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(0, builder.build());
  }


  // Notification that goes to other activity when pressed
  public static void createNotificationWithExtras(Context actualActivity, Class<AccountActivity> nextActivity,
                                                  String title, String description, HashMap<String, String> intentExtras) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(actualActivity)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(title)
            .setContentText(description);

    Intent notificationIntent = new Intent(actualActivity, nextActivity);
    //foreach extra that the activity needs, assign them from the hashMap
    for (Map.Entry<String, String> entry : intentExtras.entrySet()) {
      notificationIntent.putExtra(entry.getKey(), entry.getValue());
    }

    PendingIntent contentIntent = PendingIntent.getActivity(actualActivity, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);
    builder.setContentIntent(contentIntent);

    NotificationManager manager = (NotificationManager) actualActivity.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(0, builder.build());
  }

}
