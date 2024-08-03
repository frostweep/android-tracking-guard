package com.frostweepgames.apptracker.data;

/**
 * -------------------------------
 * Created by artem on 13.03.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 * ---------------------------------
 */

public class StopNet {

    public GPRSConfig gprs;
    public WiFiConfig wifi;


    public StopNet()
    {
        gprs = new GPRSConfig();
        wifi = new WiFiConfig();
    }
}
