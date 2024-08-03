package com.frostweepgames.familyguard.Tools;

import android.content.Context;
import android.os.Build;

import com.frostweepgames.familyguard.Model.LogModel;
import com.frostweepgames.familyguard.Networking.NetworkManagerLocal;
import com.frostweepgames.familyguard.Settings.Constants;
import com.frostweepgames.familyguard.Settings.Enumerators;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionsHandler  implements java.lang.Thread.UncaughtExceptionHandler {

    private NetworkManagerLocal _networkManagerLocal;

    public ExceptionsHandler(Context context) {
        _networkManagerLocal = new NetworkManagerLocal(context);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        LogModel log = new LogModel();
        log.stackTrace = stackTrace.toString();
        log.message = exception.getMessage();
        log.brand = Build.BRAND;
        log.device = Build.DEVICE;
        log.model = Build.MODEL;
        log.id = Build.ID;
        log.phoneSerial = Build.SERIAL;
        log.product = Build.PRODUCT;
        log.sdk = Build.VERSION.SDK;
        log.release = Build.VERSION.RELEASE;
        log.incremental = Build.VERSION.INCREMENTAL;
        log.tag = Constants.KEY_LOG_APP_ERROR;

        _networkManagerLocal.sendLogReport(log, Enumerators.LogType.NET_REPORT);
    }
}