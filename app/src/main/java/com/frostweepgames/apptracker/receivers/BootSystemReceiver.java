package com.frostweepgames.apptracker.receivers;

import android.content.BroadcastReceiver;
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

public class BootSystemReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent _syncServiceIntent = new Intent(context, SyncBackgroundService.class);
        context.startService(_syncServiceIntent);
    }
}