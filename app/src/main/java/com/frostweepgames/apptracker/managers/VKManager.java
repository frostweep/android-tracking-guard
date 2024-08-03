package com.frostweepgames.apptracker.managers;

import android.app.Activity;
import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.settings.Constants;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * -------------------------------
 * Created by artem on 03.04.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 * ---------------------------------
 */

public class VKManager {

    private Activity _activity;

    public boolean isLoggedIn = false;

    public void init(Activity activity) {
        _activity = activity;

        checkLogin();
    }

    public void login() {
        VKSdk.login(_activity, Constants.VK_SCOPE);
    }

    public void logout() {
        VKSdk.logout();
    }

    public boolean checkLogin()
    {
        return isLoggedIn = VKSdk.isLoggedIn();
    }

    public void loadFriends() {

        VKRequest request = new VKRequest(Constants.VK_API_FRIENDS_GET, VKParameters.from(VKApiConst.FIELDS, Constants.VK_FRIENDS_FIELDS));

        request.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {

                Helper.drawSystemMessageTooltip(_activity, response.responseString);
            }

            @Override
            public void onError(VKError error) {
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }

        });
    }

    public void loadMessages() {

        VKRequest request = new VKRequest(Constants.VK_API_MESSAGES_GET, VKParameters.from(VKApiConst.FIELDS, Constants.VK_MESSAGES_FIELDS));

        request.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {

                Helper.drawSystemMessageTooltip(_activity, response.responseString);
            }

            @Override
            public void onError(VKError error) {
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }

        });
    }

    public void loadGroups() {

        VKRequest request = new VKRequest(Constants.VK_API_GROUPS_GET, VKParameters.from(VKApiConst.FIELDS, Constants.VK_GROUPS_FIELDS));

        request.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {

                Helper.drawSystemMessageTooltip(_activity, response.responseString);
            }

            @Override
            public void onError(VKError error) {
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }

        });
    }
}