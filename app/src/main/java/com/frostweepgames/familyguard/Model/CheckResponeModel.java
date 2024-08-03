package com.frostweepgames.familyguard.Model;

public class CheckResponeModel {
    public Boolean result;
    public CheckData data;

    public CheckResponeModel()
    {
        result = false;
        data = null;
    }

    public class CheckData
    {
        public String token;
    }
}
