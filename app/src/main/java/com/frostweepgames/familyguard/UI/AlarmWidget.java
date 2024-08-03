package com.frostweepgames.familyguard.UI;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.frostweepgames.androidhelperlibrary.controllers.LocalGPSController;
import com.frostweepgames.androidhelperlibrary.tools.LogTracker;
import com.frostweepgames.androidhelperlibrary.tools.SerializationTool;
import com.frostweepgames.familyguard.Model.AccountData;
import com.frostweepgames.familyguard.Settings.Enumerators;
import com.frostweepgames.familyguard.Networking.NetworkManagerLocal;
import com.frostweepgames.familyguard.R;
import com.frostweepgames.familyguard.SQLiteSystem.SQLDataManagerLocal;
import com.frostweepgames.familyguard.Settings.Constants;
import com.frostweepgames.familyguard.Tools.Converter;
import com.frostweepgames.familyguard.Tools.ManagersTool;

import java.util.ArrayList;

/**-------------------------------
 * Created by Artem Shyriaiev on 15.09.2018
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class AlarmWidget extends AppWidgetProvider {

    private Context _context;

    public String WIDGET_BUTTON = Constants.APP_BUNDLE_IDENTIFIER + ".alarm_button";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        _context = context;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        _context = context;

        for (int appWidgetID : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.alarm_widget);

            remoteViews.setOnClickPendingIntent(R.id.button_alarm_widget_alarm, getPendingSelfIntent(context, WIDGET_BUTTON));

            appWidgetManager.updateAppWidget(appWidgetID, remoteViews);

            checkHideWidget();
        }
    }

    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                (context);
        ComponentName thisAppWidgetComponentName =
                new ComponentName(context.getPackageName(), getClass().getName()
                );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        _context = context;

        if (WIDGET_BUTTON.equals(intent.getAction())) {
            onUpdate(context);

            AlarmWidgetOnClickHandler();
        }
    }

    public final void AlarmWidgetOnClickHandler() {
        try {
            final NetworkManagerLocal netManager = ManagersTool.getNetworkManager(_context);

            LocalGPSController controller = new LocalGPSController(_context);

            netManager.sendRequest(Constants.SERVER_HELP_ALARM, com.frostweepgames.familyguard.Networking.Enumerators.NetworkRequestType.POST,
                    Converter.getMapWithListFrom(Constants.SERVER_KEY_DATA, SerializationTool.serializeObject(controller.getContent())),
                    true,null, null);
        } catch (Exception ex) {
            LogTracker.trackLog(ex);
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void checkHideWidget() {
        try {
            final SQLDataManagerLocal sqliteManager = ManagersTool.getSQLDataManager(_context);

            ArrayList<SQLDataManagerLocal.SQLiteLineModel> list =
                    sqliteManager.getObjectsFromDB(com.frostweepgames.familyguard.SQLiteSystem.Constants.KEY_TABLE_APP_SETTINGS, null, null, null);
            if (list.size() > 0) {
                AccountData accountData = SerializationTool.deserializeString(list.get(list.size() - 1).Data, AccountData.class);

                if (accountData.packageType != Enumerators.PackageType.CHILDREN_PROTECTION_1 && accountData.packageType != Enumerators.PackageType.CHILDREN_PROTECTION_2) {
                    RemoteViews remoteViews = new RemoteViews(_context.getPackageName(), R.layout.alarm_widget);

                    remoteViews.setViewVisibility(R.id.button_alarm_widget_alarm, View.INVISIBLE);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(_context);
                    ComponentName thisAppWidgetComponentName = new ComponentName(_context.getPackageName(), getClass().getName());
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);

                    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                }
            }
        } catch (Exception ex) {
            LogTracker.trackLog(ex);
        }
    }
}