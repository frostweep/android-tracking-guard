package com.frostweepgames.apptracker.syncservice;

/**
 * -------------------------------
 * Created by artem on 08.03.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 * ---------------------------------
 */

public class ServiceRunnable {
    public void run(final Command command) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                command.run();
            }
        };
        runnable.run();
    }

    public interface Command {
        void run();
    }
}