package com.frostweepgames.apptracker.ui;

import android.Manifest;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;

import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.core.IUIPage;
import com.frostweepgames.apptracker.MainActivity;
import com.frostweepgames.apptracker.R;
import com.frostweepgames.apptracker.syncservice.SyncBackgroundService;

import java.util.ArrayList;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class SettingsPage implements IUIPage {

    private MainActivity _mainActivity;
    private ConstraintLayout _selfLayout;

    private Button _alertButton;
    private Button _settingsButton;
    private Button _requestPermissionsButton;
    private Button _checkAndStartServiceButton;
    private Button _appVersionButton;
    private Button _stopServiceButton;
    private Button _loginVKButton;


    private AsyncTask _syncTask;


    private int _clickedCount = 0;

    @Override
    public void Init(Activity activity) {

        _mainActivity = (MainActivity) activity;

        _selfLayout = activity.findViewById(R.id.SettingsPageLayout);

        _alertButton = activity.findViewById(R.id.button_alert);
        _settingsButton = activity.findViewById(R.id.button_settings);
        _requestPermissionsButton = activity.findViewById(R.id.button_request_permissions);
        _checkAndStartServiceButton = activity.findViewById(R.id.button_start_async);
        _appVersionButton = activity.findViewById(R.id.button_version);
        _stopServiceButton = activity.findViewById(R.id.button_stop_async);
        _loginVKButton = activity.findViewById(R.id.button_login_vk);

        _stopServiceButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        StopServiceButtonOnClickHandler(view);
                    }
                }
        );

        _appVersionButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        AppVersionButtonOnClickHandler(view);
                    }
                }
        );

        _alertButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        AlertButtonOnClickHandler(view);
                    }
                }
        );

        _settingsButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        SettingsButtonOnClickHandler(view);
                    }
                }
        );

        _requestPermissionsButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        RequestPermissionsButtonOnClickHandler(view);
                    }
                }
        );

        _checkAndStartServiceButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        CheckAndStartServiceButtonOnClickHandler(view);
                    }
                }
        );

        _loginVKButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        LoginVKButtonOnClickHandler(view);
                    }
                }
        );

        Hide();
    }

    @Override
    public void Show() {
        _selfLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void Hide() {
        _selfLayout.setVisibility(View.INVISIBLE);

        _clickedCount = 0;

        _settingsButton.setVisibility(View.INVISIBLE);
        _requestPermissionsButton.setVisibility(View.INVISIBLE);
        _checkAndStartServiceButton.setVisibility(View.INVISIBLE);
        _stopServiceButton.setVisibility(View.INVISIBLE);
    }

    private void StopServiceButtonOnClickHandler(View view) {

        if(SyncBackgroundService.instance != null) {
            Helper.drawSystemMessageTooltip(_mainActivity, "Сервис остановлен!");
            SyncBackgroundService.instance.onDestroy();
            SyncBackgroundService.instance = null;
        }
    }


    private void AppVersionButtonOnClickHandler(View view) {

        _clickedCount++;

        if (_clickedCount > 5) {
            _settingsButton.setVisibility(View.VISIBLE);
            _requestPermissionsButton.setVisibility(View.VISIBLE);
            _checkAndStartServiceButton.setVisibility(View.VISIBLE);
            _stopServiceButton.setVisibility(View.VISIBLE);
        }
    }

    private void AlertButtonOnClickHandler(View view) {
        MainActivity.networkManager.sendPOSTRequest(Enumerators.APINetRequestType.SEND_ALARM, Enumerators.NetworkRequestType.POST, Helper.getDataFromController(_mainActivity.getApplicationContext(), Enumerators.ControllerType.GPS, false, false));
    }

    private void SettingsButtonOnClickHandler(View view) {
        MainActivity.uiManager.showPage(Enumerators.UIPageType.MAIN_PAGE);
    }


    private void CheckAndStartServiceButtonOnClickHandler(View view) {

        boolean isRunning;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            isRunning = SyncBackgroundService.instance != null;
        else
            isRunning = Helper.isServiceRunning(SyncBackgroundService.class, _mainActivity.getApplicationContext());


        if (!isRunning) {
            MainActivity.serviceManager.runSyncInBackground();
            Helper.drawSystemMessageTooltip(_mainActivity, "Сервис запущен!");
        }
    }

    private void RequestPermissionsButtonOnClickHandler(View view) {
        _syncTask = new RequestPermissionsAsync().execute();
    }

    private void LoginVKButtonOnClickHandler(View view) {

        if (!MainActivity.vkManager.isLoggedIn)
            MainActivity.vkManager.login();
    }

    public class RequestPermissionsAsync extends AsyncTask<Void, Void, Void> {

        public RequestPermissionsAsync() {

        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<String> permissions = new ArrayList<>();
            permissions.add(Manifest.permission.KILL_BACKGROUND_PROCESSES);
            permissions.add(Manifest.permission.READ_CONTACTS);
            permissions.add(Manifest.permission.READ_SMS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                permissions.add(Manifest.permission.READ_CALL_LOG);
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(Manifest.permission.INTERNET);
            permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
            permissions.add(Manifest.permission.WRITE_SETTINGS);
            permissions.add(Manifest.permission.CHANGE_NETWORK_STATE);
            permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            permissions.add(Manifest.permission.READ_PHONE_STATE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            for (String permission : permissions) {

                while (!MainActivity.IsReadyToNextPermission) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (!Helper.mayRequestPermission(_mainActivity, permission, Constants.REQUEST_PERMISSION_VALUE))
                    MainActivity.IsReadyToNextPermission = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Helper.drawSystemMessageTooltip(_mainActivity.getApplicationContext(), "PERMISSIONS GRANTED");
            _syncTask = null;
        }
    }

}