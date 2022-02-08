package com.VipulMittal.expensemanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import java.util.Calendar;

public class Receiver extends BroadcastReceiver {

	public static final String ACTION_SNOOZE="com.example.android.wearable.wear.wearnotifications.handlers.action.SNOOZE";
	public static final String ACTION_DISMISS="com.example.android.wearable.wear.wearnotifications.handlers.action.DISMISS";
	public final String CHANNEL_ID="1";
	public static final int notifID=2;
	NotificationManagerCompat notificationManager;


	@Override
	public void onReceive(Context context, Intent intent2) {

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean showNotif=sharedPreferences.getBoolean("notifs", true);

		if(showNotif)
		{
			NotificationCompat.Builder builder= new NotificationCompat.Builder(context, CHANNEL_ID);
			builder.setSmallIcon(R.drawable.ic_notifications)
					.setContentTitle("Add today's records!")
					.setContentText("Add today's records!")
					.setStyle(new NotificationCompat.BigTextStyle()
							.bigText("Add today's records!"))
					.setPriority(NotificationCompat.PRIORITY_DEFAULT)
					.setColor(ContextCompat.getColor(context, R.color.cyan));

			notificationManager = MainActivity.notificationManager;


			Intent intent = new Intent(context, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			builder.setContentIntent(pendingIntent);
			builder.setAutoCancel(true);

			Intent snoozeIntent = new Intent(context, IntentService1.class);
			snoozeIntent.setAction(ACTION_SNOOZE);
			PendingIntent snoozePendingIntent = PendingIntent.getService(context, 0, snoozeIntent, 0);
			NotificationCompat.Action snoozeAction = new NotificationCompat.Action.Builder(R.drawable.ic_notifications,
					"Snooze!", snoozePendingIntent).build();


			Intent dismissIntent = new Intent(context, IntentService1.class);
			dismissIntent.setAction(ACTION_DISMISS);
			PendingIntent dismissPendingIntent = PendingIntent.getService(context, 0, dismissIntent, 0);
			NotificationCompat.Action dismissAction = new NotificationCompat.Action.Builder(R.drawable.ic_notifications,
					"Nothing today!", dismissPendingIntent).build();

			builder.addAction(snoozeAction);
			builder.addAction(dismissAction);

			GlobalNotificationBuilder.setGlobalNotificationCompatBuilder(builder);

			notificationManager.notify(notifID, builder.build());
			Intent intent1 = new Intent(context, IntentService1.class);
			context.startService(intent1);
		}
	}
}
