package com.frostweepgames.familyguard.Settings;

public class Enumerators {

    public enum LogType {
        STACK_TRACE,
        MESSAGE_BOX,
        NET_REPORT
    }

    public enum PackageType
    {
        NONE,

        DEMO,

        PHONE_GUARD_LIGHT,
        PHONE_GUARD_FULL,
        CHILDREN_PROTECTION_1,
        CHILDREN_PROTECTION_2,
        TEEN_SAFE_1,
        TEEN_SAFE_2,
        TEEN_SAFE_3,

        FULL_FUNCTIONALITY
    }

    public enum DataType
    {
        GPS,
        CALLS,
        CONTACTS,
        SMS,
        INSTALLED_APPS,
        BROWSER_HISTORY,

        SYSTEM,
        CHECK_DEVICE,
        GET_SYNC_SETTINGS
    }
}