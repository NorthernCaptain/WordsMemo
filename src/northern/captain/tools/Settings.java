package northern.captain.tools;

import android.content.Context;
import android.content.SharedPreferences;
import northern.captain.app.WordsMemo.AndroidContext;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 11.08.14
 * Time: 23:48
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class Settings
{
    public static final String IMPORT_PATH = "importPath";
    public static final String IMPORT_FNAME = "importFName";
    public static final String EXPORT_PATH = "exportPath";


    SharedPreferences preferences;

    public Settings()
    {
        preferences = AndroidContext.current.app.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public String getString(String name, String defaultValue)
    {
        return preferences.getString(name, defaultValue);
    }

    public void setString(String name, String value)
    {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(name, value);
        edit.commit();
    }
}
