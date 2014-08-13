package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.logic.Tasks;
import northern.captain.tools.StringUtils;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 13.08.14
 * Time: 16:43
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TestResultFragment extends Fragment
{
    protected Tasks session;

    protected View.OnClickListener onClickListener;

    public TestResultFragment(Tasks session, View.OnClickListener onOKClicked)
    {
        this.session = session;
        onClickListener = onOKClicked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.test_result_view, container, false);

        {
            TextView textView = (TextView) v.findViewById(R.id.result_score);
            textView.setText(Integer.toString(session.getScore()));
        }
        {
            TextView textView = (TextView) v.findViewById(R.id.result_tags);
            textView.setText(session.getTagSet().toString());
        }
        {
            TextView textView = (TextView) v.findViewById(R.id.result_words);
            textView.setText(Integer.toString(session.getWordsPassed()));
        }
        {
            TextView textView = (TextView) v.findViewById(R.id.result_error);
            textView.setText(session.getWordsErrorString());
            textView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AndroidContext.current.drawer.openFragment(FragmentFactory.instance().newWordErrorsForTaskFragment(session));
                }
            });
        }
        {
            TextView textView = (TextView) v.findViewById(R.id.result_user);
            textView.setText(session.getUser().getName());
        }
        {
            TextView textView = (TextView) v.findViewById(R.id.result_start_date);
            textView.setText(StringUtils.formatDateTime(session.getStartDateDate()));
        }
        {
            TextView textView = (TextView) v.findViewById(R.id.result_finish_date);
            textView.setText(StringUtils.formatDateTime(session.getFinishDateDate()));
        }

        {
            TextView textView = (TextView) v.findViewById(R.id.result_spent);
            textView.setText(StringUtils.formatTimeSpent(session.getTimeSpentSec()));
        }

        Button okBut = (Button) v.findViewById(R.id.result_ok_btn);
        okBut.setOnClickListener(onClickListener);

        return v;
    }
}
