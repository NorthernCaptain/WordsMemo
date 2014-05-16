package northern.captain.app.WordsMemo.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TagFactory;
import northern.captain.app.WordsMemo.logic.Tags;

/**
 * Created with IntelliJ IDEA.
 * User: NorthernCaptain
 * Date: 29.10.13
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public class TagsEditDialogFragment extends DialogFragment
{
    public interface IEditCallback
    {
        public void tagCreated(Tags account);
        public void tagChanged(Tags account);
    }

    private IEditCallback callback;

    public TagsEditDialogFragment(IEditCallback callback)
    {
        this.callback = callback;
    }

    public TagsEditDialogFragment(Tags tags1, IEditCallback callback)
    {
        this.callback = callback;
        this.tags = tags1;
    }

    Tags tags;

    EditText titleEdit;
    EditText descrEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.account_edit_dlg, container, false);
        titleEdit = (EditText) v.findViewById(R.id.accdlg_title_val);
        descrEdit = (EditText) v.findViewById(R.id.accdlg_desc_val);

        if (tags != null)
        {
            titleEdit.setText(tags.getName());
            descrEdit.setText(tags.getComments());

            getDialog().setTitle(R.string.accdlg_edit_title);
        } else
        {
            getDialog().setTitle(R.string.accdlg_add_title);
        }

        Button button = (Button) v.findViewById(R.id.accdlg_ok_btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doOK();
            }
        });

        button = (Button) v.findViewById(R.id.accdlg_cancel_but);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return v;
    }

    private void doOK()
    {
        boolean isNew = tags == null;
        if(isNew)
        {
            tags = TagFactory.instance().newTag();
        }

        tags.setName(titleEdit.getText().toString());
        tags.setComments(descrEdit.getText().toString());

        if(isNew)
        {
            TagFactory.instance().add(tags);
            if(callback != null)
            {
                callback.tagChanged(tags);
            }
        } else
        {
            TagFactory.instance().update(tags);
            if(callback != null)
            {
                callback.tagCreated(tags);
            }
        }

        dismiss();
    }
}
