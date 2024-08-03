package com.frostweepgames.familyguard.Networking;

import android.content.Context;
import android.util.Log;

import com.frostweepgames.androidhelperlibrary.tools.GeneralTools;
import com.frostweepgames.androidhelperlibrary.tools.LogTracker;
import com.frostweepgames.androidhelperlibrary.tools.SerializationTool;
import com.frostweepgames.familyguard.Model.LogModel;
import com.frostweepgames.familyguard.Tools.ManagersTool;
import com.frostweepgames.familyguard.Tools.ServiceRunnable;
import com.frostweepgames.familyguard.Settings.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**-------------------------------
 * Created by Artem Shyriaiev on 15.09.2018
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class NetworkManagerLocal {

    private Context _context;

    public ArrayList<NetworkRequest> networkRequests;

    public NetworkManagerLocal(Context context) {
        _context = context;

        networkRequests = new ArrayList<>();
    }

    public void sendRequest(String routing, Enumerators.NetworkRequestType requestType, HashMap<String, Object> data, boolean useToken,
                            ServiceRunnable.Command onSuccessCommand, ServiceRunnable.Command onFailedCommand) {

        routing = Constants.SERVER_URL + routing;

        if (useToken)
            data.put(Constants.SERVER_KEY_TOKEN, ManagersTool.getUserManager(_context).accountData.token);

        networkRequests.add(new NetworkRequest(this, requestType, routing, data, onSuccessCommand, onFailedCommand));
    }

    public void dispose()
    {
        for (NetworkRequest request : networkRequests) {
            request.dispose(true);
        }
        networkRequests.clear();
    }

    public void sendLogReport(LogModel logModel, com.frostweepgames.familyguard.Settings.Enumerators.LogType logType) {
        switch (logType) {
            case STACK_TRACE:
                Log.i(logModel.tag, SerializationTool.serializeObject(logModel));
                break;
            case MESSAGE_BOX:
                GeneralTools.drawSystemMessageTooltip(_context, SerializationTool.serializeObject(logModel));
                break;
            case NET_REPORT: {
                HashMap<String, Object> map = new HashMap<>();
                map.put(Constants.SERVER_KEY_DATA, SerializationTool.serializeObject(logModel));
                sendRequest(Constants.SERVER_HANDLE_LOGS, Enumerators.NetworkRequestType.POST, map, true,null, null);
            }
            break;
            default:
                break;
        }
    }

    public static boolean isResultIsOkay(String result) {
        if(result == null)
            return false;
        return result.replace('"', '#').contains("#result#:true");
    }
}