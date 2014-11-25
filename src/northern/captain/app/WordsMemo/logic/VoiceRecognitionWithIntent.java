package northern.captain.app.WordsMemo.logic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.tools.IActivityResultCallback;

import java.util.List;

/**
 * Created by leo on 25.11.2014.
 */
public class VoiceRecognitionWithIntent extends VoiceRecognitionProcessor
{
    private static final int REQUEST_CODE=49875;

    @Override
    public void startVoiceRecognition(IVoiceCallback iVoiceCallback, String phrase)
    {
        setCallback(iVoiceCallback);

        if(!isAvailable())
        {
            AndroidContext.current.mainHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if(callback != null)
                    {
                        callback.onResultFail();
                    }
                }
            });
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, phrase != null ? "Say '" + phrase + "'" : "Say something!");
        AndroidContext.current.app.addOnActivityCallback(REQUEST_CODE, new IActivityResultCallback()
        {
            @Override
            public boolean onActivityResult(int requestCode, int resultCode, Intent data)
            {
                processResult(resultCode, data);
                return false;
            }
        });
        AndroidContext.current.app.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean isAvailable()
    {
        if(isServiceAvailable == null)
        {
            PackageManager pm = AndroidContext.current.app.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(
                    new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            isServiceAvailable = activities.size() > 0;
        }

        return isServiceAvailable;
    }
    private void processResult(int retCode, Intent data)
    {
        if(callback == null)
            return;

        List<String> matches = null;
        if( retCode == Activity.RESULT_OK )
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        }

        if( matches == null || matches.isEmpty() )
        {
            callback.onResultFail();
        }
        else
        {
            callback.onResultOK(matches);
        }
    }
}
