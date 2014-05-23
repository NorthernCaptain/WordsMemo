package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.logic.TagSet;
import northern.captain.app.WordsMemo.logic.TrainingParams;
import northern.captain.app.WordsMemo.logic.TrainingSession;
import northern.captain.tools.StringUtils;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 17.05.14
 * Time: 0:50
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TestSetupFragment extends Fragment
{
    EditText testNameEdit;
    EditText tagsEdit;
    Spinner  langSpinner;
    ImageButton sayBut;

    TagSet wordTags = new TagSet();

    int testCount = 1;
    boolean needSay = true;


    public interface onOKListener
    {
        public void onOK(TrainingSession params);
    }

    protected onOKListener okListener;

    public void setOkListener(onOKListener okListener)
    {
        this.okListener = okListener;
    }

    public TestSetupFragment(onOKListener listener)
    {
        okListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.test_setup_view, container, false);

        testNameEdit = (EditText) v.findViewById(R.id.tsetup_name_entry);
        sayBut = (ImageButton) v.findViewById(R.id.tsetup_say_but);
        sayBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switchSayBut();
            }
        });

        Button okBut = (Button) v.findViewById(R.id.tsetup_ok_btn);
        okBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doOK();
                getFragmentManager().popBackStack();
            }
        });

        Button  cancelBut = (Button) v.findViewById(R.id.tsetup_cancel_but);
        cancelBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().popBackStack();
            }
        });

        tagsEdit = (EditText) v.findViewById(R.id.tsetup_tags_entry);
        tagsEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openTagSelector();
            }
        });

        langSpinner = (Spinner) v.findViewById(R.id.tsetup_lang_spin);

        ImageButton tagBut = (ImageButton) v.findViewById(R.id.tsetup_tags_but);
        tagBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openTagSelector();
            }
        });

        AndroidContext.current.app.getSupportActionBar().setSubtitle(R.string.test_setup_title);

        setValues();

        return v;
    }

    private void openTagSelector()
    {
        TagsSelectorDialogFragment dlg = FragmentFactory.instance().newTagSelectorFragment(wordTags,
                new TagsSelectorDialogFragment.IOKCallback()
                {
                    @Override
                    public void onOK(TagsSelectorDialogFragment dlg)
                    {
                        setTagsList();
                    }
                });
        dlg.show(getFragmentManager(), "tagsChoose");
    }

    protected void setValues()
    {
        testNameEdit.setText("Training-" + testCount++);
        wordTags.clear();

        sayBut.setImageResource(needSay ? R.drawable.ic_action_volume_on : R.drawable.ic_action_volume_muted);

        setTagsList();
    }

    protected void setTagsList()
    {
        tagsEdit.setText(wordTags.toString());
    }

    protected void doOK()
    {
        String wordText = testNameEdit.getText().toString().trim();

        if(StringUtils.isNullOrEmpty(wordText))
            return;

        if(okListener != null)
        {
            TrainingSession params = new TrainingSession(testNameEdit.getText().toString());
            params.setNeedTalk(needSay);
            params.setMainLang(langSpinner.getSelectedItem().toString());
            params.setTags(this.wordTags);
            okListener.onOK(params);
        }
    }

    protected void switchSayBut()
    {
        needSay = !needSay;
        sayBut.setImageResource(needSay ? R.drawable.ic_action_volume_on : R.drawable.ic_action_volume_muted);
    }

}
