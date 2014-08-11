package northern.captain.app.WordsMemo.ui;

import android.net.sip.SipProfile;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.ExportImportFactory;
import northern.captain.tools.MyToast;
import northern.captain.tools.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leo on 11.08.2014.
 */
public class ExportXLSFragment extends Fragment
{
    EditText fnameText;
    EditText dirnameText;

    TextView resultText;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.export_excel, container, false);

        fnameText = (EditText)v.findViewById(R.id.export_name_entry);
        dirnameText = (EditText)v.findViewById(R.id.export_dir_entry);

        resultText = (TextView)v.findViewById(R.id.exportPLbl);

        Button doBut = (Button)v.findViewById(R.id.export_xls_btn);

        doBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doExport();
            }
        });

        initForm();
        return v;
    }

    protected void initForm()
    {
        StringBuilder fnameBuf = new StringBuilder("words_memo_");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy_MM_dd");
        fnameBuf.append(fmt.format(new Date()));
        fnameBuf.append(".xls");

        fnameText.setText(fnameBuf.toString());

        File extDir = Environment.getExternalStorageDirectory();
        File ourDir = new File(extDir, "WordsMemo");
        dirnameText.setText(ourDir.getAbsolutePath());
    }

    private File getOutFile()
    {
        String dirName = dirnameText.getText().toString().trim();
        if(StringUtils.isNullOrEmpty(dirName))
        {
            MyToast.toast(MyToast.WARN, R.string.err_wrong_dir_name, true);
            return null;
        }
        File ourDir = new File(dirName);
        ourDir.mkdirs();
        if(!ourDir.exists())
        {
            MyToast.toast(MyToast.WARN, R.string.err_wrong_dir_name, true);
            return null;
        }

        String fName = fnameText.getText().toString().trim();
        if(StringUtils.isNullOrEmpty(fName))
        {
            MyToast.toast(MyToast.WARN, R.string.err_wrong_file_name, true);
            return null;
        }

        File ourFile = new File(ourDir, fName);

        return ourFile;
    }

    protected void doExport()
    {
        File outputFile = getOutFile();

        if(outputFile == null)
            return;

        int ret = ExportImportFactory.instance().doExport(ExportImportFactory.TYPE_XLS, outputFile);

        resultText.setText("Done with code: " + ret);
    }
}
