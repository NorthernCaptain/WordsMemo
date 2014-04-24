package northern.captain.app.WordsMemo.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 25.10.13
 * Time: 18:55
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class NavDrawer
{
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout          mDrawerLayout;
    Activity              activity;

    public NavDrawer(Activity activity, DrawerLayout drawerLayout, ActionBarDrawerToggle toggle)
    {
        this.mDrawerLayout = drawerLayout;
        this.mDrawerToggle = toggle;
        this.activity = activity;
    }

    public void init()
    {
        ListView drawerList = (ListView) activity.findViewById(R.id.left_drawer);

        fillDrawerList(drawerList);

    }

    List<DrawerItem> items = new ArrayList<DrawerItem>();

    private void fillDrawerList(ListView drawerList)
    {
        items.add(new DrawerItem(R.string.mi_title_main, DrawerItem.TYPE_TITLE));
        items.add(new DrawerItem(R.string.mi_do_training));
        items.add(new DrawerItem(R.string.mi_do_test));
        items.add(new DrawerItem(R.string.mi_title_settings, DrawerItem.TYPE_TITLE));
        items.add(new DrawerItem(R.string.mi_categories));
        items.add(new DrawerItem(R.string.mi_manage_words));

        drawerList.setAdapter(new CustomListAdapter(activity, R.layout.drawerlist_row));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                itemClicked(position);
            }
        });
    }


    private void itemClicked(int position)
    {
        switch(items.get(position).id)
        {
            case R.string.mi_accounts:
                openFragment(FragmentFactory.instance().newAccountFragment());
                break;
        }
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    class CustomListAdapter extends ArrayAdapter<DrawerItem>
    {

        public CustomListAdapter(Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount()
        {
            return items.size();
        }

        @Override
        public boolean areAllItemsEnabled()
        {
            return false;
        }

        @Override
        public boolean isEnabled(int position)
        {
            return items.get(position).type != DrawerItem.TYPE_TITLE;
        }

        /* (non-Javadoc)
                         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
                         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;
            DrawerItem item = items.get(position);

            if(item.type == DrawerItem.TYPE_ITEM)
            {
                if (v == null)
                {
                    LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.drawerlist_row, null);
                }

                ImageView imageView = (ImageView)v.findViewById(R.id.drl_icon);
                imageView.setVisibility(View.GONE);

                TextView tv = (TextView)v.findViewById(R.id.drl_item_name);
                tv.setText(item.name);
            }

            if(item.type == DrawerItem.TYPE_TITLE)
            {
                if (v == null)
                {
                    LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.drawerlist_row_title, null);
                }

                TextView tv = (TextView)v.findViewById(R.id.drl_item_title);
                tv.setText(item.name);
                v.setFocusableInTouchMode(false);
            }

            return v;
        }

    }

    public class DrawerItem
    {
        public static final int TYPE_TITLE = 1;
        public static final int TYPE_ITEM = 2;

        public String name;
        public int type = TYPE_ITEM;
        public int id;

        public DrawerItem(int nameId)
        {
            id = nameId;
            name = activity.getResources().getString(nameId);
        }

        public DrawerItem(int nameId, int type)
        {
            id = nameId;
            name = activity.getResources().getString(nameId);
            this.type = type;
        }

        public DrawerItem(String name)
        {
            this.name = name;
        }
    }

    public void openFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = AndroidContext.current.app.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = AndroidContext.current.app.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
        fragmentTransaction.commit();
    }
}
