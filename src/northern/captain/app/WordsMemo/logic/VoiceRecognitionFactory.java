package northern.captain.app.WordsMemo.logic;

/**
 * Created by leo on 25.11.2014.
 */
public class VoiceRecognitionFactory
{
    protected static VoiceRecognitionFactory singleton = new VoiceRecognitionFactory();


    protected VoiceRecognitionProcessor current = new VoiceRecognitionWithIntent();

    public static VoiceRecognitionFactory instance()
    {
        return singleton;
    }

    public VoiceRecognitionProcessor getVoiceRec()
    {
        return current;
    }

}
