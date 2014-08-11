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

/**
 * Created by leo on 11.08.2014.
 */
public class ImportXLSFragment extends Fragment
{
    EditText fnameText;
    EditText dirnameText;

    TextView resultText;

    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.import_excel, container, false);

        fnameText = (EditText)v.findViewById(R.id.import_name_entry);
        dirnameText = (EditText)v.findViewById(R.id.import_dir_entry);

        resultText = (TextView)v.findViewById(R.id.importPLbl);

        progressBar = (ProgressBar)v.findViewById(R.id.importPBar);

        Button doBut = (Button)v.findViewById(R.id.import_xls_btn);

        doBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doImport();
            }
        });

        initForm();
        return v;
    }

    protected void initForm()
    {
        fnameText.setText(AndroidContext.current.settings.getString(SettingsNames.IMPORT_FNAME, "words_memo.xls"));

        File extDir = Environment.getExternalStorageDirectory();
        File ourDir = new File(extDir, "WordsMemo");
        String ourDirPath = ourDir.getAbsolutePath();
        ourDirPath = AndroidContext.current.settings.getString(SettingsNames.IMPORT_PATH, ourDirPath);
        dirnameText.setText(ourDirPath);
    }

    private File getInputFile()
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

        AndroidContext.current.settings.setString(SettingsNames.IMPORT_PATH, ourDir.getAbsolutePath());

        String fName = fnameText.getText().toString().trim();
        if(StringUtils.isNullOrEmpty(fName))
        {
            MyToast.toast(MyToast.WARN, R.string.err_wrong_file_name, true);
            return null;
        }

        File ourFile = new File(ourDir, fName);

        if(!ourFile.exists())
        {
            MyToast.toast(MyToast.WARN, R.string.err_wrong_file_name, true);
            return null;
        }

        AndroidContext.current.settings.setString(SettingsNames.IMPORT_FNAME, fName);

        return ourFile;
    }

    protected void doImport()
    {
        final File inputFile = getInputFile();

        if(inputFile == null)
            return;

        AsyncTask<Void, Integer, Integer> impTask = new AsyncTask<Void, Integer, Integer>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressBar.setProgress(0);
                resultText.setText("Importing....");
            }

            @Override
            protected Integer doInBackground(Void... voids)
            {
                int ret = ExportImportFactory.instance().doImport(ExportImportFactory.TYPE_XLS, inputFile, new IProgressUpdate()
                {
                    @Override
                    public void updateProgress(int currentValue, int maxValue)
                    {
                        publishProgress(new Integer[]{ currentValue, maxValue});
                    }
                });
                return ret;
            }

            @Override
            protected void onPostExecute(Integer result)
            {
                super.onPostExecute(result);
                resultText.setText("Done, total entries: " + result);
            }

            @Override
            protected void onProgressUpdate(Integer... values)
            {
                super.onProgressUpdate(values);

                int max = values[1];
                int cur = values[0];

                if(progressBar.getMax() != max)
                    progressBar.setMax(max);
                progressBar.setProgress(cur);
            }
        };

        impTask.execute();
    }
}
