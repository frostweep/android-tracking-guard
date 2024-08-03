package com.frostweepgames.familyguard.AlarmSystem;

import android.content.Context;

import com.frostweepgames.androidhelperlibrary.tools.LogTracker;
import com.frostweepgames.familyguard.Settings.Constants;
import com.frostweepgames.familyguard.Settings.Enumerators;
import com.frostweepgames.familyguard.Tools.ManagersTool;

/**-------------------------------
 * Created by Artem Shyriaiev on 15.09.2018
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class ActionsHandler {

    public static void HandleAction(Context context, String action, String extra) {
        switch (action) {
            case Constants.ACTION_ALARM_SYNC_AT_NIGHT:
                ManagersTool.getSynchronizationManager(context).syncDataAtNight();
                break;
            case Constants.ACTION_ALARM_SYNC_GPS:
                ManagersTool.getSynchronizationManager(context).syncCustomData(Enumerators.DataType.GPS);
            break;
            case "empty":
            default:
                break;
        }
    }
}