package northern.captain.app.WordsMemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.logic.Words;

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

        id = db.insert(TBL_WORDS, null, values);

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
        values.put(DBF_MODIFIED, SQLManager.getCDate());

        db.update(TBL_WORDS, values, DBF_ID + " = ?", new String[] { String.valueOf(id)});
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
    }

    @Override
    public int incrementUsage()
    {
        return incrementUsage();
    }
}