package com.frostweepgames.familyguard.Managers;

import android.content.Context;

import com.frostweepgames.androidhelperlibrary.controllers.LocalBrowsersHistoryController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalCallsController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalContactsController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalDeviceInfoController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalGPSController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalInstalledAppsController;
import com.frostweepgames.androidhelperlibrary.controllers.LocalSMSContoller;

public class DataControllersManager {

    private LocalGPSController _gpsController;
    private LocalSMSContoller _smsController;
    private LocalBrowsersHistoryController _browserHistoryController;
    private LocalCallsController _callsController;
    private LocalContactsController _contactsController;
    private LocalInstalledAppsController _intalledAppsController;
    private LocalDeviceInfoController _deviceInfoController;

    private Context _context;

    public DataControllersManager(Context context) {
        _context = context;
    }

    public LocalGPSController getGPSController() {

        if (_gpsController == null)
            _gpsController = new LocalGPSController(_context);

        return _gpsController;
    }

    public LocalSMSContoller getSMSContoller() {

        if (_smsController == null)
            _smsController = new LocalSMSContoller(_context);

        return _smsController;
    }

    public LocalBrowsersHistoryController getBrowsersHistoryController() {

        if (_browserHistoryController == null)
            _browserHistoryController = new LocalBrowsersHistoryController(_context);

        return _browserHistoryController;
    }

    public LocalCallsController getCallsController() {

        if (_callsController == null)
            _callsController = new LocalCallsController(_context);

        return _callsController;
    }

    public LocalContactsController getContactsController() {

        if (_contactsController == null)
            _contactsController = new LocalContactsController(_context);

        return _contactsController;
    }

    public LocalInstalledAppsController getInstalledAppsController() {

        if (_intalledAppsController == null)
            _intalledAppsController = new LocalInstalledAppsController(_context);

        return _intalledAppsController;
    }

    public LocalDeviceInfoController getDeviceInfoController() {

        if (_deviceInfoController == null)
            _deviceInfoController = new LocalDeviceInfoController(_context);

        return _deviceInfoController;
    }
}