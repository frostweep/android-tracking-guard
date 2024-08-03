package com.frostweepgames.apptracker.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.core.IController;
import com.frostweepgames.apptracker.models.InstalledAppsModel;

import java.util.ArrayList;
import java.util.List;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class GetInstalledAppsController implements IController {

    private Activity _currentActivity;
    private Context _currentContext;

    private ArrayList<InstalledAppsModel> _installedApps;

    public GetInstalledAppsController(Activity currentActivity) {

        _currentActivity = currentActivity;
        _currentContext = currentActivity.getApplicationContext();
        _installedApps = new ArrayList<InstalledAppsModel>();
    }


    public GetInstalledAppsController(Context context) {

        _currentContext = context;
        _installedApps = new ArrayList<InstalledAppsModel>();
    }

    public Object getContent() {
        getInstalledApps();

        return _installedApps;
    }

    public void getInstalledApps() {
        _installedApps.clear();

        final PackageManager pm = _currentContext.getPackageManager();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        InstalledAppsModel model;
        PackageInfo pInfo;
        for (ApplicationInfo packageInfo : packages) {
            if(Helper.isAppPreLoaded(_currentContext, packageInfo))
                continue;

            model = new InstalledAppsModel();
            model.appname = packageInfo.loadLabel(pm).toString();
            model.pname = packageInfo.packageName;

            try{
                pInfo = pm.getPackageInfo(packageInfo.packageName, 0);
                model.versionCode = Integer.toString(pInfo.versionCode);
                model.versionName = pInfo.versionName;
            }
            catch (Exception e){}

            _installedApps.add(model);
        }
    }
}