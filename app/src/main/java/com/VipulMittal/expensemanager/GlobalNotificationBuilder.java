package com.VipulMittal.expensemanager;

import androidx.core.app.NotificationCompat;

public class GlobalNotificationBuilder {
	private static NotificationCompat.Builder globalNotificationCompatBuilder = null;

	private GlobalNotificationBuilder() {
	}

	public static NotificationCompat.Builder getGlobalNotificationCompatBuilder() {
		return globalNotificationCompatBuilder;
	}

	public static void setGlobalNotificationCompatBuilder(NotificationCompat.Builder builder) {
		globalNotificationCompatBuilder = builder;
	}
}
