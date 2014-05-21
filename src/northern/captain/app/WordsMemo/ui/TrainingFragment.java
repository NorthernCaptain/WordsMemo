package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TTSFactory;
import northern.captain.app.WordsMemo.logic.TrainingSession;
import northern.captain.app.WordsMemo.logic.Words;
import northern.captain.tools.StringUtils;


/**
 * Created by NorthernCaptain on 21.05.2014.
 */
public class TrainingFragment extends Fragment
{
    protected WebView  wordView;
    protected WebView  transView;
    protected TextView transLbl;

    protected ImageButton sayBut;
    protected ImageButton translateOrThesaurusBut;
    protected ImageButton backBut;
    protected ImageButton nextBut;

    protected TrainingSession session;

    public TrainingFragment(TrainingSession session)
    {
        this.session = session;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.training_view, container, false);

        wordView = (WebView) v.findViewById(R.id.training_word_web);
        transView = (WebView) v.findViewById(R.id.training_translate_web);
        transLbl = (TextView) v.findViewById(R.id.training_translate_lbl);

        sayBut = (ImageButton) v.findViewById(R.id.training_say_but);
        sayBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doSay();
            }
        });
        translateOrThesaurusBut = (ImageButton)v.findViewById(R.id.training_trans_but);
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

        backBut = (ImageButton)v.findViewById(R.id.training_back_but);
        backBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doBack();
            }
        });
        nextBut = (ImageButton)v.findViewById(R.id.training_next_but);
        nextBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doNext();
            }
        });

        transView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setValues(session.getCurrentWord(), true);
            }
        });

        session.init();
        doNext();

        AndroidContext.current.app.getSupportActionBar().setSubtitle(session.getName());
        return v;
    }

    private void doSay()
    {
        Words word = session.getCurrentWord();
        if(word == null)
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

    private void doNext()
    {
        Words word = session.nextWord();

        if(word == null)
        {
            session.init();
            word = session.nextWord();
        }

        setValues(word, false);

        if(session.isNeedTalk())
        {
            doSay();
        }
    }

    private void setValues(Words word, boolean showTranslation)
    {
        if(word == null)
            return;

        wordView.loadDataWithBaseURL(null, StringUtils.toHtmlH1(word.getName()), "text/html", "UTF-8", null);

        String translation;
        int captionId;

        if(!showTranslation)
        {
            transView.loadDataWithBaseURL(null, StringUtils.toHtmlH2(""), "text/html", "UTF-8", null);
            return;
        }

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

        this.transLbl.setText(captionId);

        transView.loadDataWithBaseURL(null, StringUtils.toHtmlH2(translation), "text/html", "UTF-8", null);
    }
}
