package northern.captain.app.WordsMemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;
import northern.captain.app.WordsMemo.factory.TagFactory;
import northern.captain.app.WordsMemo.logic.Tags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: NorthernCaptain
 * Date: 29.10.13
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public class TagsSelectorDialogFragment extends DialogFragment
{
    public interface IOKCallback
    {
        public void onOK(TagsSelectorDialogFragment dlg);
    }

    private IOKCallback callback;

    private ListView tagListView;

    public TagsSelectorDialogFragment(Set<Tags> checkedTags, IOKCallback callback)
    {
        this.checkedTags = checkedTags;
        newCheckedTags.addAll(checkedTags);
        this.callback = callback;
    }

    List<Tags> items;

    Set<Tags> checkedTags;

    Set<Tags> newCheckedTags = new HashSet<Tags>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.tag_selector_dlg, container, false);
        tagListView = (ListView) v.findViewById(R.id.choose_tags_list);

        items = TagFactory.instance().getTags();

        Button button = (Button) v.findViewById(R.id.choose_tags_ok_btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doOK();
            }
        });

        button = (Button) v.findViewById(R.id.choose_tags_cancel_but);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        fillListView();

        getDialog().setTitle(R.string.tags_choose_tags);
        return v;
    }

    private void fillListView()
    {
        CustomListAdapter listAdapter = new CustomListAdapter(AndroidContext.current.app, R.layout.tags_choose_row);
        tagListView.setAdapter(listAdapter);
    }

    class CustomListAdapter extends ArrayAdapter<Tags>
    {

        public CustomListAdapter(Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount()
        {
            return items.size();
        }

        /* (non-Javadoc)
                         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
                         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;

            Tags item = items.get(position);

            if(v == null)
            {
                LayoutInflater vi = (LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.tags_choose_row, null);
            }

            CheckBox cbox = (CheckBox) v.findViewById(R.id.tags_choose_cb);
            cbox.setText(item.getName());
            cbox.setTag(item);
            cbox.setChecked(newCheckedTags.contains(item));

            cbox.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    CheckBox cbox = (CheckBox) view;
                    if(cbox.isChecked())
                    {
                        newCheckedTags.add((Tags)cbox.getTag());
                    } else
                    {
                        newCheckedTags.remove(cbox.getTag());
                    }
                }
            });

            return v;
        }

    }

    private void doOK()
    {
        checkedTags.clear();
        checkedTags.addAll(newCheckedTags);
        dismiss();
        if(callback != null)
            callback.onOK(this);
    }
}
