package northern.captain.app.WordsMemo.logic;

import java.util.List;

/**
 * Created by leo on 25.11.2014.
 */
public abstract class VoiceRecognitionProcessor
{
    public interface IVoiceCallback
    {
        public void onResultOK(List<String> words);
        public void onResultFail();
    }

    protected IVoiceCallback callback;

    public void setCallback(IVoiceCallback callback)
    {
        this.callback = callback;
    }

    protected Boolean isServiceAvailable;

    public abstract void startVoiceRecognition(IVoiceCallback callback, String phrase);
    public abstract boolean isAvailable();
}
