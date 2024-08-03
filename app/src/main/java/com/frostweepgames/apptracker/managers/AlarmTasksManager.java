package com.frostweepgames.apptracker.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class AlarmTasksManager {

    private AlarmManager _alarmManager;
    private Context _context;

    public AlarmTasksManager(Context context) {
        _context = context;

        init();
    }

    private void init()
    {
        _alarmManager = (AlarmManager)_context.getSystemService(Context.ALARM_SERVICE);
    }

}
