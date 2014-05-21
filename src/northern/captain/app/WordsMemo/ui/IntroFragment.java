package northern.captain.app.WordsMemo.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;

/**
 * Created with IntelliJ IDEA.
 * User: NorthernCaptain
 * Date: 29.10.13
 * Time: 12:35
 * To change this template use File | Settings | File Templates.
 */
public class IntroFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.intro, container, false);

        TextView verLbl = (TextView) v.findViewById(R.id.version_lbl);
        String versionName = "0.0.0";
        try
        {
            versionName = AndroidContext.current.app.getPackageManager()
                    .getPackageInfo(AndroidContext.current.app.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
        }
        verLbl.setText(versionName);
        return v;
    }
}
