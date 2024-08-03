package com.frostweepgames.familyguard;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.frostweepgames.androidhelperlibrary.controllers.LocalDeviceInfoController;
import com.frostweepgames.androidhelperlibrary.data.MobileInfoModel;
import com.frostweepgames.androidhelperlibrary.settings.Constants;
import com.frostweepgames.androidhelperlibrary.tools.GeneralTools;
import com.frostweepgames.androidhelperlibrary.tools.LogTracker;
import com.frostweepgames.androidhelperlibrary.tools.PermissionsTool;
import com.frostweepgames.androidhelperlibrary.tools.SerializationTool;
import com.frostweepgames.familyguard.Managers.UserManagerLocal;
import com.frostweepgames.familyguard.Model.AccountData;
import com.frostweepgames.familyguard.Model.CheckResponeModel;
import com.frostweepgames.familyguard.Networking.NetworkManagerLocal;
import com.frostweepgames.familyguard.Tools.ExceptionsHandler;
import com.frostweepgames.familyguard.Tools.ManagersTool;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context _context;
    private MainActivity _mainActivity;

    private EditText _userEmailText;
    private Button _saveButton;

    private AsyncTask _syncTask;

    public static boolean IsReadyToNextPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        _saveButton = findViewById(R.id.button_save);
        _userEmailText = findViewById(R.id.input_email);

        _saveButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        SaveButtonOnClickHandler(view);
                    }
                }
        );


        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        TextView textView = findViewById(R.id.textView);
        textView.setText(_userEmailText.getText());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_start_sync) {
            ManagersTool.getSynchronizationManager(_context).setupSynchronization();
        } else if (id == R.id.nav_stop_sync) {
            ManagersTool.getSynchronizationManager(_context).cancelSynchronization();
        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_logout) {
            ManagersTool.getUserManager(_context).accountData = new AccountData();
            ManagersTool.getUserManager(_context).saveAccountData();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_sync_night) {
            ManagersTool.getSynchronizationManager(_context).syncDataAtNight();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void init() {

        _context = getApplicationContext();
        _mainActivity = this;

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionsHandler(_context));


        ManagersTool.getUserManager(_context);

        _userEmailText.setText(ManagersTool.getUserManager(_context).accountData.email);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_PERMISSION_VALUE) {
            for (int i : grantResults) {
                IsReadyToNextPermission = true;
            }
        }
    }


    private void SaveButtonOnClickHandler(View view) {

        _syncTask = new RequestPermissionsAsync().execute();
    }

    private void logIn() {
        MobileInfoModel mobileInfo = (MobileInfoModel) new LocalDeviceInfoController(_context).getContent();

        UserManagerLocal userManager = ManagersTool.getUserManager(_context);

        userManager.accountData.email = _userEmailText.getText().toString();

        HashMap<String, Object> map = new HashMap<>();
        map.put(com.frostweepgames.familyguard.Settings.Constants.SERVER_KEY_EMAIL, userManager.accountData.email);
        map.put(com.frostweepgames.familyguard.Settings.Constants.SERVER_KEY_DATA, mobileInfo);

        ManagersTool.getNetworkManager(_context).sendRequest(com.frostweepgames.familyguard.Settings.Constants.SERVER_HELP_CHECK,
                com.frostweepgames.familyguard.Networking.Enumerators.NetworkRequestType.POST, map, false,
                (result) ->
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

                    CheckResponeModel response = SerializationTool.deserializeString(result, CheckResponeModel.class);

                    userManager.accountData.token = response.data.token;
                    userManager.saveAccountData();
                }, null);
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

                while (!IsReadyToNextPermission) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (!PermissionsTool.mayRequestPermission(_mainActivity, permission, Constants.REQUEST_PERMISSION_VALUE))
                    IsReadyToNextPermission = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            GeneralTools.drawSystemMessageTooltip(_mainActivity.getApplicationContext(), "ДОСТУПЫ ПОЛУЧЕНЫ");

            logIn();

            _syncTask = null;
        }
    }
}