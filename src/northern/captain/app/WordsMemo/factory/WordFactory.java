package northern.captain.app.WordsMemo.factory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.db.SQLManager;
import northern.captain.app.WordsMemo.db.WordsDB;
import northern.captain.app.WordsMemo.logic.Words;

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
public class WordFactory
{
    protected static WordFactory singleton;

    public static WordFactory instance()
    {
        return singleton;
    }

    public static void initialize()
    {
        singleton = new WordFactory();
    }

    protected WordFactory()
    {

    }

    public Words newWord()
    {
        return new WordsDB();
    }


    public List<Words> getWords()
    {
        List<Words> wordsList = new ArrayList<Words>();

        SQLiteDatabase db = SQLManager.instance().dbr();

        String query = "SELECT * from " + WordsDB.TBL_WORDS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                WordsDB wordsDB = new WordsDB();
                wordsDB.read(cursor);
                wordsList.add(wordsDB);
            } while(cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(wordsList, new Comparator<Words>()
        {
            @Override
            public int compare(Words tags, Words tags2)
            {
                return tags.getName().compareTo(tags2.getName());
            }
        });

        return wordsList;
    }

    public void add(Words account)
    {
        ((WordsDB)account).insert();
    }

    public void update(Words account)
    {
        ((WordsDB)account).update();
    }

    public void delete(Words account)
    {
        ((WordsDB)account).delete();
    }
}
