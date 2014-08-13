package northern.captain.app.WordsMemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.WordFactory;
import northern.captain.app.WordsMemo.logic.Words;

import java.util.List;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 16.05.14
 * Time: 22:34
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class WordCatalogFragment extends Fragment
{
    ListView listView;
    CustomListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.wordcat_view, container, false);

        Spinner langSpin = (Spinner)v.findViewById(R.id.wordcat_lang_spinner);

        langSpin.setSelection(0);

        setHasOptionsMenu(true);

        reloadWords();

        listView = (ListView)v.findViewById(R.id.wordcat_list);
        listAdapter = new CustomListAdapter(AndroidContext.current.app, R.layout.wordcat_row);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                processItemClick(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                if(position >= items.size())
                    return false;

                askForDelete(items.get(position));

                return true;
            }
        });


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.wordcatmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_new_tag:
                openAddNew(null);
                return true;
        }
        return super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void processItemClick(int position)
    {
        openAddNew(position >= items.size() ? null : items.get(position));
    }

    protected void openAddNew(Words editWord)
    {
        AndroidContext.current.drawer.openFragment(FragmentFactory.instance().newWordEditFragment(new WordEditFragment.onOKListener()
        {
            @Override
            public void onOK(Words newWord, boolean update)
            {
                if(update)
                {
                    WordFactory.instance().update(newWord);
                    listAdapter.notifyDataSetChanged();
                }
                else
                {
                    WordFactory.instance().add(newWord);
                    reloadWords();
                    listAdapter.notifyDataSetInvalidated();
                }
            }
        }, editWord));
    }

    protected void reloadWords()
    {
        items = WordFactory.instance().getWords();
    }

    protected void askForDelete(final Words words)
    {
        TwoButtonDialog.newInstance(R.string.delete_confirmation,
                new TwoButtonDialog.Callback()
                {
                    @Override
                    public void onPositiveClick()
                    {
                        doDelete(words);
                    }

                    @Override
                    public void onNegativeClick()
                    {
                    }
                }).show(AndroidContext.current.app.getSupportFragmentManager(), "delete_dialog");

    }

    public void doDelete(Words words)
    {
        WordFactory.instance().delete(words);
        reloadWords();
        listAdapter.notifyDataSetInvalidated();
    }

    protected List<Words> items;

    class CustomListAdapter extends ArrayAdapter<Words>
    {

        public CustomListAdapter(Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount()
        {
            return items.size() + 1;
        }

        @Override
        public boolean areAllItemsEnabled()
        {
            return false;
        }

        @Override
        public boolean isEnabled(int position)
        {
            return true;
        }

        /* (non-Javadoc)
                         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
                         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;

            if (position >= items.size())
            {
                if (v == null || v.getId() != R.id.wordcat_row_add)
                {
                    LayoutInflater vi = (LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.wordcat_row_add, null);
                }
                return v;
            }

            Words item = items.get(position);

            if (v == null || v.getId() != R.id.wordcat_row_base)
            {
                LayoutInflater vi = (LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.wordcat_row, null);
            }

            TextView tv = (TextView) v.findViewById(R.id.wordcat_item_name);
            tv.setText(item.getName());

            tv = (TextView) v.findViewById(R.id.wordcat_amount);
            tv.setText(String.valueOf(item.getUsedTimes()));

            tv = (TextView) v.findViewById(R.id.wordcat_comment);
            String desc = item.getTranslation() != null ? item.getTranslation() : item.getThesaurus();
            if (desc != null)
            {
                tv.setText(desc);
            } else
            {
                tv.setVisibility(View.GONE);
            }

            return v;
        }

    }
}
