package northern.captain.app.WordsMemo.factory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.db.AccountsDB;
import northern.captain.app.WordsMemo.db.SQLManager;
import northern.captain.app.WordsMemo.logic.Accounts;

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
public class AccountFactory
{
    protected static AccountFactory singleton;

    public static AccountFactory instance()
    {
        return singleton;
    }

    public static void initialize()
    {
        singleton = new AccountFactory();
    }

    protected AccountFactory()
    {

    }

    public Accounts newAccount()
    {
        return new AccountsDB();
    }


    public List<Accounts> getAccounts()
    {
        List<Accounts> accountsList = new ArrayList<Accounts>();

        SQLiteDatabase db = SQLManager.instance().dbr();

        String query = "SELECT * from " + AccountsDB.TBL_ACCOUNTS;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do
            {
                AccountsDB accountsDB = new AccountsDB();
                accountsDB.read(cursor);
                accountsList.add(accountsDB);
            } while(cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(accountsList, new Comparator<Accounts>()
        {
            @Override
            public int compare(Accounts accounts, Accounts accounts2)
            {
                return accounts.getName().compareTo(accounts2.getName());
            }
        });

        return accountsList;
    }

    public void add(Accounts account)
    {
        ((AccountsDB)account).insert();
    }

    public void update(Accounts account)
    {
        ((AccountsDB)account).update();
    }

    public void delete(Accounts account)
    {
        ((AccountsDB)account).delete();
    }
}
