package com.frostweepgames.apptracker.syncservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.frostweepgames.apptracker.MainActivity;
import com.frostweepgames.apptracker.data.StopApp;
import com.frostweepgames.apptracker.managers.AlarmTasksManager;
import com.frostweepgames.apptracker.managers.SQLiteManager;
import com.frostweepgames.apptracker.receivers.ExceptionsHandler;
import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.data.LocalAppSettings;
import com.frostweepgames.apptracker.managers.NetworkManager;
import com.frostweepgames.apptracker.R;
import com.frostweepgames.apptracker.managers.SettingsManager;
import com.frostweepgames.apptracker.data.SyncAppSettings;
import com.frostweepgames.apptracker.managers.SystemControllersManager;
import com.frostweepgames.apptracker.settings.Enumerators;

import java.util.ArrayList;
import java.util.HashMap;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class SyncBackgroundService extends IntentService {

    private NotificationManager _notificationManager;

    public static SystemControllersManager systemControllersManager;
    public static NetworkManager networkManager;
    public static SettingsManager settingsManager;
    public static SQLiteManager sqliteManager;
    public static AlarmTasksManager alarmTasksManager;
    public static ServiceRunnable serviceRunnable;

    public static SyncBackgroundService instance;

    private ArrayList<Thread> _syncTask,
            _fetchTask,
            _systemFuncControlTask;

    public Context context;


    public SyncBackgroundService() {
        super(Constants.APP_NAME);

        instance = this;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionsHandler(context));

        initFromService(context);
    }

    private void initFromService(Context context) {

        systemControllersManager = new SystemControllersManager(context);
        networkManager = new NetworkManager(context);
        settingsManager = new SettingsManager();
        alarmTasksManager = new AlarmTasksManager(context);
        serviceRunnable = new ServiceRunnable();

        settingsManager.localAppSettings = new LocalAppSettings();
        settingsManager.syncAppSettings = new SyncAppSettings();

        _notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onTaskRemoved(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sendNotification();

        serviceRunnable.run(() -> {
            sqliteManager = new SQLiteManager(context, Constants.KEY_DATABASE_NAME, null, Constants.KEY_DATABASE_VERSION);
            settingsManager.loadSettings(context, true);

            fillAndExecuteAsyncTasks();
        });

        return START_REDELIVER_INTENT;
    }

    private void fillAndExecuteAsyncTasks() {
        _syncTask = new ArrayList<>();
        _fetchTask = new ArrayList<>();
        _systemFuncControlTask = new ArrayList<>();

        _syncTask.add(new ServiceSyncData(Enumerators.ControllerType.CALLS,
                Integer.parseInt(settingsManager.syncAppSettings.period_transfer.call),
                Enumerators.APINetRequestType.UPDATE_CALLS,
                Constants.KEY_TABLE_CALLS, true));

        _syncTask.add(new ServiceSyncData(Enumerators.ControllerType.SMS,
                Integer.parseInt(settingsManager.syncAppSettings.period_transfer.sms),
                Enumerators.APINetRequestType.UPDATE_SMS,
                Constants.KEY_TABLE_SMS, true));

        _syncTask.add(new ServiceSyncData(Enumerators.ControllerType.CONTACTS,
                Integer.parseInt(settingsManager.syncAppSettings.period_transfer.contacts),
                Enumerators.APINetRequestType.UPDATE_CONTACTS,
                Constants.KEY_TABLE_CONTACTS, true));

        _syncTask.add(new ServiceSyncData(Enumerators.ControllerType.INSTALLED_APPS,
                Integer.parseInt(settingsManager.syncAppSettings.period_transfer.app),
                Enumerators.APINetRequestType.UPDATE_INSTALLED_APPS,
                Constants.KEY_TABLE_INSTALLED_APPS, true));

        _syncTask.add(new ServiceSyncData(Enumerators.ControllerType.BROWSER_HISTORY,
                Integer.parseInt(settingsManager.syncAppSettings.period_transfer.browser),
                Enumerators.APINetRequestType.UPDATE_BROWSER_HISTORY,
                Constants.KEY_TABLE_BROWSER_HISTORY, true));

        _syncTask.add(new ServiceSyncData(Enumerators.ControllerType.GPS,
                Integer.parseInt(settingsManager.syncAppSettings.period_transfer.gps),
                Enumerators.APINetRequestType.UPDATE_GPS,
                Constants.KEY_TABLE_GPS, true));

        _syncTask.add(new ServiceSyncData(Enumerators.ControllerType.SYSTEM,
                Integer.parseInt(settingsManager.syncAppSettings.period_transfer.system),
                Enumerators.APINetRequestType.SEND_SYSTEM,
                Constants.KEY_TABLE_SYSTEM_INF0, true));


        _fetchTask.add(new ServiceFetchData(Enumerators.ControllerType.CALLS,
                Integer.parseInt(settingsManager.syncAppSettings.period_withdrawal.call),
                Constants.KEY_TABLE_CALLS, false));

        _fetchTask.add(new ServiceFetchData(Enumerators.ControllerType.SMS,
                Integer.parseInt(settingsManager.syncAppSettings.period_withdrawal.sms),
                Constants.KEY_TABLE_SMS, false));

        _fetchTask.add(new ServiceFetchData(Enumerators.ControllerType.CONTACTS,
                Integer.parseInt(settingsManager.syncAppSettings.period_withdrawal.contacts),
                Constants.KEY_TABLE_CONTACTS, false));

        _fetchTask.add(new ServiceFetchData(Enumerators.ControllerType.INSTALLED_APPS,
                Integer.parseInt(settingsManager.syncAppSettings.period_withdrawal.app),
                Constants.KEY_TABLE_INSTALLED_APPS, false));

        _fetchTask.add(new ServiceFetchData(Enumerators.ControllerType.BROWSER_HISTORY,
                Integer.parseInt(settingsManager.syncAppSettings.period_withdrawal.browser),
                Constants.KEY_TABLE_BROWSER_HISTORY, false));

        _fetchTask.add(new ServiceFetchData(Enumerators.ControllerType.GPS,
                Integer.parseInt(settingsManager.syncAppSettings.period_withdrawal.gps),
                Constants.KEY_TABLE_GPS, false));

        _fetchTask.add(new ServiceFetchData(Enumerators.ControllerType.SYSTEM,
                Integer.parseInt(settingsManager.syncAppSettings.period_withdrawal.system),
                Constants.KEY_TABLE_SYSTEM_INF0, true));


        _systemFuncControlTask.add(new ServiceControlDeviceFunctionality(Enumerators.SystemFuncControlType.WIFI, 0.1, settingsManager.syncAppSettings.stop_net.wifi.start, settingsManager.syncAppSettings.stop_net.wifi.stop, null));
        _systemFuncControlTask.add(new ServiceControlDeviceFunctionality(Enumerators.SystemFuncControlType.MOBILE_DATA, 0.1, settingsManager.syncAppSettings.stop_net.gprs.start, settingsManager.syncAppSettings.stop_net.gprs.stop, null));

        for (StopApp app : settingsManager.syncAppSettings.stop_app)
            _systemFuncControlTask.add(new ServiceControlDeviceFunctionality(Enumerators.SystemFuncControlType.APP_OPENING, 0.1, app.start, app.stop, app.app));

        _systemFuncControlTask.add(new ServiceControlDeviceFunctionality(Enumerators.SystemFuncControlType.SETTINGS_UPDATE,
                Double.parseDouble(settingsManager.syncAppSettings.period_transfer.sync), null, null, null));

        for (Thread task : _syncTask)
            task.start();
        for (Thread task : _fetchTask)
            task.start();
        for (Thread task : _systemFuncControlTask)
            task.start();
    }

    public void relaunch()
    {
        resetTasks();
        fillAndExecuteAsyncTasks();
    }

    public void sendNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel();

        Intent notifyIntent = new Intent(context, MainActivity.class);
        //  notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // notifyIntent.setAction(Intent.ACTION_MAIN);
        //  notifyIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification;

        Notification.Builder builder = new Notification.Builder(context);

        builder = builder.setContentTitle(Constants.APP_NAME)
                .setContentText(Constants.NOTIFICATION_CONTENT_TEXT)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(notifyPendingIntent);

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

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = Constants.DEFAULT_NOTIFICATION_CHANNEL_ID;
            CharSequence channelName = Constants.APP_NAME + " Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            android.app.NotificationChannel notificationChannel = new android.app.NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.CYAN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void deleteNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.deleteNotificationChannel(Constants.DEFAULT_NOTIFICATION_CHANNEL_ID);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        resetTasks();

        _notificationManager.cancel(Constants.DEFAULT_NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            deleteNotificationChannel();

        stopSelf();
    }

    public void resetTasks()
    {
        for (Thread task : _syncTask)
            task.interrupt();
        for (Thread task : _fetchTask)
            task.interrupt();
        for (Thread task : _systemFuncControlTask)
            task.interrupt();

        _systemFuncControlTask.clear();
        _fetchTask.clear();
        _syncTask.clear();
    }

    public class ServiceSyncData extends Thread {

        private int _threadSleepTime;
        private Boolean _canStop = false;
        private Enumerators.ControllerType _currentType;
        private Enumerators.APINetRequestType _requestType;
        private boolean _useService;
        private String _keyTable;

        public ServiceSyncData(Enumerators.ControllerType type, int period, Enumerators.APINetRequestType requestType, String keyTable, boolean useService) {
            _currentType = type;
            _threadSleepTime = 1000 * 60 * period;
            _requestType = requestType;
            _keyTable = keyTable;
            _useService = useService;
        }

        public void run() {

            Looper.prepare();

            while (!_canStop) {

                try {
                    Thread.sleep(_threadSleepTime);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                try {
                    if (settingsManager.syncAppSettings.data_transfer.wifi.contains(Helper.convertControllerToString(_currentType)) && Helper.isWiFiNetworkAvailable(context)) {
                        Helper.syncDataFromDB(_requestType, _keyTable, _useService);
                    } else if (settingsManager.syncAppSettings.data_transfer.gprs.contains(Helper.convertControllerToString(_currentType)) && Helper.isGPRSNetworkAvailable(context)) {
                        Helper.syncDataFromDB(_requestType, _keyTable, _useService);
                    }

                } catch (Exception ex) {

                    Log.e("SYNC ERROR", ex.getMessage());
                }
            }
        }
    }

    public class ServiceFetchData extends Thread {

        private int _threadSleepTime;
        private Boolean _canStop = false;
        private Enumerators.ControllerType _currentType;
        private boolean _sysInfo;
        private String _keyTable;

        public ServiceFetchData(Enumerators.ControllerType type, int period, String keyTable, boolean sysInfo) {
            _currentType = type;
            _threadSleepTime = 1000 * 60 * period;
            _keyTable = keyTable;
            _sysInfo = sysInfo;
        }

        public void run() {
            Looper.prepare();

            while (!_canStop) {

                try {
                    Thread.sleep(_threadSleepTime);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                try {
                    if (!settingsManager.syncAppSettings.action_service.call && _currentType == Enumerators.ControllerType.CALLS) {
                    } else if (!settingsManager.syncAppSettings.action_service.sms && _currentType == Enumerators.ControllerType.SMS) {
                    } else if (!settingsManager.syncAppSettings.action_service.gps && _currentType == Enumerators.ControllerType.GPS) {
                    } else if (!settingsManager.syncAppSettings.action_service.contacts && _currentType == Enumerators.ControllerType.CONTACTS) {
                    } else if (!settingsManager.syncAppSettings.action_service.app && _currentType == Enumerators.ControllerType.INSTALLED_APPS) {
                    } else if (!settingsManager.syncAppSettings.action_service.browser && _currentType == Enumerators.ControllerType.BROWSER_HISTORY) {
                    } else if (!settingsManager.syncAppSettings.action_service.system && _currentType == Enumerators.ControllerType.SYSTEM) {
                    } else
                        putDataToDB(_keyTable, Helper.getDataFromController(context, _currentType, _sysInfo));
                } catch (Exception ex) {

                    Log.e("SYNC ERROR", ex.getMessage());

                }
            }
        }

        private void putDataToDB(String table, String data) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(Constants.TABLE_KEY_DATA, data);
            sqliteManager.addElementInDB(table, Helper.prepareContentValuesFromList(map));
        }
    }

    public class ServiceControlDeviceFunctionality extends Thread {

        private int _threadSleepTime;
        private Boolean _canStop = false;
        private Enumerators.SystemFuncControlType _systemFuncControlType;
        private String _beginDate;
        private String _endDate;
        private ArrayList<String> _additional;

        public ServiceControlDeviceFunctionality(Enumerators.SystemFuncControlType systemFuncControlType, Double period, String begin, String end, String additional) {
            _threadSleepTime = (int) (1000 * 60 * period);
            _systemFuncControlType = systemFuncControlType;
            _beginDate = begin;
            _endDate = end;

            if (additional != null) {
                _additional = new ArrayList<String>();
                _additional.add(additional);
            }
        }

        public void run() {
            Looper.prepare();

            while (!_canStop) {

                try {
                    Thread.sleep(_threadSleepTime);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                try {
                    if (_systemFuncControlType == Enumerators.SystemFuncControlType.MOBILE_DATA && Helper.isTimeBetween(Helper.getCurrentDate(), _beginDate, _endDate))
                        Helper.disableGPRS(context);
                    else if (_systemFuncControlType == Enumerators.SystemFuncControlType.WIFI && Helper.isTimeBetween(Helper.getCurrentDate(), _beginDate, _endDate))
                        Helper.disableWiFi(context);
                    else if (_systemFuncControlType == Enumerators.SystemFuncControlType.APP_OPENING && Helper.isTimeBetween(Helper.getCurrentDate(), _beginDate, _endDate))
                        Helper.killProccesses(context, _additional, Helper.isDeviceRooted());
                    else if (_systemFuncControlType == Enumerators.SystemFuncControlType.SETTINGS_UPDATE)
                        settingsManager.loadSyncSettings(true);
                } catch (Exception ex) {

                    Log.e("SYNC ERROR", ex.getMessage());

                }
            }
        }
    }
}