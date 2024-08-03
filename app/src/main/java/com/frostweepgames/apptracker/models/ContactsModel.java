package com.frostweepgames.apptracker.models;


import java.util.ArrayList;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class ContactsModel {

    public String id;
    public String name;
    public ArrayList<PhoneNumber> phone;
    public String company;
    public String email;
    public String note;

    public static class PhoneNumber
    {
        public String phone;
    }
}
