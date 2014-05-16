package northern.captain.app.WordsMemo.factory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.db.TagsDB;
import northern.captain.app.WordsMemo.db.SQLManager;
import northern.captain.app.WordsMemo.logic.Tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
