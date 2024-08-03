package com.frostweepgames.apptracker.data;

import java.util.HashMap;
import java.util.Map;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class PeriodWithdrawal {
    public String sms;
    public String gps;
    public String call;
    public String contacts;
    public String app;
    public String browser;
    public String system;

    public PeriodWithdrawal()
    {
        app = "10";
        browser = "10";
        system = "10";
    }
}