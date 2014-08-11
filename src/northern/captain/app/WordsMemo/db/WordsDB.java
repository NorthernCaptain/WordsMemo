package northern.captain.app.WordsMemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.factory.TagFactory;
import northern.captain.app.WordsMemo.logic.TagSet;
import northern.captain.app.WordsMemo.logic.Tags;
import northern.captain.app.WordsMemo.logic.Words;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 28.10.13
 * Time: 23:40
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class WordsDB extends Words implements ICRUD
{
    public static final String TBL_WORDS = "words";
    public static final String DBF_ID       = "id";
    public static final String DBF_NAME     = "name";
    public static final String DBF_THESAURUS = "thesaurus";
    public static final String DBF_TRANSLATION = "translation";
    public static final String DBF_USED_TIMES = "used_times";
    public static final String DBF_STATUS = "status";
    public static final String DBF_MODIFIED = "modified";
    public static final String DBF_FLAGS = "flags";
    public static final String DBF_LANG = "lang";
    public static final String DBF_TRANS_LANG = "trans_lang";

    public static final String TBL_WORDTAGS = "word_tags";
    public static final String DBF_WORDTAGS_WORD_ID = "word_id";
    public static final String DBF_WORDTAGS_TAG_ID = "tag_id";

    @Override
    public long insert()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values = new ContentValues();

        values.put(DBF_NAME, name);
        values.put(DBF_THESAURUS, thesaurus);
        values.put(DBF_TRANSLATION, translation);
        values.put(DBF_STATUS, status);
        values.put(DBF_MODIFIED, SQLManager.getCDate());
        values.put(DBF_FLAGS, flags);
        values.put(DBF_TRANS_LANG, transLang);
        values.put(DBF_LANG, lang);

        id = db.insert(TBL_WORDS, null, values);

        if(tags != null)
        {
            setTags(tags); //save the tags
        }

        return id;
    }

    @Override
    public void update()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values = new ContentValues();

        values.put(DBF_NAME, name);
        values.put(DBF_THESAURUS, thesaurus);
        values.put(DBF_TRANSLATION, translation);
        values.put(DBF_STATUS, status);
        values.put(DBF_USED_TIMES, usedTimes);
        values.put(DBF_FLAGS, flags);
        values.put(DBF_MODIFIED, SQLManager.getCDate());
        values.put(DBF_LANG, lang);
        values.put(DBF_TRANS_LANG, transLang);

        db.update(TBL_WORDS, values, DBF_ID + " = ?", new String[] { String.valueOf(id)});

        setTags(tags);
    }

    @Override
    public void delete()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        db.delete(TBL_WORDS, DBF_ID + " = ?", new String[] { String.valueOf(id)});
    }


    @Override
    public void read(Cursor cur)
    {
        id = cur.getLong(cur.getColumnIndex(DBF_ID));
        name = cur.getString(cur.getColumnIndex(DBF_NAME));
        thesaurus = cur.getString(cur.getColumnIndex(DBF_THESAURUS));
        translation = cur.getString(cur.getColumnIndex(DBF_TRANSLATION));
        usedTimes = cur.getInt(cur.getColumnIndex(DBF_USED_TIMES));
        status = cur.getInt(cur.getColumnIndex(DBF_STATUS));
        flags = cur.getInt(cur.getColumnIndex(DBF_FLAGS));
        lang = cur.getString(cur.getColumnIndex(DBF_LANG));
        transLang = cur.getString(cur.getColumnIndex(DBF_TRANS_LANG));
    }

    @Override
    public int incrementUsage()
    {
        return incrementUsage();
    }

    @Override
    public void setTags(TagSet tags)
    {
        this.tags = tags;

        if(id == 0) //not initialized
            return;

        SQLiteDatabase db = SQLManager.instance().db();
        db.delete(TBL_WORDTAGS, DBF_WORDTAGS_WORD_ID + " = ?", new String[] { String.valueOf(id)});

        if(tags == null)
            return;

        for(Tags tag : tags)
        {
            ContentValues values = new ContentValues();

            values.put(DBF_WORDTAGS_WORD_ID, id);
            values.put(DBF_WORDTAGS_TAG_ID, tag.getId());

            db.insert(TBL_WORDTAGS, null, values);
        }
    }

    public Set<Integer> getTagIds()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        String query = "SELECT tag_id from " + TBL_WORDTAGS + " where word_id=" + id;

        Set<Integer> tagIds = new HashSet<Integer>();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Integer val = cursor.getInt(0);
                tagIds.add(val);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return tagIds;
    }

    public TagSet getTags()
    {
        if(tags != null)
            return tags;

        Set<Integer> tagIds = getTagIds();
        tags = TagFactory.instance().getTagsIn(tagIds);
        return tags;
    }
}
