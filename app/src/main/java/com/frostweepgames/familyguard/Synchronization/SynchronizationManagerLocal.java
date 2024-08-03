package com.frostweepgames.familyguard.Synchronization;

import android.content.Context;
import android.util.Log;

import com.frostweepgames.androidhelperlibrary.controllers.LocalBrowsersHistoryController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalCallsController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalContactsController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalGPSController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalInstalledAppsController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalSMSContoller;
import com.frostweepgames.androidhelperlibrary.tools.LogTracker;
import com.frostweepgames.androidhelperlibrary.tools.NetworkManagementTool;
import com.frostweepgames.androidhelperlibrary.tools.SerializationTool;
import com.frostweepgames.androidhelperlibrary.tools.SystemInfoTool;
import com.frostweepgames.androidhelperlibrary.tools.TimeManagementTool;
import com.frostweepgames.familyguard.AlarmSystem.AlarmManagerLocal;
import com.frostweepgames.familyguard.Settings.Constants;
import com.frostweepgames.familyguard.Settings.Enumerators;
import com.frostweepgames.familyguard.Tools.Converter;
import com.frostweepgames.familyguard.Tools.ManagersTool;

import java.util.ArrayList;
import java.util.Calendar;

public class SynchronizationManagerLocal {

    private Context _context;

    public SynchronizationManagerLocal(Context context) {
        _context = context;
    }

    public void setupSynchronization() {

        AlarmManagerLocal alarmManager = ManagersTool.getAlarmManager(_context);

        alarmManager.setAlarmRepeating(_context,
                Constants.ACTION_ALARM_SYNC_AT_NIGHT,
                Constants.KEY_EXTRA,
                Constants.SYNC_AT_NIGHT_ALARM_REQUEST_CODE,
                Constants.DAY_INTERVAL,
                TimeManagementTool.getTimestampTillHourAndMinute(Constants.SYNC_AT_NIGHT_TIME_HOUR, Constants.SYNC_AT_NIGHT_TIME_MINUTE));

        alarmManager.setAlarmRepeating(_context,
                Constants.ACTION_ALARM_SYNC_GPS,
                Constants.KEY_EXTRA,
                Constants.SYNC_GPS_ALARM_REQUEST_CODE,
                1000 * 60 * 5,
                TimeManagementTool.getCurrentTimestamp());

        alarmManager.setAlarmRepeating(_context,
                Constants.ACTION_ALARM_SYNC_SYSTEM,
                Constants.KEY_EXTRA,
                Constants.SYNC_SYSTEM_ALARM_REQUEST_CODE,
                1000 * 60 * 60,
                TimeManagementTool.getCurrentTimestamp());

        alarmManager.setAlarmRepeating(_context,
                Constants.ACTION_ALARM_GET_SYNC_SETTINGS,
                Constants.KEY_EXTRA,
                Constants.GET_SYNC_SETTINGS_ALARM_REQUEST_CODE,
                1000 * 60 * 60,
                TimeManagementTool.getCurrentTimestamp());
    }

    public void cancelSynchronization() {
        AlarmManagerLocal alarmManager = ManagersTool.getAlarmManager(_context);

        alarmManager.unsetAlarm(_context, Constants.ACTION_ALARM_SYNC_AT_NIGHT, Constants.KEY_EXTRA, Constants.SYNC_AT_NIGHT_ALARM_REQUEST_CODE);
        alarmManager.unsetAlarm(_context, Constants.ACTION_ALARM_SYNC_GPS, Constants.KEY_EXTRA, Constants.SYNC_GPS_ALARM_REQUEST_CODE);
        alarmManager.unsetAlarm(_context, Constants.ACTION_ALARM_SYNC_SYSTEM, Constants.KEY_EXTRA, Constants.SYNC_SYSTEM_ALARM_REQUEST_CODE);
        alarmManager.unsetAlarm(_context, Constants.ACTION_ALARM_GET_SYNC_SETTINGS, Constants.KEY_EXTRA, Constants.GET_SYNC_SETTINGS_ALARM_REQUEST_CODE);
    }


    public void syncDataAtNight() {
        if (SystemInfoTool.isDeviceCharging(_context) && NetworkManagementTool.isNetworkAvailable(_context)) {

            com.frostweepgames.familyguard.Networking.Enumerators.NetworkRequestType postRequest =
                    com.frostweepgames.familyguard.Networking.Enumerators.NetworkRequestType.POST;

            if (ManagersTool.getUserManager(_context).syncSettings.action_service.browser) {
                try {
                    ManagersTool.getNetworkManager(_context).sendRequest(Constants.SERVER_SYNC_BROWSERS_HISTORY, postRequest,
                            Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA,
                                    SerializationTool.serializeObject(ManagersTool.getDataControllersManager(_context).getBrowsersHistoryController().getContent())), true, null, null);

                    ManagersTool.getDataControllersManager(_context).getBrowsersHistoryController().clear();
                } catch (Exception ex) {
                    LogTracker.trackLog(ex);
                }
            }

            if (ManagersTool.getUserManager(_context).syncSettings.action_service.call) {
                try {
                    ManagersTool.getNetworkManager(_context).sendRequest(Constants.SERVER_SYNC_CALLS, postRequest,
                            Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA,
                                    SerializationTool.serializeObject(ManagersTool.getDataControllersManager(_context).getCallsController().getContent())), true, null, null);

                    ManagersTool.getDataControllersManager(_context).getCallsController().clear();
                } catch (Exception ex) {
                    LogTracker.trackLog(ex);
                }
            }

            if (ManagersTool.getUserManager(_context).syncSettings.action_service.contacts) {
                try {
                    ManagersTool.getNetworkManager(_context).sendRequest(Constants.SERVER_SYNC_CONTACTS, postRequest,
                            Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA,
                                    SerializationTool.serializeObject(ManagersTool.getDataControllersManager(_context).getContactsController().getContent())), true, null, null);

                    ManagersTool.getDataControllersManager(_context).getContactsController().clear();
                } catch (Exception ex) {
                    LogTracker.trackLog(ex);
                }
            }

            if (ManagersTool.getUserManager(_context).syncSettings.action_service.app) {
                try {
                    ManagersTool.getNetworkManager(_context).sendRequest(Constants.SERVER_SYNC_INSTALLED_APPS, postRequest,
                            Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA,
                                    SerializationTool.serializeObject(ManagersTool.getDataControllersManager(_context).getInstalledAppsController().getContent())), true, null, null);

                    ManagersTool.getDataControllersManager(_context).getInstalledAppsController().clear();
                } catch (Exception ex) {
                    LogTracker.trackLog(ex);
                }
            }

            if (ManagersTool.getUserManager(_context).syncSettings.action_service.sms) {
                try {
                    ManagersTool.getNetworkManager(_context).sendRequest(Constants.SERVER_SYNC_SMS, postRequest,
                            Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA,
                                    SerializationTool.serializeObject(ManagersTool.getDataControllersManager(_context).getSMSContoller().getContent())), true, null, null);

                    ManagersTool.getDataControllersManager(_context).getSMSContoller().clear();
                } catch (Exception ex) {
                    LogTracker.trackLog(ex);
                }
            }

            if (ManagersTool.getUserManager(_context).syncSettings.action_service.vkontakte) {
                try {
                    //     handleAction(Constants.ACTION_ALARM_VK, null);
                } catch (Exception ex) {
                    LogTracker.trackLog(ex);
                }
            }
        }
    }

    public void syncCustomData(Enumerators.DataType dataType) {

        if (NetworkManagementTool.isNetworkAvailable(_context)) {
            switch (dataType) {
                case GPS: {
                    if (ManagersTool.getUserManager(_context).syncSettings.action_service.gps) {
                        ManagersTool.getNetworkManager(_context).sendRequest(Constants.SERVER_SYNC_GPS, com.frostweepgames.familyguard.Networking.Enumerators.NetworkRequestType.POST,
                                Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA,
                                        SerializationTool.serializeObject(ManagersTool.getDataControllersManager(_context).getGPSController().getContent())),
                                true, null, null);

                        ArrayList<Object> objects = ManagersTool.getSQLDataManager(_context).getDataFromDB(com.frostweepgames.familyguard.SQLiteSystem.Constants.KEY_TABLE_GPS);

                        ManagersTool.getNetworkManager(_context).sendRequest(Constants.SERVER_SYNC_GPS, com.frostweepgames.familyguard.Networking.Enumerators.NetworkRequestType.POST,
                                Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA,
                                        SerializationTool.serializeObject(objects)), true, null, null);
                    }
                }
                break;
                case CHECK_DEVICE:
                    break;
                case SYSTEM: {
                    ManagersTool.getNetworkManager(_context).sendRequest(Constants.SERVER_HELP_SYSTEM, com.frostweepgames.familyguard.Networking.Enumerators.NetworkRequestType.POST,
                            Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA,
                                    SerializationTool.serializeObject(SystemInfoTool.getSystemInfo(_context))), true, null, null);
                }
                break;
                case GET_SYNC_SETTINGS: {
                    ManagersTool.getUserManager(_context).requestSynchronizationData();
                }
                break;
                default:
                    break;
            }
        } else {
            switch (dataType) {
                case GPS:
                    if (ManagersTool.getUserManager(_context).syncSettings.action_service.gps) {
                        ManagersTool.getSQLDataManager(_context).putDataToDB(com.frostweepgames.familyguard.SQLiteSystem.Constants.KEY_TABLE_GPS,
                                SerializationTool.serializeObject(ManagersTool.getDataControllersManager(_context).getGPSController().getContent()));
                    }
                    break;
                case CHECK_DEVICE:
                    break;
                case SYSTEM:
                    break;
                case GET_SYNC_SETTINGS:
                    break;
                default:
                    break;
            }
        }
    }
}