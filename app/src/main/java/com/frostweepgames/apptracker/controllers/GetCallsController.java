package com.frostweepgames.apptracker.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.frostweepgames.apptracker.models.CallsModel;
import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.core.IController;

import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.READ_CALL_LOG;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class GetCallsController implements IController {

    private Activity _currentActivity;
    private Context _currentContext;

    private ArrayList<CallsModel> _callList;


    private Cursor _cursor;

    public GetCallsController(Activity currentActivity) {

        _currentActivity = currentActivity;
        _currentContext = currentActivity.getApplicationContext();
        _callList = new ArrayList<CallsModel>();
    }

    public GetCallsController(Context context) {

        _currentContext = context;
        _callList = new ArrayList<CallsModel>();
    }

    public Object getContent() {

        getCalls();

        return _callList;
    }

    @SuppressLint("MissingPermission")
    public void getCalls() {

        if(_currentActivity != null) {
            if (!Helper.mayRequestPermission(_currentActivity, READ_CALL_LOG, Constants.REQUEST_PERMISSION_VALUE)) {
                return;
            }
        }

        _callList.clear();

        ContentResolver contentResolver = _currentContext.getContentResolver();

        _cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,null, null, null, null);

        int numberColumn = _cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int typeColumn = _cursor.getColumnIndex(CallLog.Calls.TYPE);
        int durationColumn = _cursor.getColumnIndex(CallLog.Calls.DURATION);
        int dateColumn = _cursor.getColumnIndex(CallLog.Calls.DATE);

        if (_cursor.getCount() > 0) {

            CallsModel call = null;

            while (_cursor.moveToNext()) {
                call = new CallsModel();

                String callerPhoneNumber = _cursor.getString(numberColumn);
                int callType = _cursor.getInt(typeColumn);
                String duration = _cursor.getString(durationColumn);

                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        call.type = Enumerators.PhoneActionType.input;
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        call.type = Enumerators.PhoneActionType.output;
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        call.type = Enumerators.PhoneActionType.missed;
                        break;
                    case CallLog.Calls.BLOCKED_TYPE:
                        call.type = Enumerators.PhoneActionType.blocked;
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        call.type = Enumerators.PhoneActionType.rejected;
                        break;
                    default:
                        call.type = Enumerators.PhoneActionType.unknown;
                        break;
                }
                call.destination = callerPhoneNumber;
                call.duration = duration;
                call.tm  = _cursor.getLong(dateColumn);

                _callList.add(call);
            }
        }
    }
}