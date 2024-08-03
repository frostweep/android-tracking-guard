package com.frostweepgames.apptracker.managers;

import android.app.Activity;

import com.frostweepgames.apptracker.core.IUIPage;
import com.frostweepgames.apptracker.settings.Enumerators.UIPageType;
import com.frostweepgames.apptracker.*;
import com.frostweepgames.apptracker.ui.MainPage;
import com.frostweepgames.apptracker.ui.SettingsPage;
import com.frostweepgames.apptracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class UIManager {
    private MainActivity _mainActivity;

    private Map<UIPageType, IUIPage> _uiPages;
    private ArrayList<UIPageType> _activeUIPages;

    public UIManager(Activity activity) {
        _mainActivity = (MainActivity) activity;

        activity.setContentView(R.layout.activity_main);

        init();
    }

    public UIManager() {

    }

    public void init() {
        _activeUIPages = new ArrayList<>();
        _uiPages = new HashMap<>();
        _uiPages.put(UIPageType.MAIN_PAGE, new MainPage());
        _uiPages.put(UIPageType.SETTINGS_PAGE, new SettingsPage());

        for (Map.Entry<UIPageType, IUIPage> entry : _uiPages.entrySet())
            entry.getValue().Init(_mainActivity);
    }

    public void showPage(UIPageType pageType) {

        for (UIPageType entry : _activeUIPages)
            _uiPages.get(entry).Hide();
        _activeUIPages.clear();

        _activeUIPages.add(pageType);
        _uiPages.get(pageType).Show();
    }
}