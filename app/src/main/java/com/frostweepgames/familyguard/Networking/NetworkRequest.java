package com.frostweepgames.familyguard.Networking;

import com.frostweepgames.androidhelperlibrary.tools.LogTracker;
import com.frostweepgames.androidhelperlibrary.tools.SerializationTool;
import com.frostweepgames.familyguard.Settings.Constants;
import com.frostweepgames.familyguard.Tools.ServiceRunnable;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**-------------------------------
 * Created by Artem Shyriaiev on 15.09.2018
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class NetworkRequest {

    private NetworkRequest _selfObject;

    private NetworkManagerLocal _networkManager;

    private AsyncHttpClient _httpClient;

    private ServiceRunnable.Command _onSuccessCommand;
    private ServiceRunnable.Command _onFailedCommand;

    public NetworkRequest(NetworkManagerLocal netManager, Enumerators.NetworkRequestType requestType, String routing,
                          HashMap<String, Object> sendData, ServiceRunnable.Command onSuccessCommand, ServiceRunnable.Command onFailedCommand) {
        _networkManager = netManager;
        _onSuccessCommand = onSuccessCommand;
        _onFailedCommand = onFailedCommand;

        _selfObject = this;

        sendRequest(routing, requestType, sendData);
    }


    private void sendRequest(String routing, Enumerators.NetworkRequestType requestType, HashMap<String, Object> sendData) {
        RequestParams params = new RequestParams();

        for (Map.Entry<String, Object> entry : sendData.entrySet()) {

            Object val = entry.getValue();
            String value;

            if (val.getClass() != String.class)
                value = SerializationTool.serializeObject(val);
            else
                value = (String) val;

            params.put(entry.getKey(), value);
        }

        _httpClient = new AsyncHttpClient();

        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    if (_onSuccessCommand != null)
                        new ServiceRunnable().run(_onSuccessCommand, new String(bytes, Constants.DEFAULT_CHARSET));

                } catch (UnsupportedEncodingException ex) {
                    LogTracker.trackLog(ex);
                }

                if (_networkManager.networkRequests.contains(_selfObject))
                    _networkManager.networkRequests.remove(_selfObject);
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String data = "";

                    if (bytes != null)
                        data = new String(bytes, Constants.DEFAULT_CHARSET);

                    if (_onFailedCommand != null)
                        new ServiceRunnable().run(_onFailedCommand, data);

                } catch (UnsupportedEncodingException ex) {
                    LogTracker.trackLog(ex);
                }

                if (_networkManager.networkRequests.contains(_selfObject))
                    _networkManager.networkRequests.remove(_selfObject);
            }

            @Override
            public void onRetry(int retryNo) {
                LogTracker.trackLog(new Exception("Retry error number: " + Integer.toString(retryNo)));
            }
        };

        switch (requestType) {
            case GET:
                _httpClient.get(routing, params, handler);
                break;
            case POST:
                _httpClient.post(routing, params, handler);
                break;
            default:
                break;
        }
    }


    public void dispose(boolean interrupt) {
        if (_httpClient != null)
            _httpClient.cancelAllRequests(interrupt);
    }
}