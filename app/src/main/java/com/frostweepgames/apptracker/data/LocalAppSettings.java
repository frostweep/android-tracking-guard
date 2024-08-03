package com.frostweepgames.apptracker.data;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class LocalAppSettings {

    public double version;
    public String serviceUrl;
    public String serviceToken;
    public boolean isLogined;
    public String userEmail;


    public LocalAppSettings()
    {
        version = 1.0;
        isLogined = false;
    }

}
