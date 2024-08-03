package com.frostweepgames.familyguard.Model;

public class LogModel {

    public String stackTrace = "";
    public String message = "";
    public String phoneSerial = "";
    public String tag = "";
    public String brand = "";
    public String device = "";
    public String model = "";
    public String id = "";
    public String product = "";
    public String sdk = "";
    public String release = "";
    public String incremental = "";

    public LogModel(){}

    public LogModel(String tag, String message)
    {
        this.message = message;
        this.tag = tag;
    }
}