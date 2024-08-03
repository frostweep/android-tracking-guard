package com.frostweepgames.apptracker.controllers;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.core.IController;
import com.frostweepgames.apptracker.models.SMSModel;

import java.util.ArrayList;

import static android.Manifest.permission.READ_SMS;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class GetSMSContoller implements IController {

    private Activity _currentActivity;
    private Context _currentContext;

    private ArrayList<SMSModel> _smsList;


    public GetSMSContoller(Activity currentActivity) {

        _currentActivity = currentActivity;
        _currentContext = currentActivity.getApplicationContext();
        _smsList = new ArrayList<SMSModel>();
    }

    public GetSMSContoller(Context context) {

        _currentContext = context;
        _smsList = new ArrayList<SMSModel>();
    }

    public Object getContent() {

        getSMS();

        return _smsList;
    }


    public void getSMS()
    {
        if(_currentActivity != null) {
            if (!Helper.mayRequestPermission(_currentActivity, READ_SMS, Constants.REQUEST_PERMISSION_VALUE)) {
                return;
            }
        }

        _smsList.clear();


        final String ADRESS = "address";
        final String BODY = "body";
        final String TYPE = "type";
        final String DATE = "date";

        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = _currentContext.getContentResolver().query(uri, null, null ,null,null);

        SMSModel sms = null;
        if(cursor.moveToFirst()) {
            for(int i=0; i < cursor.getCount(); i++) {
                sms = new SMSModel();
                sms.destination = cursor.getString(cursor.getColumnIndexOrThrow(ADRESS));
                sms.message = cursor.getString(cursor.getColumnIndexOrThrow(BODY));

                int smsType = cursor.getInt(cursor.getColumnIndexOrThrow(TYPE));

                switch (smsType) {
                    case Telephony.Sms.MESSAGE_TYPE_INBOX:
                        sms.type = Enumerators.SMSActionType.input;
                        break;
                    case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                        sms.type = Enumerators.SMSActionType.output;
                        break;
                    case Telephony.Sms.MESSAGE_TYPE_SENT:
                        sms.type = Enumerators.SMSActionType.sent;
                        break;
                    case Telephony.Sms.MESSAGE_TYPE_FAILED:
                        sms.type = Enumerators.SMSActionType.failed;
                        break;
                    case Telephony.Sms.MESSAGE_TYPE_QUEUED:
                        sms.type = Enumerators.SMSActionType.queued;
                        break;
                    case Telephony.Sms.MESSAGE_TYPE_DRAFT:
                        sms.type = Enumerators.SMSActionType.draft;
                        break;
                    default:
                        sms.type = Enumerators.SMSActionType.all;
                        break;
                }
                sms.tm = cursor.getLong(cursor.getColumnIndexOrThrow(DATE));
                _smsList.add(sms);

                cursor.moveToNext();
            }
        }
        cursor.close();
    }
}
