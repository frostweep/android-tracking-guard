package com.frostweepgames.familyguard.Model;

import com.frostweepgames.familyguard.Settings.Enumerators;

public class AccountData {

    public String token;
    public String email;
    public Enumerators.PackageType packageType;

    public AccountData() {
        packageType = Enumerators.PackageType.DEMO;
        token = "";
        email = "";
    }
}