package com.frostweepgames.apptracker.core;

import android.app.Activity;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public interface IUIPage
{
    void Init(Activity activity);
    void Show();
    void Hide();
}
