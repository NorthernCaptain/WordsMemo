package northern.captain.app.WordsMemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.db.TasksDB;
import northern.captain.app.WordsMemo.factory.TaskFactory;
import northern.captain.app.WordsMemo.logic.Tasks;
import northern.captain.tools.StringUtils;

import java.util.List;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 16.05.14
 * Time: 22:34
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TaskInfoListFragment extends Fragment
{
    ListView listView;
    CustomListAdapter listAdapter;

    TextView titleLbl;

    public interface OnTaskSelectedListener
    {
        public void onSelected(Tasks task);
    }

    private OnTaskSelectedListener onTaskSelectedListener;

    public TaskInfoListFragment(int taskStatus, boolean sortByScore, OnTaskSelectedListener onTaskSelectedListener)
    {
        this.taskStatus = taskStatus;
        this.sortByScore = sortByScore;
        this.onTaskSelectedListener = onTaskSelectedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.task_info_view, container, false);

        Spinner langSpin = (Spinner)v.findViewById(R.id.task_info_spinner);

        langSpin.setSelection(0);

        setHasOptionsMenu(true);

        titleLbl = (TextView) v.findViewById(R.id.task_info_textView);

        reloadItems();

        listView = (ListView)v.findViewById(R.id.task_info_list);
        listAdapter = new CustomListAdapter(AndroidContext.current.app, R.layout.task_info_row);
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

        Spinner spinner = (Spinner) v.findViewById(R.id.task_info_spinner);
        spinner.setSelection(sortByScore ? 0 : 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                sortByScore = pos == 0;
                reloadItems();
                listAdapter.notifyDataSetInvalidated();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

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
        if(position < 0 || position >= items.size())
            return;

        Tasks task = items.get(position);

        onTaskSelectedListener.onSelected(task);
    }

    protected void openAddNew(Tasks editWord)
    {
    }

    boolean sortByScore = true;
    int taskStatus = Tasks.STATUS_DONE;

    protected void reloadItems()
    {
        items = TaskFactory.instance().getTasksByStatus( taskStatus,
                sortByScore ? TasksDB.DBF_SCORE : TasksDB.DBF_FINISH_DATE);
        titleLbl.setText(sortByScore ? R.string.high_score_lbl : R.string.by_date_lbl);
    }

    protected void askForDelete(final Tasks words)
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

    public void doDelete(Tasks words)
    {
        TaskFactory.instance().delete(words);
        reloadItems();
        listAdapter.notifyDataSetInvalidated();
    }

    protected List<Tasks> items;

    class CustomListAdapter extends ArrayAdapter<Tasks>
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

        /* (non-Javadoc)
                         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
                         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;

            if (v == null)
            {
                LayoutInflater vi = (LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.task_info_row, null);
            }

            Tasks item = items.get(position);

            TextView tv = (TextView) v.findViewById(R.id.task_info_row_name);
            tv.setText(item.getName());

            tv = (TextView) v.findViewById(R.id.task_info_row_score);
            if(item.getStatus() == Tasks.STATUS_DONE)
            {
                tv.setText(String.valueOf(item.getScore()));
            }
            else
            {
                tv.setText(String.valueOf(item.getWordsPassed()*100/item.getWordsTotal()) + "%");
            }

            tv = (TextView) v.findViewById(R.id.task_info_row_tags);
            tv.setText(item.getTagSet().toString());

            tv = (TextView) v.findViewById(R.id.task_info_row_date);
            tv.setText(StringUtils.formatDateTime(item.getFinishDateDate()));

            tv = (TextView) v.findViewById(R.id.task_info_row_info);
            {
                String msg = AndroidContext.current.app.getResources().getString(R.string.task_info_lbl);
                tv.setText(String.format(msg, item.getWordsTotal(), item.getWordsErrorString(), StringUtils.formatTimeSpent(item.getTimeSpentSec())));
            }

            return v;
        }

    }
}
