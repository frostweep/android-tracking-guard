package com.frostweepgames.apptracker.managers;

import android.app.Activity;
import android.content.Context;

import com.frostweepgames.apptracker.controllers.MobileInfoController;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.frostweepgames.apptracker.controllers.GetBrowsersHistoryController;
import com.frostweepgames.apptracker.controllers.GetCallsController;
import com.frostweepgames.apptracker.controllers.GetContactsController;
import com.frostweepgames.apptracker.controllers.GetGPSController;
import com.frostweepgames.apptracker.controllers.GetInstalledAppsController;
import com.frostweepgames.apptracker.controllers.GetSMSContoller;
import com.frostweepgames.apptracker.core.IController;

import java.util.HashMap;
import java.util.Map;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class SystemControllersManager {

    private Activity _currentActivity;
    private Map<Enumerators.ControllerType, IController> _controllers;

    public SystemControllersManager(Activity currentActivity) {
        _currentActivity = currentActivity;
        init();
    }

    public SystemControllersManager(Context context) {
        initWithContext(context);
    }

    public void init() {
        _controllers = new HashMap<>();

        _controllers.put(Enumerators.ControllerType.CONTACTS, new GetContactsController(_currentActivity));
        _controllers.put(Enumerators.ControllerType.SMS, new GetSMSContoller(_currentActivity));
        _controllers.put(Enumerators.ControllerType.CALLS, new GetCallsController(_currentActivity));
        _controllers.put(Enumerators.ControllerType.INSTALLED_APPS, new GetInstalledAppsController(_currentActivity));
        _controllers.put(Enumerators.ControllerType.GPS, new GetGPSController(_currentActivity));
        _controllers.put(Enumerators.ControllerType.BROWSER_HISTORY, new GetBrowsersHistoryController(_currentActivity));
        _controllers.put(Enumerators.ControllerType.MOBILE_INFO, new MobileInfoController(_currentActivity));
    }


    public void initWithContext(Context context) {
        _controllers = new HashMap<>();

        _controllers.put(Enumerators.ControllerType.CONTACTS, new GetContactsController(context));
        _controllers.put(Enumerators.ControllerType.SMS, new GetSMSContoller(context));
        _controllers.put(Enumerators.ControllerType.CALLS, new GetCallsController(context));
        _controllers.put(Enumerators.ControllerType.INSTALLED_APPS, new GetInstalledAppsController(context));
        _controllers.put(Enumerators.ControllerType.GPS, new GetGPSController(context));
        _controllers.put(Enumerators.ControllerType.BROWSER_HISTORY, new GetBrowsersHistoryController(context));
        _controllers.put(Enumerators.ControllerType.MOBILE_INFO, new MobileInfoController(context));

    }

    public IController GetController(Enumerators.ControllerType type) {
        return _controllers.get(type);
    }
}