package com.cenfotec.ponto.entities.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Notification;
import com.cenfotec.ponto.entities.account.AccountActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NotificationFactory {
  public static final String NOTIFICATION_CHANNEL_ID = "10001";
  public static final String NOTIFICATION_CHANNEL_NAME = "Notifications";

  //Simple notification that does not goes to Activity when pressed
  public static void createNotificationWithoutExtras(Context actualActivity, String title, String description) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(actualActivity, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(title)
            .setContentText(description);

    NotificationManager manager = (NotificationManager) actualActivity.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(0, builder.build());
  }

  public static void createNotificationWithoutExtras(Context actualActivity, Notification notification) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(actualActivity, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(notification.getTitle())
            .setContentText(notification.getDetail());
    Random random = new Random();

    NotificationManager manager = (NotificationManager) actualActivity.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE)+random.nextInt(9999 - 1000), builder.build());
  }

  // Notification that goes to other activity when pressed
  public static void createNotificationWithExtras(Context actualActivity, Class<AccountActivity> nextActivity,
                                                  String title, String description, HashMap<String, String> intentExtras) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(actualActivity, NOTIFICATION_CHANNEL_ID)
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
