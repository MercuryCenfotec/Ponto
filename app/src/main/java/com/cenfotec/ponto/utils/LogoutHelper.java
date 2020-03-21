package com.cenfotec.ponto.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cenfotec.ponto.MainActivity;

public class LogoutHelper {

    public static final String MY_PREFERENCES = "MyPrefs";

    public static void logout(Context packageContext) {
        SharedPreferences sharedPreferences = packageContext.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        Intent intent = new Intent(packageContext, MainActivity.class);
        packageContext.startActivity(intent);
    }
}
