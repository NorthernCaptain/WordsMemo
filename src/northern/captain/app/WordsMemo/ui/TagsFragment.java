package northern.captain.app.WordsMemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TagFactory;
import northern.captain.app.WordsMemo.logic.Tags;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NorthernCaptain
 * Date: 29.10.13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class TagsFragment extends Fragment
{
    public TagsFragment()
    {

    }

    ListView listView;
    CustomListAdapter listAdapter;
    TextView totalVal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        items = TagFactory.instance().getTags();

        View v = inflater.inflate(R.layout.account_view, container, false);

        listView = (ListView)v.findViewById(R.id.account_list);
        listAdapter = new CustomListAdapter(AndroidContext.current.app, R.layout.accountlist_row);
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
        totalVal = (TextView)v.findViewById(R.id.account_total_val);

        setHasOptionsMenu(true);
        updateTotal();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.tagmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_new_tag:
                openNewDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void openNewDialog()
    {
        TagsEditDialogFragment dlg = new TagsEditDialogFragment(new TagsEditDialogFragment.IEditCallback()
        {
            @Override
            public void tagCreated(Tags account)
            {
                listAdapter.notifyDataSetInvalidated();
                updateTotal();
            }

            @Override
            public void tagChanged(Tags account)
            {
                items = TagFactory.instance().getTags();
                listAdapter.notifyDataSetInvalidated();
                updateTotal();
            }
        });

        dlg.show(AndroidContext.current.app.getSupportFragmentManager(), "account_dialog");
    }

    protected void processItemClick(int position)
    {
        if(position >= items.size())
        {
            openNewDialog();
        } else
        {
            TagsEditDialogFragment dlg = new TagsEditDialogFragment(items.get(position),
                    new TagsEditDialogFragment.IEditCallback()
            {
                @Override
                public void tagCreated(Tags account)
                {
                    listAdapter.notifyDataSetInvalidated();
                    updateTotal();
                }

                @Override
                public void tagChanged(Tags account)
                {
                    items = TagFactory.instance().getTags();
                    listAdapter.notifyDataSetInvalidated();
                    updateTotal();
                }
            });

            dlg.show(AndroidContext.current.app.getSupportFragmentManager(), "account_dialog");
        }
    }

    protected List<Tags> items;

    class CustomListAdapter extends ArrayAdapter<Tags>
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
                if (v == null || v.getId() != R.id.acclst_row_add)
                {
                    LayoutInflater vi = (LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.accountlist_row_add, null);
                }
                return v;
            }

            Tags item = items.get(position);

            if (v == null || v.getId() != R.id.acclst_row_base)
            {
                LayoutInflater vi = (LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.accountlist_row, null);
            }

            TextView tv = (TextView) v.findViewById(R.id.acclst_item_name);
            tv.setText(item.getName());

            tv = (TextView) v.findViewById(R.id.acclst_amount);
            tv.setText(String.valueOf(item.getAmount()));

            tv = (TextView) v.findViewById(R.id.acclst_comment);
            if (item.getComments() != null)
            {
                tv.setText(item.getComments());
            } else
            {
                tv.setVisibility(View.GONE);
            }

            return v;
        }

    }

    private void updateTotal()
    {
        int total = 0;
        for (Tags acc : items)
        {
            total += acc.getAmount();
        }

        totalVal.setText(String.valueOf(total));
    }

    private void askForDelete(final Tags tags)
    {
        TwoButtonDialog.newInstance(R.string.delete_confirmation,
                new TwoButtonDialog.Callback()
                {
                    @Override
                    public void onPositiveClick()
                    {
                        doDelete(tags);
                    }

                    @Override
                    public void onNegativeClick()
                    {
                    }
                }).show(AndroidContext.current.app.getSupportFragmentManager(), "delete_dialog");
    }


    private void doDelete(Tags account)
    {
        TagFactory.instance().delete(account);
        items = TagFactory.instance().getTags();
        listAdapter.notifyDataSetInvalidated();
        updateTotal();
    }

}
