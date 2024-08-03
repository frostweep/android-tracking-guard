package com.frostweepgames.apptracker.networking;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.managers.NetworkManager;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.loopj.android.http.*;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

    public class NetworkPOSTRequest {

    private Enumerators.APINetRequestType _requestType;
    private NetworkManager _networkManager;


    public NetworkPOSTRequest(NetworkManager netManager, Enumerators.APINetRequestType apiNetRequestType, Enumerators.NetworkRequestType requestType, String routing, HashMap<String, Object> sendData) {
        _networkManager = netManager;
        _requestType = apiNetRequestType;

        sendRequest(routing, requestType, sendData);
    }


    private void sendRequest(String routing, Enumerators.NetworkRequestType requestType, HashMap<String, Object> sendData) {
        RequestParams params = new RequestParams();

        for (Map.Entry<String, Object> entry : sendData.entrySet()) {

            Object val = entry.getValue();
            String value;

            if (val.getClass() != String.class)
                value = Helper.serializeObject(val);
            else
                value = (String) val;


            params.put(entry.getKey(), value);
        }

        AsyncHttpClient client = new AsyncHttpClient();

        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    _networkManager.handleResult(_requestType, new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.d("Found exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    if (bytes != null)
                        _networkManager.handleResult(_requestType, new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.d("Found exception", e.getMessage());
                }
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d("Found error: ", Integer.toString(retryNo));
            }
        };


        if (requestType == Enumerators.NetworkRequestType.GET)
            client.get(routing, params, handler);
        else
            client.post(routing, params, handler);
    }
}