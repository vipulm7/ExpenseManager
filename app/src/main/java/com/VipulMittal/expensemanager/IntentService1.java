package com.VipulMittal.expensemanager;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

public class IntentService1 extends IntentService {

	public IntentService1() {
		super("Receiver");
	}

	public static final String TAG="Vipul";
	public static final String ACTION_SNOOZE="com.example.android.wearable.wear.wearnotifications.handlers.action.SNOOZE";
	public static final String ACTION_DISMISS="com.example.android.wearable.wear.wearnotifications.handlers.action.DISMISS";
	NotificationCompat.Builder builder;
	NotificationManagerCompat notificationManagerCompat;
//	public int snoozeTimeInMillies=3600000;
	public int snoozeTimeInMillies=30000;
	public final String CHANNEL_ID="1";
	public static final int notifID =2;

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		Log.d(TAG, "onHandleIntent: ");
		if(intent!=null)
		{
//			Toast.makeText(getApplicationContext(),"Hey there!!!", Toast.LENGTH_LONG).show();
			final String actionReceived=intent.getAction();
			Log.d(TAG, "onHandleIntent: intent = "+intent);
			Log.d(TAG, "onHandleIntent: string = "+actionReceived);
			if(actionReceived!=null) {
				if (actionReceived.equals(ACTION_SNOOZE)) {
					Log.d(TAG, "onHandleIntent: ACTION_SNOOZE");
					snooze();
				} else if (actionReceived.equals(ACTION_DISMISS)) {
					Log.d(TAG, "onHandleIntent: ACTION_DISMISS");
					dismiss();
				}
			}
		}
	}

	private void dismiss() {
		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
		notificationManagerCompat.cancel(MainActivity.notifID);
	}

	private void snooze() {
		builder= GlobalNotificationBuilder.getGlobalNotificationCompatBuilder();

		if(builder==null)
			recreateBuilder();

		Notification notification=builder.build();
		if(notification != null)
		{
			Log.d(TAG, "onHandleIntent: "+builder);
			notificationManagerCompat=NotificationManagerCompat.from(getApplicationContext());
			notificationManagerCompat.cancel(notifID);

			try {
				Thread.sleep(snoozeTimeInMillies);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			notificationManagerCompat.notify(notifID, notification);
		}
	}

	private void recreateBuilder() {
		builder=new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
		GlobalNotificationBuilder.setGlobalNotificationCompatBuilder(builder);

		Log.d(TAG, "recreateBuilder: ");
		builder.setSmallIcon(R.drawable.ic_notifications)
				.setContentTitle("Add today's records!")
				.setContentText("Add today's records!")
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText("Add today's records!"))
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setColor(ContextCompat.getColor(this, R.color.cyan));


		// Normal notification's intent
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
			pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		else
			pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		builder.setAutoCancel(true); //notif is removed when user clicks on it. works only when intent is passed to builder.


		//Notification to snooze
		Intent snoozeIntent = new Intent(this, IntentService1.class);
		snoozeIntent.setAction(ACTION_SNOOZE);
		PendingIntent snoozePendingIntent;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
			snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		else
			snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Action snoozeAction=new NotificationCompat.Action.Builder(R.drawable.ic_notifications, "Snooze it!", snoozePendingIntent).build();

		Intent dismissIntent = new Intent(this, IntentService1.class);
		dismissIntent.setAction(ACTION_DISMISS);
		PendingIntent dismissPendingIntent;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
			dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		else
			dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Action dismissAction = new NotificationCompat.Action.Builder(R.drawable.ic_notifications, "Nothing today!", dismissPendingIntent).build();

		builder.addAction(snoozeAction);
		builder.addAction(dismissAction);

		Context context = getApplicationContext();

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		Intent intent1 = new Intent(context, IntentService1.class);
		context.startService(intent1);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		long notifTime = sharedPreferences.getLong("notifTime", -1);
		editor.putLong("notifTime", notifTime+ AlarmManager.INTERVAL_DAY);
		editor.apply();

		Log.d(TAG, "notifTime before: recreate"+notifTime);
		Log.d(TAG, "notifTime after: recreate"+sharedPreferences.getLong("notifTime", -1));

		GlobalNotificationBuilder.setGlobalNotificationCompatBuilder(builder);
	}
}
