package northern.captain.app.WordsMemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.logic.TaskDetail;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 13.08.14
 * Time: 12:14
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TaskDetailDB extends TaskDetail implements ICRUD
{
    public static final String TBL_TASK_DETAIL = "task_detail";

    public static final String DBF_ID                = "id";
    public static final String DBF_TASK_ID           = "task_id";
    public static final String DBF_WORD_ID           = "word_id";
    public static final String DBF_STATUS            = "status";

    @Override
    public long insert()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values = new ContentValues();

        values.put(DBF_TASK_ID, taskId);
        values.put(DBF_WORD_ID, wordId);
        values.put(DBF_STATUS, status);

        id = db.insert(TBL_TASK_DETAIL, null, values);

        return id;
    }

    @Override
    public void update()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values = new ContentValues();

        //Do not update all fields ?
        values.put(DBF_STATUS, status);

        db.update(TBL_TASK_DETAIL, values, DBF_ID + " = ?", new String[] { String.valueOf(id)});

    }

    @Override
    public void delete()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        db.delete(TBL_TASK_DETAIL, DBF_ID + " = ?", new String[] { String.valueOf(id)});
    }


    @Override
    public void read(Cursor cur)
    {
        id = cur.getLong(cur.getColumnIndex(DBF_ID));
        taskId = cur.getLong(cur.getColumnIndex(DBF_TASK_ID));
        wordId = cur.getLong(cur.getColumnIndex(DBF_WORD_ID));
        status = cur.getInt(cur.getColumnIndex(DBF_STATUS));
    }
}
