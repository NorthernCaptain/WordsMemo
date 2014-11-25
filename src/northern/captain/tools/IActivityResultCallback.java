package northern.captain.tools;

import android.content.Intent;

/**
 * Created by leo on 25.11.2014.
 */
public interface IActivityResultCallback
{
    /**
     * Calls when activity receives onActivityResult for the registered request code
     * @param requestCode
     * @param resultCode
     * @param data
     * @return true if you need to leave callback for further calls
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data);
}
