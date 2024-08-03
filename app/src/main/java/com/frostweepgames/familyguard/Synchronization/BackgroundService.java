package com.frostweepgames.familyguard.Synchronization;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.frostweepgames.androidhelperlibrary.tools.LogTracker;
import com.frostweepgames.familyguard.MainActivity;
import com.frostweepgames.familyguard.R;
import com.frostweepgames.familyguard.Settings.Constants;
import com.frostweepgames.familyguard.Tools.ExceptionsHandler;

public class BackgroundService extends IntentService {

    private NotificationManager _notificationManager;

    private Context _context;
    private Intent _intent;

    public static BackgroundService instance;

    public BackgroundService() {
        super(Constants.APP_CHANNEL_NAME);

        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _context = this;

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionsHandler(_context));

        initService();
    }

    @Override
    public void onTaskRemoved(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sendNotification();

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        _intent = intent;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        _notificationManager.cancel(Constants.DEFAULT_NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            deleteNotificationChannel();

        stopSelf();
    }

    private void sendNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel();

        Intent notifyIntent = new Intent(_context, MainActivity.class);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(_context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification;

        Notification.Builder builder = new Notification.Builder(_context);

        builder = builder.setContentTitle(Constants.APP_CHANNEL_NAME)
                .setContentText(Constants.NOTIFICATION_CONTENT_TEXT)
               // .setSmallIcon(R.drawable.icon_64)
                .setContentIntent(notifyPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(ContextCompat.getColor(_context, R.color.notification_background));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder = builder.setChannelId(Constants.DEFAULT_NOTIFICATION_CHANNEL_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            notification = builder.build();
        else
            notification = builder.getNotification();

        startForeground(Constants.DEFAULT_NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
                String channelId = Constants.DEFAULT_NOTIFICATION_CHANNEL_ID;
                CharSequence channelName = Constants.APP_CHANNEL_NAME + " Channel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                android.app.NotificationChannel notificationChannel = new android.app.NotificationChannel(channelId, channelName, importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.CYAN);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(notificationChannel);
            } catch (Exception ex) {
                LogTracker.trackLog(ex);
            }
        }
    }

    private void deleteNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.deleteNotificationChannel(Constants.DEFAULT_NOTIFICATION_CHANNEL_ID);
            } catch (Exception ex) {
                LogTracker.trackLog(ex);
            }
        }
    }

    private void initService()
    {
        _notificationManager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
    }
}