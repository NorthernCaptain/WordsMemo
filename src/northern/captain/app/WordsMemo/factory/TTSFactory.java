package northern.captain.app.WordsMemo.factory;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.tools.StringUtils;

import java.util.Locale;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 17.05.14
 * Time: 16:15
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TTSFactory implements TextToSpeech.OnInitListener
{
    protected static TTSFactory singleton;

    public static TTSFactory instance()
    {
        return singleton;
    }

    public static void initialize()
    {
        singleton = new TTSFactory();
    }


    protected TextToSpeech textToSpeech;

    public void initEngine()
    {
        textToSpeech = new TextToSpeech(AndroidContext.current.app, this);
    }

    public void saySomething(String text)
    {
        if(!isEnabled || StringUtils.isNullOrEmpty(text))
            return;

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void sayLater(String text)
    {
        if(!isEnabled || StringUtils.isNullOrEmpty(text))
            return;

        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    protected boolean isEnabled = false;

    @Override
    public void onInit(int status)
    {
        isEnabled = false;
        if (status == TextToSpeech.SUCCESS)
        {
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("wmemoTTS", "This Language is not supported");
            } else
            {
                isEnabled = true;
                Log.e("wmemoTTS", "TTS fully enabled for US language");
            }

        } else
        {
            Log.e("wmemoTTS", "Initilization Failed!");
        }
    }

    public void shutdown()
    {
        isEnabled = false;
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

}
