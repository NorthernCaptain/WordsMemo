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
    TextView translationLbl;

    TagSet wordTags = new TagSet();

    Words current;
    ImageButton changeTransBut;
    ImageButton transBut;
    ImageButton previewBut;

    FrameLayout frameLay;
    LinearLayout webLinearLay;
    TextView     webView;


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

        Button addNew = (Button) v.findViewById(R.id.wordedit_ok_and_new);
        if(current != null)
        {
            addNew.setEnabled(false);
        }
        else
        {
            addNew.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    doOK();
                    clearAll();
                    wordEdit.requestFocus();
                }
            });
        }

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

        previewBut = (ImageButton) v.findViewById(R.id.wordedit_preview_but);
        previewBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showPreviewMode(!previewMode);
            }
        });

        webView = (TextView) v.findViewById(R.id.wordedit_translate_web);
        webLinearLay = (LinearLayout) v.findViewById(R.id.wordedit_web_lay);
        frameLay = (FrameLayout) v.findViewById(R.id.wordedit_frame_lay);

        setValues();

        return v;
    }

    String translationText;
    int translationFmtIdx;

    String thesaurusText;
    int thesaurusFmtIdx;

    boolean previewMode = false;

    private void askThesaurus()
    {
        String word = wordEdit.getText().toString().trim();
        if(StringUtils.isNullOrEmpty(word))
            return;

        String definition = ThesaurusRetrieverFactory.instance().getThesaurus().getDefinition(word);

        if(!StringUtils.isNullOrEmpty(definition))
        {
            thesaurusText = definition;
            thesaurusFmtIdx = 1;

            if(inTransMode)
            {
                changeTransMode();
            } else
            {
                showTransMode();
            }
            showPreviewMode(true);
        }
    }

    private void showPreviewMode(boolean preview)
    {
        if(previewMode != preview)
        {
            previewMode = preview;
        }

        if(previewMode)
        {
            frameLay.bringChildToFront(webLinearLay);
            previewBut.setImageResource(R.drawable.ic_action_view_as_list);
            webView.setText(StringUtils.toTextHtmlH2(translationEdit.getText().toString()));
        } else
        {
            frameLay.bringChildToFront(translationEdit);
            previewBut.setImageResource(R.drawable.ic_action_edit);
        }

        frameLay.requestLayout();
        frameLay.invalidate();
        previewBut.setImageResource(previewMode ? R.drawable.ic_action_view_as_list : R.drawable.ic_action_edit);
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
            translationLbl.setText(R.string.workedit_translate);
            changeTransBut.setImageResource(R.drawable.ic_action_import_export);
        } else
        {
            translationEdit.setText(thesaurusText);
            translationLbl.setText(R.string.workedit_thesaurus);
            changeTransBut.setImageResource(R.drawable.ic_action_web_site);
        }
    }

    private void changeTransMode()
    {
        if(inTransMode)
        {
            translationText = this.translationEdit.getText().toString();
        }
        else
        {
            thesaurusText = this.translationEdit.getText().toString();
        }

        inTransMode = !inTransMode;
        showTransMode();
    }

    protected  void clearAll()
    {
        wordEdit.setText("");
        thesaurusText = "";
        translationText = "";
        thesaurusFmtIdx = 0;
        translationFmtIdx = 0;
        showTransMode();
        setTagsList();
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

            thesaurusFmtIdx = current.isFlagSet(Words.FLAG_THESAURUS_IN_HTML) ? 1 : 0;
            translationFmtIdx = current.isFlagSet(Words.FLAG_TRANSLATION_IN_HTML) ? 1 : 0;
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
            } else
            {
                thesaurusText = translationEdit.getText().toString();
            }

            Words word = current != null ? current : WordFactory.instance().newWord();

            word.setName(wordText);
            word.setTranslation(translationText);
            word.setThesaurus(thesaurusText);

            word.setLang(langSpinner.getSelectedItem().toString());

            word.setFlagBit(thesaurusFmtIdx > 0 ? Words.FLAG_THESAURUS_IN_HTML : 0);
            word.setFlagBit(translationFmtIdx > 0 ? Words.FLAG_TRANSLATION_IN_HTML : 0);

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
