package com.frostweepgames.apptracker.models;

import com.frostweepgames.apptracker.settings.Enumerators;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class SMSModel {

    public String destination;
    public String message;
    public Enumerators.SMSActionType type;
    public Long tm;
}
