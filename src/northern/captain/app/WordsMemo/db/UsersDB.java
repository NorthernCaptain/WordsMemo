package northern.captain.app.WordsMemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.logic.Tags;
import northern.captain.app.WordsMemo.logic.Users;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 28.10.13
 * Time: 23:40
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class UsersDB extends Tags implements ICRUD, Users
{
    public static final String DB_USER      = "_db_";
    public static final int    DB_USER_ID   = -1;
    public static final String TBL_USERS    = "users";
    public static final String DBF_ID       = "id";
    public static final String DBF_NAME     = "name";
    public static final String DBF_COMMENTS = "thesaurus";
    public static final String DBF_MODIFIED = "modified";

    @Override
    public long insert()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values = new ContentValues();

        values.put(DBF_NAME, name);
        values.put(DBF_COMMENTS, comments);
        values.put(DBF_MODIFIED, SQLManager.getCDate());

        id = db.insert(TBL_USERS, null, values);

        return id;
    }

    public static void insertDefaultUser(SQLiteDatabase db, String comments)
    {
        ContentValues values = new ContentValues();

        values.put(DBF_ID, DB_USER_ID);
        values.put(DBF_NAME, DB_USER);
        values.put(DBF_COMMENTS, comments);
        values.put(DBF_MODIFIED, SQLManager.getCDate());

        db.insert(TBL_USERS, null, values);
    }

    @Override
    public void update()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values = new ContentValues();

        values.put(DBF_NAME, name);
        values.put(DBF_COMMENTS, comments);
        values.put(DBF_MODIFIED, SQLManager.getCDate());

        db.update(TBL_USERS, values, DBF_ID + " = ?", new String[] { String.valueOf(id)});
    }

    @Override
    public void delete()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        db.delete(TBL_USERS, DBF_ID + " = ?", new String[] { String.valueOf(id)});
    }


    @Override
    public void read(Cursor cur)
    {
        id = cur.getLong(cur.getColumnIndex(DBF_ID));
        name = cur.getString(cur.getColumnIndex(DBF_NAME));
        comments = cur.getString(cur.getColumnIndex(DBF_COMMENTS));
    }
}
