package northern.captain.app.WordsMemo.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.ExportImportFactory;
import northern.captain.tools.*;

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

    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.export_excel, container, false);

        fnameText = (EditText)v.findViewById(R.id.export_name_entry);
        dirnameText = (EditText)v.findViewById(R.id.export_dir_entry);

        resultText = (TextView)v.findViewById(R.id.exportPLbl);

        progressBar = (ProgressBar)v.findViewById(R.id.exportPBar);

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
        String ourDirPath = ourDir.getAbsolutePath();
        ourDirPath = AndroidContext.current.settings.getString(SettingsNames.EXPORT_PATH, ourDirPath);
        dirnameText.setText(ourDirPath);
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

        AndroidContext.current.settings.setString(SettingsNames.EXPORT_PATH, ourDir.getAbsolutePath());

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
        final File outputFile = getOutFile();

        if(outputFile == null)
            return;

        AsyncTask<Void, Integer, Integer> expTask = new AsyncTask<Void, Integer, Integer>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                resultText.setText("Exporting....");
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Integer doInBackground(Void... voids)
            {
                int ret = ExportImportFactory.instance().doExport(ExportImportFactory.TYPE_XLS, outputFile,
                        new IProgressUpdate()
                        {
                            @Override
                            public void updateProgress(int currentValue, int maxValue)
                            {
                                publishProgress(new Integer[]{currentValue, maxValue});
                            }
                        });
                return ret;
            }

            @Override
            protected void onPostExecute(Integer result)
            {
                super.onPostExecute(result);
                resultText.setText("Done, total records: " + result);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values)
            {
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
            }
        };

        expTask.execute();
    }
}
