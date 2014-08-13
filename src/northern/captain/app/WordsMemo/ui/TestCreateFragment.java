package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TaskFactory;
import northern.captain.app.WordsMemo.factory.WordFactory;
import northern.captain.app.WordsMemo.logic.TagSet;
import northern.captain.app.WordsMemo.logic.Tasks;
import northern.captain.tools.MyToast;
import northern.captain.tools.SettingsNames;
import northern.captain.tools.StringUtils;

import java.util.List;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 17.05.14
 * Time: 0:50
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TestCreateFragment extends Fragment
{
    EditText testNameEdit;
    EditText tagsEdit;
    Spinner  langSpinner;
    ImageButton sayBut;
    TextView totalWordsLbl;

    TagSet wordTags = new TagSet();

    List<Long> wordsList;


    boolean needSay = true;


    public interface onOKListener
    {
        public void onOK(Tasks params);
        public void onContinue();
    }

    protected onOKListener okListener;

    public void setOkListener(onOKListener okListener)
    {
        this.okListener = okListener;
    }

    public TestCreateFragment(onOKListener listener)
    {
        okListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.test_create_view, container, false);

        testNameEdit = (EditText) v.findViewById(R.id.tcreate_name_entry);
        sayBut = (ImageButton) v.findViewById(R.id.tcreate_say_but);
        sayBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switchSayBut();
            }
        });

        Button okBut = (Button) v.findViewById(R.id.tcreate_ok_btn);
        okBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doOK();
            }
        });

        Button  continueBut = (Button) v.findViewById(R.id.tcreate_continue_btn);
        continueBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(okListener != null)
                    okListener.onContinue();
            }
        });

        Button  cancelBut = (Button) v.findViewById(R.id.tcreate_cancel_but);
        cancelBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().popBackStack();
            }
        });

        tagsEdit = (EditText) v.findViewById(R.id.tcreate_tags_entry);
        tagsEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openTagSelector();
            }
        });

        langSpinner = (Spinner) v.findViewById(R.id.tcreate_lang_spin);

        ImageButton tagBut = (ImageButton) v.findViewById(R.id.tcreate_tags_but);

        totalWordsLbl = (TextView) v.findViewById(R.id.tcreate_total_words);

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
        String userName = AndroidContext.current.settings.getString(SettingsNames.USER_NAME, "");
        testNameEdit.setText(userName);
        wordTags.clear();

        sayBut.setImageResource(needSay ? R.drawable.ic_action_volume_on : R.drawable.ic_action_volume_muted);

        setTagsList();
    }

    protected void setTagsList()
    {
        tagsEdit.setText(wordTags.toString());
        wordsList = WordFactory.instance().getIdsByTags(wordTags);
        totalWordsLbl.setText(Integer.toString(wordsList.size()));
    }

    protected void doOK()
    {
        if(okListener != null)
        {
            String name = this.testNameEdit.getText().toString();
            if(StringUtils.isNullOrEmpty(name.trim()))
            {
                MyToast.toast(MyToast.WARN, R.string.err_user_name, true);
                return;
            }

            AndroidContext.current.settings.setString(SettingsNames.USER_NAME, name);

            if(wordsList.isEmpty())
                return;

            Tasks params = TaskFactory.instance().newTask();
            params.setNeedTalk(needSay);
            params.setMainLang(langSpinner.getSelectedItem().toString());
            params.setTagSet(this.wordTags);
            params.setName(name);
            params.initUser(name);
            params.setInitialWordIdList(wordsList);
            okListener.onOK(params);
        }
        getFragmentManager().popBackStack();
    }

    protected void switchSayBut()
    {
        needSay = !needSay;
        sayBut.setImageResource(needSay ? R.drawable.ic_action_volume_on : R.drawable.ic_action_volume_muted);
    }

}
