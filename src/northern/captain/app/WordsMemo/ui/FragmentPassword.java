package northern.captain.app.WordsMemo.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import northern.captain.app.WordsMemo.R;
import northern.captain.tools.MyToast;

/**
 * Created with IntelliJ IDEA.
 * User: NorthernCaptain
 * Date: 28.10.13
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */
public class FragmentPassword extends DialogFragment
{
    public interface IPasswordCallback
    {
        public boolean passwordEntered(String password);
        public void cancelActivated();
    }

    public static FragmentPassword newInstance(boolean firstTime, IPasswordCallback callback)
    {
        return new FragmentPassword(firstTime, callback);
    }

    public FragmentPassword(boolean firstTime, IPasswordCallback callback)
    {
        this.firstTime = firstTime;
        this.callback = callback;
    }

    private IPasswordCallback callback;

    private boolean firstTime;

    private EditText passwordEdit;
    private EditText verifyEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.passwd_dlg, container, false);

        Button okBut = (Button)v.findViewById(R.id.passwd_okbut);

        okBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(checkPassword())
                {
                    dismiss();
                }
            }
        });

        passwordEdit = (EditText)v.findViewById(R.id.password_text);
        verifyEdit = (EditText)v.findViewById(R.id.password_verify_text);

        if(!firstTime)
        {
            View widv = v.findViewById(R.id.password_verify_text);
            widv.setVisibility(View.GONE);
            widv = v.findViewById(R.id.password_verify_lbl);
            widv.setVisibility(View.GONE);
        }
        else
        {
            TextView tv = (TextView)v.findViewById(R.id.password_text_lbl);
            tv.setText(R.string.passwd_new_lbl);
        }

        getDialog().setTitle(R.string.passwd_title);
        getDialog().setCancelable(true);
        getDialog().setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                if(callback != null)
                {
                    callback.cancelActivated();
                }
            }
        });
        return v;
    }

    private boolean checkPassword()
    {
        String password = passwordEdit.getText().toString().trim();

        if(password.length() < 3)
        {
            MyToast.toast(MyToast.WARN, R.string.password_too_short, true);
            return false;
        }

        if(firstTime)
        {
            String verifyP = verifyEdit.getText().toString().trim();

            if(!password.equals(verifyP))
            {
                MyToast.toast(MyToast.WARN, R.string.password_differs, true);
                return false;
            }
        }

        boolean ret = true;

        if(callback != null)
            ret = callback.passwordEntered(password);

        if(!ret)
        {
            MyToast.toast(MyToast.WARN, R.string.password_incorrect, true);
        }

        return ret;
    }
}
