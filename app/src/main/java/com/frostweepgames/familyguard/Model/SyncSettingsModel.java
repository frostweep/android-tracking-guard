package com.frostweepgames.familyguard.Model;

import java.util.ArrayList;
import java.util.List;

public class SyncSettingsModel {

    public DataTransfer data_transfer;
    public PeriodTransfer period_transfer;
    public PeriodWithdrawal period_withdrawal;
    public ActionService action_service;

    public StopNet stop_net;
    public ArrayList<StopApp> stop_app;
    public String appPackage;

    public boolean isReset;

    public SyncSettingsModel() {
        data_transfer = new DataTransfer();
        period_withdrawal = new PeriodWithdrawal();
        period_transfer = new PeriodTransfer();
        action_service = new ActionService();
        stop_net = new StopNet();
        stop_app = new ArrayList<StopApp>();

        isReset = false;
    }

    public class DataTransfer {

        public List<String> wifi = new ArrayList<String>();
        public List<String> gprs = new ArrayList<String>();
    }

    public class PeriodTransfer {
        public int sms;
        public int gps;
        public int call;
        public int contacts;
        public int app;
        public int browser;
        public int system;
        public int sync;
        public int setting;
        public String vk;

        public PeriodTransfer() {
            app = 10;
            browser = 10;
            system = 10;
            sync = 120;
            setting = 30;
            vk = "03:00";
        }
    }

    public class PeriodWithdrawal {
        public int sms;
        public int gps;
        public int call;
        public int contacts;
        public int app;
        public int browser;
        public int system;

        public PeriodWithdrawal() {
            app = 10;
            browser = 10;
            system = 10;
        }
    }

    public class ActionService {

        public Boolean sms;
        public Boolean gps;
        public Boolean call;
        public Boolean contacts;
        public Boolean app;
        public Boolean browser;
        public Boolean system;
        public Boolean geofencing;
        public Boolean photo;
        public Boolean video;
        public Boolean record;
        public Boolean hw;
        public Boolean camera;
        public Boolean vkontakte;
        public Boolean viber;
        public Boolean telegram;
        public Boolean skype;
        public Boolean facebook;
        public Boolean setting;

        public ActionService() {

            sms = false;
            gps = false;
            call = false;
            contacts = false;
            app = false;
            browser = false;
            system = false;
            setting = false;
        }
    }

    public class StopApp {
        public String app;
        public String start;
        public String end;
    }

    public class StopNet {

        public TimeConfig gprs;
        public TimeConfig wifi;


        public StopNet() {
            gprs = new TimeConfig();
            wifi = new TimeConfig();
        }

        public class TimeConfig {
            public String start;
            public String stop;

            public TimeConfig() {
                start = "12:00";
                stop = "12:00";
            }
        }


    }
}
