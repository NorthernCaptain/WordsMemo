package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.AccountFactory;
import northern.captain.app.WordsMemo.logic.Accounts;

/**
 * Created with IntelliJ IDEA.
 * User: NorthernCaptain
 * Date: 29.10.13
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public class AccountEditDlgFragment extends DialogFragment
{
    public interface IEditCallback
    {
        public void accountChanged(Accounts account);
        public void accountCreated(Accounts account);
    }

    private IEditCallback callback;

    public AccountEditDlgFragment(IEditCallback callback)
    {
        this.callback = callback;
    }

    public AccountEditDlgFragment(Accounts account, IEditCallback callback)
    {
        this.callback = callback;
        this.account = account;
    }

    Accounts  account;

    EditText titleEdit;
    EditText descrEdit;
    EditText amountEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.account_edit_dlg, container, false);
        titleEdit = (EditText)v.findViewById(R.id.accdlg_title_val);
        descrEdit = (EditText)v.findViewById(R.id.accdlg_desc_val);
        amountEdit = (EditText)v.findViewById(R.id.accdlg_amount_val);

        if(account != null)
        {
            titleEdit.setText(account.getName());
            amountEdit.setText(String.valueOf(account.getAmount()));
            descrEdit.setText(account.getComments());

            getDialog().setTitle(R.string.accdlg_edit_title);
        }
        else
        {
            getDialog().setTitle(R.string.accdlg_add_title);
        }

        Button button = (Button) v.findViewById(R.id.accdlg_ok_btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doOK();
            }
        });

        button = (Button) v.findViewById(R.id.accdlg_cancel_but);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return v;
    }

    private void doOK()
    {
        boolean isNew = account == null;
        if(isNew)
        {
            account = AccountFactory.instance().newAccount();
        }

        account.setName(titleEdit.getText().toString());
        account.setComments(descrEdit.getText().toString());
        account.setAmount(Integer.parseInt(amountEdit.getText().toString()));

        if(isNew)
        {
            AccountFactory.instance().add(account);
            if(callback != null)
            {
                callback.accountCreated(account);
            }
        } else
        {
            AccountFactory.instance().update(account);
            if(callback != null)
            {
                callback.accountChanged(account);
            }
        }

        dismiss();
    }
}
