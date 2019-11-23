package com.klan.alarmscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import java.util.Calendar;

public class AlarmService extends Service {


	private static AlarmManager alarmMgr;

	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}


	public void onCreate() {
		super.onCreate();
		setAlarms(getApplicationContext(),1);
		setAlarms(getApplicationContext(),2);
	}

	public void setAlarms(Context context, int alarmId) {
		SharedPreferences prefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);


		PendingIntent alarmPendingIntent;
		Intent alarmIntent = new Intent(context, AlarmReceiver.class);
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, alarmIntent, 0);

		long alarmTime;

		if (alarmId == 1)
			alarmTime = prefs.getLong("alarm1Time", 300000);
		else
			alarmTime = prefs.getLong("alarm2Time", 300000);

		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime,
				AlarmManager.INTERVAL_DAY, alarmPendingIntent);
	}
}
