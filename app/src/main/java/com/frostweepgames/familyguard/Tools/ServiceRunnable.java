package com.frostweepgames.familyguard.Tools;

/**-------------------------------
 * Created by Artem Shyriaiev on 15.09.2018
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class ServiceRunnable {
    public void run(final Command command, final String string) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                command.run(string);
            }
        };
        runnable.run();
    }

    public interface Command {
        void run(String data);
    }
}