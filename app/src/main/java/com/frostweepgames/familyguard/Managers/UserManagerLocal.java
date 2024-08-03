package com.frostweepgames.familyguard.Managers;

import android.content.Context;

import com.frostweepgames.androidhelperlibrary.tools.LogTracker;
import com.frostweepgames.androidhelperlibrary.tools.SerializationTool;
import com.frostweepgames.familyguard.Model.AccountData;
import com.frostweepgames.familyguard.Model.SyncSettingsModel;
import com.frostweepgames.familyguard.Model.SyncSettingsResponseModel;
import com.frostweepgames.familyguard.Networking.NetworkManagerLocal;
import com.frostweepgames.familyguard.SQLiteSystem.Constants;
import com.frostweepgames.familyguard.SQLiteSystem.SQLDataManagerLocal;
import com.frostweepgames.familyguard.Settings.Enumerators;
import com.frostweepgames.familyguard.Tools.Converter;
import com.frostweepgames.familyguard.Tools.ManagersTool;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManagerLocal {

    private Context _context;

    public AccountData accountData;
    public SyncSettingsModel syncSettings;

    public UserManagerLocal(Context context) {
        _context = context;

        accountData = new AccountData();
        syncSettings = new SyncSettingsModel();

        loadAccountData();
        loadSynchronizationData();
    }

    public void loadAccountData() {
        ArrayList<SQLDataManagerLocal.SQLiteLineModel> list = ManagersTool.getSQLDataManager(_context).getObjectsFromDB(Constants.KEY_TABLE_APP_SETTINGS, null, null, null);
        if (list.size() > 0)
            accountData = SerializationTool.deserializeString(list.get(list.size() - 1).Data, AccountData.class);
    }

    public void saveAccountData() {
        SQLDataManagerLocal sqliteManager = ManagersTool.getSQLDataManager(_context);
        sqliteManager.removeElementInDB(Constants.KEY_TABLE_APP_SETTINGS, 0, true);
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.TABLE_KEY_DATA, SerializationTool.serializeObject(accountData));
        sqliteManager.addElementInDB(Constants.KEY_TABLE_APP_SETTINGS, Converter.prepareContentValuesFromList(map));
    }

    public void requestSynchronizationData() {
        ManagersTool.getNetworkManager(_context).sendRequest(com.frostweepgames.familyguard.Settings.Constants.SERVER_HELP_SETTINGS,
                com.frostweepgames.familyguard.Networking.Enumerators.NetworkRequestType.GET, new HashMap<String, Object>(), true, (result) ->
                {
                    LogTracker.trackLog(new Exception(result));

                    if (!NetworkManagerLocal.isResultIsOkay(result)) {
                        try {
                            //  FailedResponse response = SerializationTool.deserializeString(result, FailedResponse.class);
                            // Helper.sendLogReport(_currentContext, new LogModel(Constants.KEY_LOG_NETWORK_RESPONSE, response.data), Enumerators.LogType.NET_REPORT);
                        } catch (Exception ex) {
                            LogTracker.trackLog(ex);
                        }
                        return;
                    }

                    SyncSettingsResponseModel response = SerializationTool.deserializeString(result, SyncSettingsResponseModel.class);

                    syncSettings = response.data;

                    if(syncSettings == null)
                        syncSettings = new SyncSettingsModel();

                    if (syncSettings.appPackage == null)
                        syncSettings.appPackage = Enumerators.PackageType.NONE.toString();

                    accountData.packageType = Enum.valueOf(Enumerators.PackageType.class, syncSettings.appPackage);
                    saveAccountData();
                    saveSynchronizationData();
                }, null);
    }

    public void loadSynchronizationData() {
        ArrayList<SQLDataManagerLocal.SQLiteLineModel> list = ManagersTool.getSQLDataManager(_context).getObjectsFromDB(Constants.KEY_TABLE_SYNC_SETTINGS, null, null, null);
        if (list.size() > 0)
            syncSettings = SerializationTool.deserializeString(list.get(list.size() - 1).Data, SyncSettingsModel.class);
    }

    public void saveSynchronizationData() {
        SQLDataManagerLocal sqliteManager = ManagersTool.getSQLDataManager(_context);
        sqliteManager.removeElementInDB(Constants.KEY_TABLE_SYNC_SETTINGS, 0, true);
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.TABLE_KEY_DATA, SerializationTool.serializeObject(syncSettings));
        sqliteManager.addElementInDB(Constants.KEY_TABLE_SYNC_SETTINGS, Converter.prepareContentValuesFromList(map));
    }
}