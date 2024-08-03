package com.frostweepgames.apptracker.models;

import com.frostweepgames.apptracker.controllers.MobileInfoController;
import com.frostweepgames.apptracker.settings.Enumerators;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * -------------------------------
 * Created by artem on 25.03.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 * ---------------------------------
 */

public class MobileInfoModel {

    public String imei;
    public String radioVersion;
    public String phoneNumber;
    public Enumerators.PhoneType phoneType;
    public String phoneModel;
    public String brand;
    public String displayId;
    public String phoneSerial;
    public String hardware;
    public String user;
    public String fingerprint;
    public String bootloader;
    public String board;

    public ArrayList<CPUUsageInfo> cpuUsage;

    public HashMap<String, MobileInfoController.ProcCpuInfoProperty> cpuInfo;

    public static class CPUUsageInfo
    {
        public Long active;
        public Long total;
    }
}
