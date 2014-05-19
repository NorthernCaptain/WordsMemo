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
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TTSFactory;
import northern.captain.app.WordsMemo.factory.WordFactory;
import northern.captain.app.WordsMemo.logic.Tags;
import northern.captain.app.WordsMemo.logic.Words;
import northern.captain.tools.StringUtils;

import java.util.HashSet;
import java.util.Set;

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
    EditText translationEdit;
    EditText thesaurusEdit;
    EditText tagsEdit;
    Spinner  langSpinner;
    Spinner  translationTextFmtSpinner;
    Spinner  thesaurusTextFmtSpinner;

    Set<Tags> wordTags = new HashSet<Tags>();

    public interface onOKListener
    {
        public void onOK(Words newWord);
    }

    protected onOKListener okListener;

    public void setOkListener(onOKListener okListener)
    {
        this.okListener = okListener;
    }

    public WordEditFragment(onOKListener listener)
    {
        okListener = listener;
    }

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

        Button okBut = (Button) v.findViewById(R.id.wordedit_ok_btn);
        okBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doOK();
                getFragmentManager().popBackStack();
            }
        });

        translationEdit = (EditText) v.findViewById(R.id.wordedit_trans_entry);
        thesaurusEdit = (EditText) v.findViewById(R.id.wordedit_thes_entry);
        tagsEdit = (EditText) v.findViewById(R.id.wordedit_tags_entry);

        langSpinner = (Spinner) v.findViewById(R.id.wordedit_lang_spin);
        thesaurusTextFmtSpinner = (Spinner) v.findViewById(R.id.wordedit_thes_type);
        translationTextFmtSpinner = (Spinner) v.findViewById(R.id.wordedit_trans_type);

        ImageButton tagBut = (ImageButton) v.findViewById(R.id.wordedit_tags_but);
        tagBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
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
        });

        setValues();

        return v;
    }

    protected void setValues()
    {
        setTagsList();
    }

    protected void setTagsList()
    {
        tagsEdit.setText(StringUtils.collectionToString(wordTags));
    }

    protected void doOK()
    {
        String wordText = wordEdit.getText().toString().trim();

        if(StringUtils.isNullOrEmpty(wordText))
            return;

        if(okListener != null)
        {
            Words word = WordFactory.instance().newWord();

            word.setName(wordText);
            word.setTranslation(translationEdit.getText().toString().trim());
            word.setThesaurus(thesaurusEdit.getText().toString().trim());

            word.setLang(langSpinner.getSelectedItem().toString());

            word.setFlagBit("Htm".equals(thesaurusTextFmtSpinner.getSelectedItem().toString()) ? Words.FLAG_THESAURUS_IN_HTML : 0);
            word.setFlagBit("Htm".equals(translationTextFmtSpinner.getSelectedItem().toString()) ? Words.FLAG_TRANSLATION_IN_HTML : 0);

            word.setTags(this.wordTags);

            okListener.onOK(word);
        }
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
