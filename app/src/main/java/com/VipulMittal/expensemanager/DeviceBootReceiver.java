package com.VipulMittal.expensemanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
		{
			Calendar calendar=Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 20);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Intent intent1 = new Intent(context, Receiver.class);
			MainActivity.pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			if(alarmManager != null) {
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, MainActivity.pendingIntent);
			}
		}
	}
}
