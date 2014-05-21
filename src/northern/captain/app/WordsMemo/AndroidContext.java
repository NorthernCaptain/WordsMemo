package northern.captain.app.WordsMemo;

import android.os.Handler;
import android.view.Menu;
import northern.captain.app.WordsMemo.ui.NavDrawer;


/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 27.10.13
 * Time: 22:03
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class AndroidContext
{
    public static AndroidContext current;

    public MainActivity app;

    public Menu mainMenu;

    public NavDrawer drawer;

    public Handler mainHandler;
}
