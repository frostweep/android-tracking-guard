package com.frostweepgames.familyguard.AlarmSystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.frostweepgames.familyguard.Settings.Constants;

/**-------------------------------
 * Created by Artem Shyriaiev on 15.09.2018
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class ActionsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(Constants.KEY_EXTRA)) {
            String action = "empty";

            if(intent.getAction() != null)
                action = intent.getAction();

            ActionsHandler.HandleAction(context, action, intent.getStringExtra(Constants.KEY_EXTRA));
        }
    }
}