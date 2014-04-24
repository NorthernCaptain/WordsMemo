package northern.captain.app.WordsMemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.logic.Accounts;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 28.10.13
 * Time: 23:40
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class AccountsDB extends Accounts implements ICRUD
{
    public static final String TBL_ACCOUNTS = "accounts";
    public static final String DBF_ID = "id";
    public static final String DBF_NAME = "name";
    public static final String DBF_AMOUNT = "amount";
    public static final String DBF_COMMENTS = "comments";
    public static final String DBF_MODIFIED = "modified";

    @Override
    public long insert()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values  = new ContentValues();

        values.put(DBF_NAME, name);
        values.put(DBF_AMOUNT, amount);
        values.put(DBF_COMMENTS, comments);
        values.put(DBF_MODIFIED, SQLManager.getCDate());

        id = db.insert(TBL_ACCOUNTS, null, values);

        return id;
    }

    @Override
    public void update()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        ContentValues values  = new ContentValues();

        values.put(DBF_NAME, name);
        values.put(DBF_AMOUNT, amount);
        values.put(DBF_COMMENTS, comments);
        values.put(DBF_MODIFIED, SQLManager.getCDate());

        db.update(TBL_ACCOUNTS, values, DBF_ID + " = ?", new String[] { String.valueOf(id)});
    }

    @Override
    public void delete()
    {
        SQLiteDatabase db = SQLManager.instance().db();
        db.delete(TBL_ACCOUNTS, DBF_ID + " = ?", new String[] { String.valueOf(id)});
    }


    @Override
    public void read(Cursor cur)
    {
        id = cur.getLong(cur.getColumnIndex(DBF_ID));
        name = cur.getString(cur.getColumnIndex(DBF_NAME));
        amount = cur.getInt(cur.getColumnIndex(DBF_AMOUNT));
        comments = cur.getString(cur.getColumnIndex(DBF_COMMENTS));
    }
}
