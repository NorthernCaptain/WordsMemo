package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TTSFactory;
import northern.captain.app.WordsMemo.logic.TaskDetail;
import northern.captain.app.WordsMemo.logic.Tasks;
import northern.captain.app.WordsMemo.logic.Words;
import northern.captain.tools.StringUtils;


/**
 * Created by NorthernCaptain on 21.05.2014.
 */
public class TestingFragment extends Fragment
{
    protected TextView  wordView;
    protected TextView  transView;
    protected TextView transLbl;

    protected ImageButton sayBut;
    protected ImageButton translateOrThesaurusBut;
    protected ImageButton backBut;
    protected ImageButton nextBut;

    protected ProgressBar progressBar;
    protected RadioGroup chooseResultGrp;
    protected RadioButton choose1Button;
    protected RadioButton choose2Button;
    protected RadioButton choose3Button;
    protected RadioButton choose4Button;

    protected Tasks session;
    protected Words[] wordChoices = new Words[4];

    protected boolean isNameFirst = true;

    public interface OnDoneListener
    {
        public void onDone(Tasks session);
    }

    protected OnDoneListener onDoneListener;

    public TestingFragment(Tasks session, OnDoneListener listener)
    {
        this.session = session;
        onDoneListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.test_view, container, false);

        wordView = (TextView) v.findViewById(R.id.test_word_web);
        wordView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doNext();
            }
        });
        transLbl = (TextView) v.findViewById(R.id.test_translate_lbl);
        progressBar = (ProgressBar) v.findViewById(R.id.test_pbar);
        chooseResultGrp = (RadioGroup) v.findViewById(R.id.test_choose_grp);
        choose1Button = (RadioButton) v.findViewById(R.id.test_choose_1);
        choose2Button = (RadioButton) v.findViewById(R.id.test_choose_2);
        choose3Button = (RadioButton) v.findViewById(R.id.test_choose_3);
        choose4Button = (RadioButton) v.findViewById(R.id.test_choose_4);

        chooseResultGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                doChoice(i);
            }
        });

        sayBut = (ImageButton) v.findViewById(R.id.test_say_but);
        sayBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doSay(isNameFirst);
            }
        });
        translateOrThesaurusBut = (ImageButton)v.findViewById(R.id.test_trans_but);
        translateOrThesaurusBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setValues(session.getCurrentWord(), true);
            }
        });
        translateOrThesaurusBut.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                switchThesaurus();
                return true;
            }
        });
        translateOrThesaurusBut.setImageResource(session.isShowThesaurus() ? R.drawable.ic_action_web_site : R.drawable.ic_action_import_export);

        backBut = (ImageButton)v.findViewById(R.id.test_back_but);
        backBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doBack();
            }
        });
        nextBut = (ImageButton)v.findViewById(R.id.test_next_but);
        nextBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doNext();
            }
        });

//        transView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                setValues(session.getCurrentWord(), true);
//            }
//        });

//        View lay = v.findViewById(R.id.test_lay);
//        lay.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                setValues(session.getCurrentWord(), true);
//            }
//        });

        progressBar.setMax(session.getWordsTotal());

        setWord(session.getCurrentWord());

        AndroidContext.current.app.getSupportActionBar().setSubtitle(session.getName());
        return v;
    }

    private void doSay(boolean canSay)
    {
        Words word = session.getCurrentWord();
        if(word == null || !canSay)
            return;

        TTSFactory.instance().saySomething(word.getName());
    }

    private void switchThesaurus()
    {
        session.setShowThesaurus(!session.isShowThesaurus());
        translateOrThesaurusBut.setImageResource(session.isShowThesaurus() ? R.drawable.ic_action_web_site : R.drawable.ic_action_import_export);
        setValues(session.getCurrentWord(), true);
    }

    private void doBack()
    {

    }

    private void doChoice(int choice)
    {
        boolean isOk = false;
        final String currentName = session.getCurrentWord().getName();
        switch(choice)
        {
            case R.id.test_choose_1:
                isOk = wordChoices[0].getName().equals(currentName);
                break;
            case R.id.test_choose_2:
                isOk = wordChoices[1].getName().equals(currentName);
                break;
            case R.id.test_choose_3:
                isOk = wordChoices[2].getName().equals(currentName);
                break;
            case R.id.test_choose_4:
                isOk = wordChoices[3].getName().equals(currentName);
                break;
            default:
                return;
        }

        if(!isNameFirst && session.isNeedTalk())
            doSay(true);

        if(session.markWordPassed(isOk ? TaskDetail.STATUS_PASSED : TaskDetail.STATUS_ERROR,
                isOk ? TaskDetail.SCORE_PASSED : TaskDetail.SCORE_FAILED))
        {
            if(onDoneListener != null)
                onDoneListener.onDone(session);
            return;
        }

        Words word = session.nextWord();

        setWord(word);
    }

    private void doNext()
    {
        if(session.markWordPassed(TaskDetail.STATUS_ERROR, TaskDetail.SCORE_FAILED))
        {
            if(onDoneListener != null)
                onDoneListener.onDone(session);
            return;
        }

        Words word = session.nextWord();

        setWord(word);
    }

    private void setWord(Words word)
    {
        if(word == null)
        {
            session.start();
            word = session.nextWord();
        }

        isNameFirst = session.getMainLang().equals(word.getLang());

        setValues(word, false);

        if(session.isNeedTalk())
        {
            doSay(isNameFirst);
        }
    }

    private void setValues(Words word, boolean showTranslation)
    {
        if(word == null)
            return;

        progressBar.setProgress(session.getWordsPassed());
        wordView.setText(StringUtils.toTextHtmlH1(getName(word)));
        chooseResultGrp.clearCheck();

        int captionId;

        if(session.isShowThesaurus())
        {
            captionId = R.string.workedit_thesaurus;
        } else
        {
            captionId = R.string.workedit_translate;
        }

        this.transLbl.setText(captionId);


        wordChoices = new Words[wordChoices.length];

        wordChoices[Tasks.rnd.nextInt(wordChoices.length)] = word;

        for(int i = 0; i< wordChoices.length;i++)
        {
            if(wordChoices[i] == null)
                wordChoices[i] = session.getAnyWord(word);
        }


        choose1Button.setText(StringUtils.toTextHtml(getTranslation(wordChoices[0])));
        choose2Button.setText(StringUtils.toTextHtml(getTranslation(wordChoices[1])));
        choose3Button.setText(StringUtils.toTextHtml(getTranslation(wordChoices[2])));
        choose4Button.setText(StringUtils.toTextHtml(getTranslation(wordChoices[3])));
    }

    String getName(Words word)
    {
        return isNameFirst ? getNameInternal(word) : getTranslationInternal(word);
    }

    private String getNameInternal(Words word)
    {
        return word.getName();
    }

    String getTranslation(Words word)
    {
        return !isNameFirst ? getNameInternal(word) : getTranslationInternal(word);
    }

    private String getTranslationInternal(Words word)
    {
        String translation;
        int captionId;

        boolean isHtml = false;
        if(session.isShowThesaurus())
        {
            translation = word.getThesaurus();
            isHtml = word.isFlagSet(Words.FLAG_THESAURUS_IN_HTML);
            captionId = R.string.workedit_thesaurus;
            if(StringUtils.isNullOrEmpty(translation))
            {
                translation = word.getTranslation();
                isHtml = word.isFlagSet(Words.FLAG_TRANSLATION_IN_HTML);
                captionId = R.string.workedit_translate;
            }
        } else
        {
            translation = word.getTranslation();
            isHtml = word.isFlagSet(Words.FLAG_TRANSLATION_IN_HTML);
            captionId = R.string.workedit_translate;
            if(StringUtils.isNullOrEmpty(translation))
            {
                translation = word.getThesaurus();
                isHtml = word.isFlagSet(Words.FLAG_THESAURUS_IN_HTML);
                captionId = R.string.workedit_thesaurus;
            }
        }

        return translation;
    }
}
