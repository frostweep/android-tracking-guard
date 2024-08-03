package com.frostweepgames.familyguard.AlarmSystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.frostweepgames.familyguard.Settings.Constants;

public class AlarmManagerLocal {

    private AlarmManager _alarmManager;
    private Context _context;

    public AlarmManagerLocal(Context context) {
        _context = context;

        init();
    }

    private void init() {
        _alarmManager = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarmRepeating(Context context, String action, String extra, int requestCode, long interval, long startTime) {
        Intent intent = createIntent(context, action, extra);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(_context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        _alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, interval, pendingIntent);
    }

    public void setAlarmTime(Context context, String action, String extra, int requestCode, long startTime) {
        Intent intent = createIntent(context, action, extra);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(_context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        _alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, pendingIntent);
    }

    public void unsetAlarm(Context context, String action, String extra, int requestCode) {
        Intent intent = createIntent(context, action, extra);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(_context, requestCode, intent, 0);
        _alarmManager.cancel(pendingIntent);
    }

    private Intent createIntent(Context context, String action, String extra) {
        Intent intent = new Intent(context, ActionsReceiver.class);
        intent.setAction(action);
        intent.putExtra(Constants.KEY_EXTRA, extra);
        return intent;
    }
}