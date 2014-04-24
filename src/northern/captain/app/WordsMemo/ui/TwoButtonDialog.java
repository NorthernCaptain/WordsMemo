package northern.captain.app.WordsMemo.ui;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 30.10.13
 * Time: 0:15
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TwoButtonDialog extends DialogFragment
{

    public interface Callback
    {
        public void onPositiveClick();
        public void onNegativeClick();
    }

    private Callback callback;

    public static TwoButtonDialog newInstance(int title, Callback callback) {
        TwoButtonDialog frag = new TwoButtonDialog(callback);
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    private TwoButtonDialog(Callback callback)
    {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                callback.onPositiveClick();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                callback.onNegativeClick();
                            }
                        }
                )
                .create();
    }
}
