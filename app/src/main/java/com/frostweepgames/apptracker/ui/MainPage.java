package com.frostweepgames.apptracker.ui;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.models.MobileInfoModel;
import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.settings.Enumerators;
import com.frostweepgames.apptracker.core.IUIPage;
import com.frostweepgames.apptracker.MainActivity;
import com.frostweepgames.apptracker.R;

import java.util.HashMap;

import static android.Manifest.permission.READ_PHONE_STATE;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class MainPage implements IUIPage {

    private MainActivity _mainActivity;

    private ConstraintLayout _selfLayout;

    private Button _saveButton;

    private EditText _userEmailText;

    @Override
    public void Init(Activity activity) {

        _mainActivity = (MainActivity) activity;

        _selfLayout = activity.findViewById(R.id.MainPageLayout);

        _saveButton = activity.findViewById(R.id.button_save);

        _userEmailText = activity.findViewById(R.id.input_email);

        _saveButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        SaveButtonOnClickHandler(view);
                    }
                }
        );

        Hide();
    }

    @Override
    public void Show() {
        _selfLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void Hide() {
        _selfLayout.setVisibility(View.INVISIBLE);
    }

    private void SaveButtonOnClickHandler(View view) {

        if (_mainActivity != null) {
            if (!Helper.mayRequestPermission(_mainActivity, READ_PHONE_STATE, Constants.REQUEST_PERMISSION_VALUE)) {
                return;
            }
        }

        MobileInfoModel mobileInfo = (MobileInfoModel)MainActivity.systemControllersManager.GetController(Enumerators.ControllerType.MOBILE_INFO).getContent();

        MainActivity.settingsManager.localAppSettings.userEmail = _userEmailText.getText().toString();
        MainActivity.settingsManager.localAppSettings.serviceUrl = Constants.SERVER_URL;

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.SERVER_KEY_EMAIL, MainActivity.settingsManager.localAppSettings.userEmail);
        map.put(Constants.SERVER_KEY_DATA, mobileInfo);

        MainActivity.networkManager.sendPOSTRequest(Enumerators.APINetRequestType.CHECK, Enumerators.NetworkRequestType.POST, map);
    }
}
