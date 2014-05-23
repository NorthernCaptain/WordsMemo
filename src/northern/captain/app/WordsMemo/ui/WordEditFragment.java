package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TTSFactory;
import northern.captain.app.WordsMemo.factory.WordFactory;
import northern.captain.app.WordsMemo.logic.TagSet;
import northern.captain.app.WordsMemo.logic.ThesaurusRetrieverFactory;
import northern.captain.app.WordsMemo.logic.Words;
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
    EditText translationEdit;
    EditText tagsEdit;
    Spinner  langSpinner;
    Spinner  translationTextFmtSpinner;
    TextView translationLbl;

    TagSet wordTags = new TagSet();

    Words current;
    ImageButton changeTransBut;
    ImageButton transBut;

    boolean inTransMode = true;

    public interface onOKListener
    {
        public void onOK(Words newWord, boolean editMode);
    }

    protected onOKListener okListener;

    public void setOkListener(onOKListener okListener)
    {
        this.okListener = okListener;
    }

    public WordEditFragment(onOKListener listener, Words words)
    {
        okListener = listener;
        current = words;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.word_edit_view, container, false);

        wordEdit = (EditText) v.findViewById(R.id.wordedit_word_entry);
        ImageButton sayBut = (ImageButton) v.findViewById(R.id.wordedit_say_but);
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

        Button  cancelBut = (Button) v.findViewById(R.id.wordedit_cancel_but);
        cancelBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().popBackStack();
            }
        });

        translationEdit = (EditText) v.findViewById(R.id.wordedit_trans_entry);
        tagsEdit = (EditText) v.findViewById(R.id.wordedit_tags_entry);
        tagsEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openTagSelector();
            }
        });

        langSpinner = (Spinner) v.findViewById(R.id.wordedit_lang_spin);
        translationTextFmtSpinner = (Spinner) v.findViewById(R.id.wordedit_trans_type);

        changeTransBut = (ImageButton) v.findViewById(R.id.wordedit_change_trans_but);
        changeTransBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeTransMode();
            }
        });

        changeTransBut.setImageResource(inTransMode ? R.drawable.ic_action_import_export : R.drawable.ic_action_web_site);

        translationLbl = (TextView) v.findViewById(R.id.wordedit_trans_lbl);

        ImageButton tagBut = (ImageButton) v.findViewById(R.id.wordedit_tags_but);
        tagBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openTagSelector();
            }
        });

        transBut = (ImageButton) v.findViewById(R.id.wordedit_trans_but);

        transBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                askThesaurus();
            }
        });

        setValues();

        return v;
    }

    String translationText;
    int translationFmtIdx;

    String thesaurusText;
    int thesaurusFmtIdx;


    private void askThesaurus()
    {
        String word = wordEdit.getText().toString().trim();
        if(StringUtils.isNullOrEmpty(word))
            return;

        String definition = ThesaurusRetrieverFactory.instance().getThesaurus().getDefinition(word);

        if(!StringUtils.isNullOrEmpty(definition))
        {
            thesaurusText = definition;
            if(inTransMode)
            {
                changeTransMode();
            } else
            {
                showTransMode();
            }
        }
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

    private void showTransMode()
    {
        if(inTransMode)
        {
            translationEdit.setText(translationText);
            translationTextFmtSpinner.setSelection(translationFmtIdx);

            translationLbl.setText(R.string.workedit_translate);
            changeTransBut.setImageResource(R.drawable.ic_action_import_export);
        } else
        {
            translationEdit.setText(thesaurusText);
            translationTextFmtSpinner.setSelection(thesaurusFmtIdx);

            translationLbl.setText(R.string.workedit_thesaurus);
            changeTransBut.setImageResource(R.drawable.ic_action_web_site);
        }
    }

    private void changeTransMode()
    {
        if(inTransMode)
        {
            translationText = this.translationEdit.getText().toString();
            translationFmtIdx = translationTextFmtSpinner.getSelectedItemPosition();

        }
        else
        {
            thesaurusText = this.translationEdit.getText().toString();
            thesaurusFmtIdx = translationTextFmtSpinner.getSelectedItemPosition();

        }

        inTransMode = !inTransMode;
        showTransMode();
    }

    protected void setValues()
    {
        if(current != null)
        {
            wordEdit.setText(current.getName());
            {
                SpinnerAdapter adapter = langSpinner.getAdapter();
                for (int i = 0; i < adapter.getCount();i++)
                {
                    if(current.getLang().equals(adapter.getItem(i).toString()))
                    {
                        langSpinner.setSelection(i);
                        break;
                    }
                }
            }

            {
                SpinnerAdapter adapter = translationTextFmtSpinner.getAdapter();
                String fmt = current.isFlagSet(Words.FLAG_THESAURUS_IN_HTML) ? Words.HTML_FMT : Words.TEXT_FMT;
                for (int i = 0; i < adapter.getCount();i++)
                {
                    if(fmt.equals(adapter.getItem(i).toString()))
                    {
                        thesaurusFmtIdx = i;
                        break;
                    }
                }
            }

            {
                SpinnerAdapter adapter = translationTextFmtSpinner.getAdapter();
                String fmt = current.isFlagSet(Words.FLAG_TRANSLATION_IN_HTML) ? Words.HTML_FMT : Words.TEXT_FMT;
                for (int i = 0; i < adapter.getCount();i++)
                {
                    if(fmt.equals(adapter.getItem(i).toString()))
                    {
                        translationFmtIdx = i;
                        break;
                    }
                }
            }
            translationText = current.getTranslation();
            thesaurusText = current.getThesaurus();

            showTransMode();

            wordTags.clear();
            wordTags.addAll(current.getTags());
        }
        setTagsList();
    }

    protected void setTagsList()
    {
        tagsEdit.setText(wordTags.toString());
    }

    protected void doOK()
    {
        String wordText = wordEdit.getText().toString().trim();

        if(StringUtils.isNullOrEmpty(wordText))
            return;

        if(okListener != null)
        {
            if(inTransMode)
            {
                translationText = translationEdit.getText().toString();
                translationFmtIdx = translationTextFmtSpinner.getSelectedItemPosition();
            } else
            {
                thesaurusText = translationEdit.getText().toString();
                thesaurusFmtIdx = translationTextFmtSpinner.getSelectedItemPosition();
            }

            Words word = current != null ? current : WordFactory.instance().newWord();

            word.setName(wordText);
            word.setTranslation(translationText);
            word.setThesaurus(thesaurusText);

            word.setLang(langSpinner.getSelectedItem().toString());

            SpinnerAdapter adapter = translationTextFmtSpinner.getAdapter();

            word.setFlagBit(Words.HTML_FMT.equals(adapter.getItem(thesaurusFmtIdx).toString()) ? Words.FLAG_THESAURUS_IN_HTML : 0);
            word.setFlagBit(Words.HTML_FMT.equals(adapter.getItem(translationFmtIdx).toString()) ? Words.FLAG_TRANSLATION_IN_HTML : 0);

            word.setTags(this.wordTags);

            okListener.onOK(word, word == current);
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
