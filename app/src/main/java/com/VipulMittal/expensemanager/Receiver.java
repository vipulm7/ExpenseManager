package com.VipulMittal.expensemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent1= new Intent(context, IntentService1.class);
		context.startService(intent);
	}
}
