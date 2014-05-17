package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TTSFactory;
import northern.captain.tools.StringUtils;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 17.05.14
 * Time: 0:50
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class WordEditFragment extends Fragment
{
    EditText wordEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.word_edit_view, container, false);

        wordEdit = (EditText) v.findViewById(R.id.wordedit_word_entry);
        ImageButton sayBut = (ImageButton)v.findViewById(R.id.wordedit_say_but);
        sayBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String toSay = wordEdit.getText().toString().trim();
                doSay(toSay);
            }
        });
        return v;
    }

    protected boolean doSay(String text)
    {
        if(StringUtils.isNullOrEmpty(text))
        {
            return false;
        }

        TTSFactory.instance().saySomething(text);

        return true;
    }
}
