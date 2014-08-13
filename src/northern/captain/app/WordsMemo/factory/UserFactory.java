package northern.captain.app.WordsMemo.factory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.db.SQLManager;
import northern.captain.app.WordsMemo.db.UsersDB;
import northern.captain.app.WordsMemo.logic.Users;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 13.08.14
 * Time: 11:07
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class UserFactory
{
    protected static UserFactory singleton;

    public static void initialize()
    {
        singleton = new UserFactory();
    }

    public static UserFactory instance()
    {
        return singleton;
    }

    public Users getByNameOrCreate(String userName)
    {
        Users user = getByName(userName);
        if(user == null)
        {
            UsersDB udb = new UsersDB();
            udb.setName(userName);
            udb.insert();
            user = udb;
        }
        return user;
    }

    public Users getByName(String userName)
    {
        SQLiteDatabase db = SQLManager.instance().dbr();

        String query = "SELECT * from " + UsersDB.TBL_USERS + " WHERE name = ?";

        Cursor cursor = db.rawQuery(query, new String[] { userName });
        try
        {
            if (cursor.moveToFirst())
            {
                UsersDB userDB = new UsersDB();
                userDB.read(cursor);
                return userDB;
            }
        }
        finally
        {
            cursor.close();
        }
        return null;
    }

    public Users getById(long id)
    {
        SQLiteDatabase db = SQLManager.instance().dbr();

        String query = "SELECT * from " + UsersDB.TBL_USERS + " WHERE id = " + id;

        Cursor cursor = db.rawQuery(query, null);
        try
        {
            if (cursor.moveToFirst())
            {
                UsersDB userDB = new UsersDB();
                userDB.read(cursor);
                return userDB;
            }
        }
        finally
        {
            cursor.close();
        }
        return null;
    }
}
