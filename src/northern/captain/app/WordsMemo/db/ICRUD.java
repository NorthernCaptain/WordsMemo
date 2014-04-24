package northern.captain.app.WordsMemo.db;

import android.database.Cursor;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 28.10.13
 * Time: 23:59
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public interface ICRUD
{
    /**
     * Insert new record into DB
     * @return
     */
    long insert();

    /**
     * Updates all fields to db using record id
     */
    void update();

    /**
     * Delete record from db with the given id
     */
    void delete();


    /**
     * Read record values from the given cursor
     * @param cur
     */
    void read(Cursor cur);
}
