package northern.captain.app.WordsMemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.AccountFactory;
import northern.captain.app.WordsMemo.logic.Accounts;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NorthernCaptain
 * Date: 29.10.13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class AccountFragment extends Fragment
{
    public AccountFragment()
    {

    }

    ListView listView;
    CustomListAdapter listAdapter;
    TextView totalVal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        items = AccountFactory.instance().getAccounts();

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

        updateTotal();

        return v;
    }

    protected void processItemClick(int position)
    {
        if(position >= items.size())
        {
            AccountEditDlgFragment dlg = new AccountEditDlgFragment(new AccountEditDlgFragment.IEditCallback()
            {
                @Override
                public void accountChanged(Accounts account)
                {
                    listAdapter.notifyDataSetInvalidated();
                    updateTotal();
                }

                @Override
                public void accountCreated(Accounts account)
                {
                    items = AccountFactory.instance().getAccounts();
                    listAdapter.notifyDataSetInvalidated();
                    updateTotal();
                }
            });

            dlg.show(AndroidContext.current.app.getSupportFragmentManager(), "account_dialog");
        } else
        {
            AccountEditDlgFragment dlg = new AccountEditDlgFragment(items.get(position),
                    new AccountEditDlgFragment.IEditCallback()
            {
                @Override
                public void accountChanged(Accounts account)
                {
                    listAdapter.notifyDataSetInvalidated();
                    updateTotal();
                }

                @Override
                public void accountCreated(Accounts account)
                {
                    items = AccountFactory.instance().getAccounts();
                    listAdapter.notifyDataSetInvalidated();
                    updateTotal();
                }
            });

            dlg.show(AndroidContext.current.app.getSupportFragmentManager(), "account_dialog");
        }
    }

    protected List<Accounts> items;

    class CustomListAdapter extends ArrayAdapter<Accounts>
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

            if(position >= items.size())
            {
                if (v == null || v.getId() != R.id.acclst_row_add)
                {
                    LayoutInflater vi = (LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.accountlist_row_add, null);
                }
                return v;
            }

            Accounts item = items.get(position);

            if (v == null || v.getId() != R.id.acclst_row_base)
            {
                LayoutInflater vi = (LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.accountlist_row, null);
            }

            TextView tv = (TextView)v.findViewById(R.id.acclst_item_name);
            tv.setText(item.getName());

            tv = (TextView)v.findViewById(R.id.acclst_amount);
            tv.setText(String.valueOf(item.getAmount()));

            tv = (TextView)v.findViewById(R.id.acclst_comment);
            if(item.getComments() != null)
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
        for(Accounts acc : items)
        {
            total += acc.getAmount();
        }

        totalVal.setText(String.valueOf(total));
    }

    private void askForDelete(final Accounts account)
    {
        TwoButtonDialog.newInstance(R.string.delete_confirmation,
                new TwoButtonDialog.Callback()
                {
                    @Override
                    public void onPositiveClick()
                    {
                        doDelete(account);
                    }

                    @Override
                    public void onNegativeClick()
                    {
                    }
                }).show(AndroidContext.current.app.getSupportFragmentManager(), "delete_dialog");
    }


    private void doDelete(Accounts account)
    {
        AccountFactory.instance().delete(account);
        items = AccountFactory.instance().getAccounts();
        listAdapter.notifyDataSetInvalidated();
        updateTotal();
    }
    
}
