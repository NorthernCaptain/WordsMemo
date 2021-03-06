package northern.captain.app.WordsMemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import northern.captain.app.WordsMemo.db.SQLManager;
import northern.captain.app.WordsMemo.factory.*;
import northern.captain.app.WordsMemo.ui.FragmentFactory;
import northern.captain.app.WordsMemo.ui.NavDrawer;
import northern.captain.tools.IActivityResultCallback;
import northern.captain.tools.MyToast;
import northern.captain.tools.Settings;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity
{
    private NavDrawer drawer;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;

    private SQLManager sql;

    public String getmTitle()
    {
        return mTitle;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AndroidContext.current = new AndroidContext();
        AndroidContext.current.app = this;
        AndroidContext.current.settings = new Settings();

        MyToast.initialize();

        setContentView(R.layout.main);

        mTitle = getTitle().toString();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer = new NavDrawer(this, mDrawerLayout, mDrawerToggle);
        AndroidContext.current.drawer = drawer;
        initDrawer();

        AndroidContext.current.mainHandler = new Handler(getMainLooper());

        initialize();
    }

    public String x(int resId)
    {
        return getResources().getString(resId);
    }

    private void initDrawer()
    {
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        )

        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                getSupportActionBar().setTitle(getmTitle());
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                getSupportActionBar().setTitle(R.string.drawer_opened);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer.init();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        AndroidContext.current.mainMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if(mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void doQuit()
    {
        finish();
    }

    public void initialize()
    {
        SQLManager.initialize();
        sql = SQLManager.instance();

        TagFactory.initialize();
        WordFactory.initialize();
        FragmentFactory.initialize();
        TTSFactory.initialize();
        TTSFactory.instance().initEngine();
        ExportImportFactory.initialize();
        UserFactory.initialize();
        TaskFactory.initialize();

        drawer.setFragment(FragmentFactory.instance().newIntroFragment());
    }

    public void openInitialScreen()
    {
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SQLManager.instance().resume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        SQLManager.instance().shutdown();
    }

    @Override
    protected void onDestroy()
    {
        TTSFactory.instance().shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            if(mDrawerLayout.isDrawerOpen(Gravity.START))
            {
                mDrawerLayout.closeDrawer(Gravity.START);
            }
            else
            {
                mDrawerLayout.openDrawer(Gravity.START);
            }
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    public DrawerLayout getmDrawerLayout()
    {
        return mDrawerLayout;
    }



    private Map<Integer, IActivityResultCallback> activityResultCallbackMap = new HashMap<Integer, IActivityResultCallback>();

    public void addOnActivityCallback(int code, IActivityResultCallback callback)
    {
        activityResultCallbackMap.put(code, callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IActivityResultCallback callback = activityResultCallbackMap.get(requestCode);
        if(callback != null)
        {
            boolean ret = callback.onActivityResult(requestCode, resultCode, data);
            if(!ret)
            {
                activityResultCallbackMap.remove(callback);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
