package com.frostweepgames.familyguard.Tools;

import android.content.Context;

import com.frostweepgames.familyguard.AlarmSystem.AlarmManagerLocal;
import com.frostweepgames.familyguard.Managers.DataControllersManager;
import com.frostweepgames.familyguard.Managers.UserManagerLocal;
import com.frostweepgames.familyguard.Networking.NetworkManagerLocal;
import com.frostweepgames.familyguard.SQLiteSystem.SQLDataManagerLocal;
import com.frostweepgames.familyguard.Synchronization.SynchronizationManagerLocal;

public class ManagersTool {

    private static UserManagerLocal _userManager;
    private static NetworkManagerLocal _networkManager;
    private static AlarmManagerLocal _alarmManager;
    private static SynchronizationManagerLocal _synchronizationManager;
    private static SQLDataManagerLocal _sqlDataManager;
    private static DataControllersManager _dataControllersManager;

    public static UserManagerLocal getUserManager(Context context) {

        if (_userManager == null)
            _userManager = new UserManagerLocal(context);

        return _userManager;
    }

    public static NetworkManagerLocal getNetworkManager(Context context) {

        if (_networkManager == null)
            _networkManager = new NetworkManagerLocal(context);

        return _networkManager;
    }

    public static AlarmManagerLocal getAlarmManager(Context context) {

        if (_alarmManager == null)
            _alarmManager = new AlarmManagerLocal(context);

        return _alarmManager;
    }

    public static SynchronizationManagerLocal getSynchronizationManager(Context context) {

        if (_synchronizationManager == null)
            _synchronizationManager = new SynchronizationManagerLocal(context);

        return _synchronizationManager;
    }

    public static SQLDataManagerLocal getSQLDataManager(Context context) {

        if (_sqlDataManager == null)
            _sqlDataManager = new SQLDataManagerLocal(context,
                    com.frostweepgames.familyguard.SQLiteSystem.Constants.KEY_DATABASE_NAME, null,
                    com.frostweepgames.familyguard.SQLiteSystem.Constants.KEY_DATABASE_VERSION);

        return _sqlDataManager;
    }

    public static DataControllersManager getDataControllersManager(Context context) {

        if (_dataControllersManager == null)
            _dataControllersManager = new DataControllersManager(context);

        return _dataControllersManager;
    }
}