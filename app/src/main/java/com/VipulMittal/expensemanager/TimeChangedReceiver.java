package com.VipulMittal.expensemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.Calendar;

public class TimeChangedReceiver extends BroadcastReceiver {

	public static final String TAG="Vipul_tag";
	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 20);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		editor.putLong("notifTime", calendar1.getTimeInMillis());
		editor.apply();

		Log.d(TAG, "onReceive: TIME CHANGED");
	}
}
