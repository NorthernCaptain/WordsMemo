package northern.captain.app.WordsMemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.logic.Tasks;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 13.08.14
 * Time: 11:36
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TasksDB extends Tasks implements ICRUD
{
    /* id integer primary key, user_id integer references users(id)), name text(64), "
                +"start_date datetime, finish_date datetime, time_spent_sec integer default 0, "
                +"words_total integer, words_passed integer default 0, words_error integer default 0, "
                +"status integer default 0, tags text, task_type integer default 0, score integer default 0 */

    public static final String TBL_TASKS             = "tasks";

    public static final String DBF_ID                = "id";
    public static final String DBF_NAME              = "name";
    public static final String DBF_USER_ID           = "user_id";
    public static final String DBF_START_DATE        = "start_date";
    public static final String DBF_FINISH_DATE       = "finish_date";
    public static final String DBF_TIME_SPENT_SEC    = "time_spent_sec";
    public static final String DBF_WORDS_TOTAL       = "words_total";
    public static final String DBF_WORDS_PASSED      = "words_passed";
    public static final String DBF_WORDS_ERROR       = "words_error";
    public static final String DBF_STATUS            = "status";
    public static final String DBF_TAGS              = "tags";
    public static final String DBF_TASK_TYPE         = "task_type";
    public static final String DBF_SCORE             = "score";

    @Override
    public long insert()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values = new ContentValues();

        values.put(DBF_NAME, name);
        values.put(DBF_USER_ID, userId);
        startDate = finishDate = SQLManager.getCDate();
        values.put(DBF_START_DATE, startDate);
        values.put(DBF_FINISH_DATE, finishDate);
        values.put(DBF_TIME_SPENT_SEC, timeSpentSec);
        values.put(DBF_WORDS_TOTAL, wordsTotal);
        values.put(DBF_WORDS_PASSED, wordsPassed);
        values.put(DBF_WORDS_ERROR, wordsError);
        values.put(DBF_STATUS, status);
        values.put(DBF_TAGS, tagSet.id2String());
        values.put(DBF_TASK_TYPE, taskType);
        values.put(DBF_SCORE, score);

        id = db.insert(TBL_TASKS, null, values);

        return id;
    }

    @Override
    public void update()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values = new ContentValues();

        //Do not update all fields ?
        values.put(DBF_NAME, name);
        values.put(DBF_USER_ID, userId);
        values.put(DBF_START_DATE, startDate);
        values.put(DBF_TAGS, tagSet.id2String());
        values.put(DBF_TASK_TYPE, taskType);

        finishDate = SQLManager.getCDate();
        values.put(DBF_FINISH_DATE, finishDate);
        values.put(DBF_TIME_SPENT_SEC, timeSpentSec);
        values.put(DBF_WORDS_TOTAL, wordsTotal);
        values.put(DBF_WORDS_PASSED, wordsPassed);
        values.put(DBF_WORDS_ERROR, wordsError);
        values.put(DBF_STATUS, status);
        values.put(DBF_SCORE, score);

        db.update(TBL_TASKS, values, DBF_ID + " = ?", new String[] { String.valueOf(id)});

    }

    @Override
    public void delete()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        db.delete(TaskDetailDB.TBL_TASK_DETAIL, TaskDetailDB.DBF_TASK_ID + " = ?", new String[] { String.valueOf(id)});
        db.delete(TBL_TASKS, DBF_ID + " = ?", new String[] { String.valueOf(id)});
    }


    @Override
    public void read(Cursor cur)
    {
        id = cur.getLong(cur.getColumnIndex(DBF_ID));
        name = cur.getString(cur.getColumnIndex(DBF_NAME));
        userId = cur.getLong(cur.getColumnIndex(DBF_USER_ID));
        startDate = cur.getLong(cur.getColumnIndex(DBF_START_DATE));
        finishDate = cur.getLong(cur.getColumnIndex(DBF_FINISH_DATE));
        timeSpentSec = cur.getInt(cur.getColumnIndex(DBF_TIME_SPENT_SEC));
        wordsTotal = cur.getInt(cur.getColumnIndex(DBF_WORDS_TOTAL));
        wordsPassed = cur.getInt(cur.getColumnIndex(DBF_WORDS_PASSED));
        wordsError = cur.getInt(cur.getColumnIndex(DBF_WORDS_ERROR));
        status = cur.getInt(cur.getColumnIndex(DBF_STATUS));
        taskType = cur.getInt(cur.getColumnIndex(DBF_TASK_TYPE));
        score = cur.getInt(cur.getColumnIndex(DBF_SCORE));
        tagSet.string2Id(cur.getString(cur.getColumnIndex(DBF_TAGS)));
    }
}
