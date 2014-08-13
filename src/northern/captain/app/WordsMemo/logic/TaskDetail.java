package northern.captain.app.WordsMemo.logic;

import northern.captain.app.WordsMemo.factory.WordFactory;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 13.08.14
 * Time: 12:11
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TaskDetail
{
    /*
                "create table task_detail (id integer primary key, task_id integer references tasks(id)), "
               +"word_id integer references words(id), status integer default 0)";
     */

    protected long id;
    protected long taskId;
    protected long wordId;

    public static final int STATUS_UNUSED = 0;
    public static final int STATUS_PASSED = 1;
    public static final int STATUS_ERROR  = 2;

    public static final int SCORE_PASSED = 21;
    public static final int SCORE_FAILED = 0;

    protected int status;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getWordId()
    {
        return wordId;
    }

    public void setWordId(long wordId)
    {
        this.wordId = wordId;
    }

    public Words getWord()
    {
        return WordFactory.instance().getById(wordId);
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(long taskId)
    {
        this.taskId = taskId;
    }
}
