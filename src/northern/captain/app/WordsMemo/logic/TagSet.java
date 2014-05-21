package northern.captain.app.WordsMemo.logic;

import northern.captain.app.WordsMemo.factory.TagFactory;
import northern.captain.tools.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by NorthernCaptain on 20.05.2014.
 */
public class TagSet extends HashSet<Tags>
{
    /**
     * Return string of ids of tags from this set.
     * @return JSON array as string
     */
    public String id2String()
    {
        JSONArray jar = new JSONArray();
        for(Tags tag : this)
        {
            jar.put(tag.getId());
        }
        return jar.toString();
    }

    public String id2SQL()
    {
        StringBuilder buf = new StringBuilder();
        int count = 0;
        int siz = size();
        for(Tags tag : this)
        {
            buf.append(tag.getId());
            count++;
            if(count < siz)
            {
                buf.append(',');
            }
        }

        return buf.toString();
    }

    /**
     * Converts JSON array of ids into Tags and fills this set.
     * @param json
     */
    public void string2Id(String json)
    {
        try
        {
            clear();
            JSONArray jar = new JSONArray(json);
            Set<Integer> ids = new HashSet<Integer>();
            for(int i=0;i<jar.length();i++)
            {
                ids.add(jar.getInt(i));
            }

            addAll(TagFactory.instance().getTagsIn(ids));
        }
        catch(JSONException jex) {}
    }


    @Override
    public String toString()
    {
        return StringUtils.collectionToString(this);
    }
}
