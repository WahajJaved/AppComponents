package com.klan.alarmscheduler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int alarmNo = intent.getIntExtra("AlarmNo", 0);
		String details = "Alarm !!!";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel notificationChannel = new NotificationChannel("1", "Alarm",
							NotificationManager.IMPORTANCE_DEFAULT);
			notificationChannel.setDescription("Alarm Notify");
			NotificationManager mNotifyManager =
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotifyManager.createNotificationChannel(notificationChannel);
			NotificationCompat.Builder notification = new NotificationCompat.
					Builder(context, "1").setSmallIcon(R.drawable.ic_launcher_foreground).
					setContentText(details).setContentTitle("Alarm Number " + alarmNo);

			Intent contentIntent = new Intent(context, DisplayActivity.class);
			PendingIntent pendingContentIntent = PendingIntent.getActivity(context, 0,
					contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setContentIntent(pendingContentIntent);
			mNotifyManager.notify(1, notification.build());
		}
	}
}
