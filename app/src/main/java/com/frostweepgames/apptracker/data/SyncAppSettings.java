package com.frostweepgames.apptracker.data;

import java.util.ArrayList;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class SyncAppSettings {

    public DataTransfer data_transfer;
    public PeriodTransfer period_transfer;
    public PeriodWithdrawal period_withdrawal;
    public ActionService action_service;

    public StopNet stop_net;
    public ArrayList<StopApp> stop_app;

    public SyncAppSettings()
    {
        data_transfer = new DataTransfer();
        period_withdrawal = new PeriodWithdrawal();
        period_transfer = new PeriodTransfer();
        action_service = new ActionService();
        stop_net = new StopNet();
        stop_app = new ArrayList<StopApp>();
    }
}
