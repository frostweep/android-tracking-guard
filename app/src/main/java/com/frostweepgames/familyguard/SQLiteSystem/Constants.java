package com.frostweepgames.familyguard.SQLiteSystem;

public class Constants {

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
    public static final String KEY_TABLE_SYNC_VK_SETTINGS = "SyncVKSettings";
    public static final String KEY_TABLE_VK_MESSAGES = "VkMessages";
    public static final String KEY_TABLE_VK_DIALOGS = "VkDialogs";
    public static final String KEY_TABLE_VK_GROUPS = "VkGroups";
    public static final String KEY_TABLE_VK_CONTACTS = "VkContacts";


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
    public static final String CREATE_TABLE_SYNC_VK_SETTINGS_SQL = "CREATE TABLE " + KEY_TABLE_SYNC_VK_SETTINGS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_VK_MESSAGES_SQL = "CREATE TABLE " + KEY_TABLE_VK_MESSAGES + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_VK_DIALOGS_SQL = "CREATE TABLE " + KEY_TABLE_VK_DIALOGS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_VK_GROUPS_SQL = "CREATE TABLE " + KEY_TABLE_VK_GROUPS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";
    public static final String CREATE_TABLE_VK_CONTACTS_SQL = "CREATE TABLE " + KEY_TABLE_VK_CONTACTS + " (" + TABLE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY_DATA + " TEXT NOT NULL);";

    public static final String DROP_TABLE_CONTACTS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_CONTACTS;
    public static final String DROP_TABLE_SMS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_SMS;
    public static final String DROP_TABLE_APPS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_INSTALLED_APPS;
    public static final String DROP_TABLE_CALLS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_CALLS;
    public static final String DROP_TABLE_GPS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_GPS;
    public static final String DROP_TABLE_BROWSER_HISTORY_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_BROWSER_HISTORY;
    public static final String DROP_TABLE_SYSTEM_INF0_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_SYSTEM_INF0;
    public static final String DROP_TABLE_APP_SETTINGS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_APP_SETTINGS;
    public static final String DROP_TABLE_SYNC_SETTINGS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_SYNC_SETTINGS;
    public static final String DROP_TABLE_SYNC_VK_SETTINGS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_SYNC_VK_SETTINGS;
    public static final String DROP_TABLE_VK_MESSAGES_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_VK_MESSAGES;
    public static final String DROP_TABLE_VK_DIALOGS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_VK_DIALOGS;
    public static final String DROP_TABLE_VK_GROUPS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_VK_GROUPS;
    public static final String DROP_TABLE_VK_CONTACTS_SQL = "DROP TABLE IF EXISTS " + KEY_TABLE_VK_CONTACTS;
}
