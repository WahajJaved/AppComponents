package com.klan.alarmscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener{

	Button setAlarm1Time;
	Button setAlarm2Time;
	Button cancelAlarm1;
	Button cancelAlarm2;
	TextView chosenTime1;
	TextView chosenTime2;
	AlarmManager alarmMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		chosenTime1 = findViewById(R.id.alarm1time);

		setAlarm1Time = findViewById(R.id.alarm1time_set);
		setAlarm1Time.setOnClickListener(this);

		cancelAlarm1 = findViewById(R.id.alarm1cancel);
		cancelAlarm1.setOnClickListener(this);


		chosenTime2 = findViewById(R.id.alarm2time);

		setAlarm2Time = findViewById(R.id.alarm2time_set);
		setAlarm2Time.setOnClickListener(this);

		cancelAlarm2 = findViewById(R.id.alarm2cancel);
		cancelAlarm2.setOnClickListener(this);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
		BootReceiver receiver = new BootReceiver();
		registerReceiver(receiver, intentFilter);
	}

	public void onClick(View v) {
		String cancelText = "Alarm Cancelled";

		switch (v.getId()) {
			case R.id.alarm1time_set: {
				addAlarm(R.id.alarm1time);
				break;
			}
			case R.id.alarm2time_set: {
				addAlarm(R.id.alarm2time);
				break;
			}
			case R.id.alarm1cancel: {
				cancelAlarm(1);
				Toast.makeText(this, "Alarm 1 Cancelled.", Toast.LENGTH_SHORT).show();
				chosenTime1.setText(cancelText);
				break;
			}
			case R.id.alarm2cancel: {
				cancelAlarm(2);
				Toast.makeText(this, "Alarm 2 Cancelled.", Toast.LENGTH_SHORT).show();
				chosenTime2.setText(cancelText);
				break;
			}
		}
	}
	public void addAlarm(final int request) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		TimePickerDialog timePickerDialog = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hour,
					                      int minute) {
						String alarmText = "Alarm set for " + hour + ":" + String.format("%02d", minute);
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, hour);
						calendar.set(Calendar.MINUTE, minute);
						calendar.set(Calendar.SECOND, 0);
						if (request == R.id.alarm1time) {
							chosenTime1.setText(alarmText);
							scheduleAlarm(calendar, 1);
						} else if (request == R.id.alarm2time) {
							chosenTime2.setText(alarmText);
							scheduleAlarm(calendar, 2);
						}

					}
				}, hour, minute, true);

		timePickerDialog.show();
	}
	public void scheduleAlarm(Calendar calendar, int request ) {
		SharedPreferences.Editor prefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE).edit();

		if (request == 1)
			prefs.putLong("alarm1Time", calendar.getTimeInMillis());
		else
			prefs.putLong("alarm2Time", calendar.getTimeInMillis());

		Intent alarmIntent = new Intent(this, AlarmReceiver.class);
		alarmIntent.putExtra("AlarmNo", request);

		PendingIntent alarmPendingIntent;
		alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		alarmPendingIntent = PendingIntent.getBroadcast(this, request, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, alarmPendingIntent);
	}

	public void cancelAlarm(int request) {
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, request, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(alarmIntent);
		alarmIntent.cancel();
	}
}
