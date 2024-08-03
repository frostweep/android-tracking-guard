package com.frostweepgames.apptracker.managers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.data.SyncDataTimes;
import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.frostweepgames.apptracker.data.LocalAppSettings;
import com.frostweepgames.apptracker.MainActivity;
import com.frostweepgames.apptracker.data.SyncAppSettings;
import com.frostweepgames.apptracker.syncservice.SyncBackgroundService;

import java.util.ArrayList;
import java.util.HashMap;


/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class SettingsManager {

    private MainActivity _mainActivity;

    public LocalAppSettings localAppSettings;
    public SyncAppSettings syncAppSettings;
    public SyncDataTimes syncDataTimes;

    public SettingsManager(Activity activity) {

        _mainActivity = (MainActivity) activity;
        localAppSettings = new LocalAppSettings();
        syncAppSettings = new SyncAppSettings();
        syncDataTimes = new SyncDataTimes();
    }

    public SettingsManager() {
        localAppSettings = new LocalAppSettings();
        syncAppSettings = new SyncAppSettings();
        syncDataTimes = new SyncDataTimes();
    }

    public void saveSettings() {
        MainActivity.sqliteManager.removeElementInDB(Constants.KEY_TABLE_APP_SETTINGS, 0, true);
        MainActivity.sqliteManager.removeElementInDB(Constants.KEY_TABLE_SYNC_SETTINGS, 0, true);

        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.TABLE_KEY_DATA, Helper.serializeObject(localAppSettings));
        MainActivity.sqliteManager.addElementInDB(Constants.KEY_TABLE_APP_SETTINGS, Helper.prepareContentValuesFromList(map));

        map.clear();
        map.put(Constants.TABLE_KEY_DATA, Helper.serializeObject(syncAppSettings));
        MainActivity.sqliteManager.addElementInDB(Constants.KEY_TABLE_SYNC_SETTINGS, Helper.prepareContentValuesFromList(map));
    }

    public void loadSettings(Context context, boolean useService) {

        SQLiteManager sqliteManager;

        if (useService)
            sqliteManager = SyncBackgroundService.sqliteManager;
        else
            sqliteManager = MainActivity.sqliteManager;

        ArrayList<SQLiteManager.SQLiteLineModel> list = sqliteManager.getObjectsFromDB(Constants.KEY_TABLE_APP_SETTINGS, null, null, null);
        if (list.size() > 0)
            localAppSettings = Helper.deserializeString(list.get(list.size() - 1).Data, LocalAppSettings.class);

        list = sqliteManager.getObjectsFromDB(Constants.KEY_TABLE_SYNC_SETTINGS, null, null, null);
        if (list.size() > 0)
            syncAppSettings = Helper.deserializeString(list.get(list.size() - 1).Data, SyncAppSettings.class);
        ;
    }

    public void loadSyncSettings(boolean useService) {
        if (!useService)
            MainActivity.networkManager.sendPOSTRequest(Enumerators.APINetRequestType.GET_SETTINGS, Enumerators.NetworkRequestType.GET, new HashMap<String, Object>());
        else
            SyncBackgroundService.networkManager.sendPOSTRequest(Enumerators.APINetRequestType.GET_SETTINGS, Enumerators.NetworkRequestType.GET, new HashMap<String, Object>());

    }

}