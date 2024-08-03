package com.frostweepgames.apptracker.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.frostweepgames.apptracker.syncservice.SyncBackgroundService;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class ServiceManager {

    private Intent _syncServiceIntent;
    private Activity _mainActivity;
    private Context _context;

    public ServiceManager(Activity activity) {
        _mainActivity = activity;
        _context = _mainActivity.getApplicationContext();
    }

    public ServiceManager() {

    }


    public void runSyncInBackground() {
        _syncServiceIntent = new Intent(_context, SyncBackgroundService.class);
        _syncServiceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startService(_syncServiceIntent);
    }

    public void stopSyncInBackground(Intent syncServiceIntent) {
        _syncServiceIntent = syncServiceIntent;

        if (_syncServiceIntent != null)
            _context.stopService(_syncServiceIntent);
    }
}