package northern.captain.app.WordsMemo.factory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.db.TagsDB;
import northern.captain.app.WordsMemo.db.SQLManager;
import northern.captain.app.WordsMemo.logic.TagSet;
import northern.captain.app.WordsMemo.logic.Tags;
import northern.captain.tools.StringUtils;

import java.util.*;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 28.10.13
 * Time: 23:35
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TagFactory
{
    protected static TagFactory singleton;

    public static TagFactory instance()
    {
        return singleton;
    }

    public static void initialize()
    {
        singleton = new TagFactory();
    }

    protected TagFactory()
    {

    }

    public Tags newTag()
    {
        return new TagsDB();
    }


    public TagSet getTagsIn(Set<Integer> tagIds)
    {
        TagSet tagsList = new TagSet();

        if(tagIds.size() == 0)
            return tagsList;

        SQLiteDatabase db = SQLManager.instance().dbr();

        String query = "SELECT * from " + TagsDB.TBL_TAGS + " where id in ("
                + StringUtils.collectionToString(tagIds) + ")";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                TagsDB tagsDB = new TagsDB();
                tagsDB.read(cursor);
                tagsList.add(tagsDB);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return tagsList;
    }


    public List<Tags> getTags()
    {
        List<Tags> tagsList = new ArrayList<Tags>();

        SQLiteDatabase db = SQLManager.instance().dbr();

        String query = "SELECT * from " + TagsDB.TBL_TAGS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                TagsDB tagsDB = new TagsDB();
                tagsDB.read(cursor);
                tagsList.add(tagsDB);
            } while(cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(tagsList, new Comparator<Tags>()
        {
            @Override
            public int compare(Tags tags, Tags tags2)
            {
                return tags.getName().compareTo(tags2.getName());
            }
        });

        return tagsList;
    }

    public Map<String, Tags> getTagsMap()
    {
        Map<String, Tags> tagMap = new HashMap<String, Tags>();
        List<Tags> allTags = getTags();

        for(Tags tag : allTags)
        {
            tagMap.put(tag.getName(), tag);
        }

        return tagMap;
    }

    public TagSet getTagsFromStringNames(Map<String, Tags> allTags, String tagNames)
    {
        if(StringUtils.isNullOrEmpty(tagNames))
            return null;

        String[] names = tagNames.split(",");

        if(names.length == 0)
            return null;

        TagSet tset = new TagSet();
        for(String name : names)
        {
            name = name.trim();
            if(StringUtils.isNullOrEmpty(name))
                continue;

            Tags tag = allTags.get(name);
            if(tag == null)
            {
                tag = TagFactory.instance().newTag();
                tag.setName(name);
                TagFactory.instance().add(tag);
                allTags.put(name, tag);
            }
            tset.add(tag);
        }

        return tset;
    }

    public void add(Tags account)
    {
        ((TagsDB)account).insert();
    }

    public void update(Tags account)
    {
        ((TagsDB)account).update();
    }

    public void delete(Tags account)
    {
        ((TagsDB)account).delete();
    }
}
