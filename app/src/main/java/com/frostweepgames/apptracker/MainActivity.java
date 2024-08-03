package com.frostweepgames.apptracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.frostweepgames.apptracker.managers.SQLiteManager;
import com.frostweepgames.apptracker.managers.ServiceManager;
import com.frostweepgames.apptracker.managers.SettingsManager;
import com.frostweepgames.apptracker.managers.SystemControllersManager;
import com.frostweepgames.apptracker.managers.UIManager;
import com.frostweepgames.apptracker.managers.NetworkManager;
import com.frostweepgames.apptracker.managers.VKManager;
import com.frostweepgames.apptracker.receivers.ExceptionsHandler;
import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class MainActivity extends AppCompatActivity {

    public static SystemControllersManager systemControllersManager;
    public static UIManager uiManager;
    public static SQLiteManager sqliteManager;
    public static NetworkManager networkManager;
    public static SettingsManager settingsManager;
    public static ServiceManager serviceManager;
    public static VKManager vkManager;

    private static Activity _currentActivity;
    private static Context _currentContext;


    public static boolean IsReadyToNextPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public static Activity SelfActivity() {
        return _currentActivity;
    }


    private void init() {
         _currentActivity = this;
        _currentContext = getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionsHandler(_currentContext));

        systemControllersManager = new SystemControllersManager(_currentActivity);
        uiManager = new UIManager(_currentActivity);
        sqliteManager = new SQLiteManager(_currentContext, Constants.KEY_DATABASE_NAME, null, Constants.KEY_DATABASE_VERSION);
        networkManager = new NetworkManager(_currentActivity);
        settingsManager = new SettingsManager(_currentActivity);
        serviceManager = new ServiceManager(_currentActivity);
        vkManager = new VKManager();

        vkManager.init(this);

        settingsManager.loadSettings(_currentContext, false);

        if (settingsManager.localAppSettings.isLogined)
            uiManager.showPage(Enumerators.UIPageType.SETTINGS_PAGE);
        else
            uiManager.showPage(Enumerators.UIPageType.MAIN_PAGE);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_PERMISSION_VALUE) {
            for (int i : grantResults) {
                IsReadyToNextPermission = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                vkManager.isLoggedIn = true;
            }
            @Override
            public void onError(VKError error) {
                Helper.drawSystemMessageTooltip(_currentContext, "Ошибка авторизации!");
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}