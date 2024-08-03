package com.frostweepgames.apptracker.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.CpuUsageInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.core.IController;
import com.frostweepgames.apptracker.models.MobileInfoModel;
import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * -------------------------------
 * Created by artem on 25.03.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 * ---------------------------------
 */

public class MobileInfoController implements IController {

    private Activity _currentActivity;
    private Context _currentContext;
    private MobileInfoModel _mobileInfo;

    public MobileInfoController(Activity selfActivity) {
        _currentActivity = selfActivity;
        _mobileInfo = new MobileInfoModel();
    }

    public MobileInfoController(Context context) {

        _currentContext = context;
        _mobileInfo = new MobileInfoModel();
    }


    public Object getContent() {
        getMobileInfo();

        return _mobileInfo;
    }


    @SuppressLint("MissingPermission")
    private void getMobileInfo() {
        if (_currentActivity != null) {
            if (!Helper.mayRequestPermission(_currentActivity, READ_PHONE_STATE, Constants.REQUEST_PERMISSION_VALUE)) {
                return;
            }
        }

        TelephonyManager telephonyManager = (TelephonyManager) _currentActivity.getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                _mobileInfo.phoneSerial = Build.getSerial();
                _mobileInfo.radioVersion = Build.getRadioVersion();
            } else {
                _mobileInfo.phoneSerial = Build.SERIAL;
                _mobileInfo.radioVersion = Build.RADIO;
                _mobileInfo.imei = telephonyManager.getDeviceId();
            }

            _mobileInfo.phoneNumber = "";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                List<SubscriptionInfo> subscription = SubscriptionManager.from(_currentActivity.getApplicationContext()).getActiveSubscriptionInfoList();
                String number;
                for (int i = 0; i < subscription.size(); i++) {
                    SubscriptionInfo info = subscription.get(i);
                    number = info.getNumber();

                    if(number == null)
                        number = "unknown";

                    _mobileInfo.phoneNumber += number + ";";
                }
            } else
                _mobileInfo.phoneNumber = telephonyManager.getLine1Number();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                _mobileInfo.imei = "";

                for (int i = 0; i < telephonyManager.getPhoneCount(); i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        _mobileInfo.imei += telephonyManager.getImei(i) + ";";
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            _mobileInfo.imei += telephonyManager.getDeviceId(i) + ";";
                    }
                }

                switch (telephonyManager.getPhoneType()) {
                    case TelephonyManager.PHONE_TYPE_CDMA:
                        _mobileInfo.phoneType = Enumerators.PhoneType.CDMA;
                        break;
                    case TelephonyManager.PHONE_TYPE_GSM:
                        _mobileInfo.phoneType = Enumerators.PhoneType.GSM;
                        break;
                    case TelephonyManager.PHONE_TYPE_SIP:
                        _mobileInfo.phoneType = Enumerators.PhoneType.SIP;
                        break;
                    case TelephonyManager.PHONE_TYPE_NONE:
                        _mobileInfo.phoneType = Enumerators.PhoneType.NONE;
                        break;
                    default:
                        _mobileInfo.phoneType = Enumerators.PhoneType.UNKNOWN;
                        break;

                }

            }
        }

        _mobileInfo.phoneModel = Build.MODEL;
        _mobileInfo.brand = Build.BRAND;
        _mobileInfo.displayId = Build.DISPLAY;
        _mobileInfo.hardware = Build.HARDWARE;
        _mobileInfo.user = Build.USER;
        _mobileInfo.fingerprint = Build.FINGERPRINT;
        _mobileInfo.bootloader = Build.BOOTLOADER;
        _mobileInfo.board = Build.BOARD;

        _mobileInfo.cpuInfo = parseProcCpuinfo();

        _mobileInfo.cpuUsage = new ArrayList<MobileInfoModel.CPUUsageInfo>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            MobileInfoModel.CPUUsageInfo entry;
            for (CpuUsageInfo cpuUsage : CpuUsageInfo.CREATOR.newArray(getNumberOfCores())) {
                if (cpuUsage != null) {
                    entry = new MobileInfoModel.CPUUsageInfo();
                    entry.active = cpuUsage.getActive();
                    entry.total = cpuUsage.getTotal();
                    _mobileInfo.cpuUsage.add(entry);
                }
            }
        }
    }


    public HashMap<String, ProcCpuInfoProperty> parseProcCpuinfo() {
        String procMemInfo = Constants.CPU_INFO_FILE;

        HashMap<String, ProcCpuInfoProperty> procCpuinfoProperties = new HashMap<String, ProcCpuInfoProperty>();
        ProcCpuInfoProperty property;

        String temp;
        int readBlockSize = 8192;

        try {
            FileReader fileReader = new FileReader(procMemInfo);
            BufferedReader bufferedReader = new BufferedReader(fileReader, readBlockSize);

            while ((temp = bufferedReader.readLine()) != null) {

                property = parseProcCpuinfoLine(temp);
                if (property != null) {
                    procCpuinfoProperties.put(property.name, property);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return procCpuinfoProperties;
    }

    private ProcCpuInfoProperty parseProcCpuinfoLine(String line) {
        String[] parts = line.split(":");
        if (parts.length == 2 && parts[0] != null
                && parts[1] != null) {
            return new ProcCpuInfoProperty(parts[0].trim(), parts[1].trim());
        }

        return null;
    }

    public static class ProcCpuInfoProperty {
        public String name;
        public String value;

        public ProcCpuInfoProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }


    private int getNumberOfCores() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Runtime.getRuntime().availableProcessors();
        } else {
            return getNumCoresOldPhones();
        }
    }

    private int getNumCoresOldPhones() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            File dir = new File(Constants.CPU_CORES_COUNT_INFO_FILE);
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }
}