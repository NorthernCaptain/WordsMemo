package northern.captain.app.WordsMemo.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import northern.captain.app.WordsMemo.AndroidContext;

import java.io.File;
import java.util.Date;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 27.10.13
 * Time: 22:00
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class SQLManager
{
    protected static SQLManager singleton;

    public static SQLManager instance()
    {
        return singleton;
    }

    public static void initialize()
    {
        singleton = new SQLManager();
    }

    protected SQLManager()
    {
        initDB();
    }

    private String passwd = "ecrypt";

    private static final String DB_NAME="budwise.db";
    private static final int DB_VERSION = 1;

    private Helper helper;

    private class Helper extends SQLiteOpenHelper
    {
        private static final String CRE_ACCOUNTS =
                "create table accounts (id integer primary key, name text(64), amount integer default 0 not null, comments text, modified datetime)";

        private static final String CRE_CATEGORIES =
                "create table categories (id integer primary key, name text(64), parent_id integer, type_id integer default 0, comments text, modified datetime)";

        private static final String CRE_CAT_INDEX1 =
                "create index cat_parent_idx on categories(parent_id)";

        private static final String CRE_CATEGORY_GROUP =
                "create table category_group (id integer primary key, group_id integer not null references categories(id) on delete cascade, category_id integer not null references categories(id) on delete cascade)";

        private static final String CRE_CAT_GROUP_INDEX1 =
                "create index cat_group_group_idx on category_group(group_id)";

        private static final String CRE_BUDGETS =
                "create table budgets (id integer primary key, name text(64), total_amount integer not null, spent integer default 0, start_date datetime, end_date datetime, comments text, modified datetime)";

        private static final String CRE_PEOPLE =
                "create table people (id integer primary key, name text(64), comments text, modified datetime)";

        private static final String CRE_TRANSACTIONS =
                "create table transactions (id integer primary key, name text(64), comments text, category_group_id integer references categories(id), "
               +"amount integer not null, account_id integer references accounts(id), tran_date datetime not null, type_id integer default 0, "
               +"person_id integer references people(id), budget_id integer references budgets(id), tran_group integer default 0)";

        private static final String CRE_TRANS_INDEX1 =
                "create index tran_date_idx on transactions(tran_date)";
        private static final String CRE_TRANS_INDEX2 =
                "create index tran_cat_idx on transactions(category_group_id)";
        private static final String CRE_TRANS_INDEX3 =
                "create index tran_type_idx on transactions(type_id)";

        public Helper()
        {
            super(AndroidContext.current.app, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            setPassword(db);
            db.execSQL(CRE_ACCOUNTS);

            db.execSQL(CRE_CATEGORIES);
            db.execSQL(CRE_CAT_INDEX1);

            db.execSQL(CRE_CATEGORY_GROUP);
            db.execSQL(CRE_CAT_GROUP_INDEX1);

            db.execSQL(CRE_BUDGETS);
            db.execSQL(CRE_PEOPLE);

            db.execSQL(CRE_TRANSACTIONS);
            db.execSQL(CRE_TRANS_INDEX1);
            db.execSQL(CRE_TRANS_INDEX2);
            db.execSQL(CRE_TRANS_INDEX3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i2)
        {
            setPassword(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db)
        {
            setPassword(db);
        }

        private void setPassword(SQLiteDatabase db)
        {
//            db.execSQL("PRAGMA key='" + passwd + "'");
        }
    }

    private void initDB()
    {
        helper = new Helper();
    }

    public boolean exists()
    {
        File dbf = AndroidContext.current.app.getDatabasePath(DB_NAME);
        return dbf.exists();
    }

    public boolean checkPassword(String passwd)
    {
        try
        {
            SQLiteDatabase ourDB = helper.getWritableDatabase();

            if(ourDB != null)
            {
                this.passwd = passwd;
                return true;
            }
        } catch(Exception ex) {}
        return false;
    }

    public SQLiteDatabase db()
    {
        return helper.getWritableDatabase();
    }

    public SQLiteDatabase dbr()
    {
        return helper.getReadableDatabase();
    }

    /**
     * Gets current date and time in Unix C time_t format
     * @return
     */
    public static long getCDate()
    {
        return new Date().getTime()/1000L;
    }

    public void shutdown()
    {
        helper.close();
        helper = null;
    }

    public void resume()
    {
        if(helper == null)
            helper = new Helper();
    }
}
