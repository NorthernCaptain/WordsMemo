package northern.captain.app.WordsMemo.factory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import northern.captain.app.WordsMemo.db.SQLManager;
import northern.captain.app.WordsMemo.db.TaskDetailDB;
import northern.captain.app.WordsMemo.db.TasksDB;
import northern.captain.app.WordsMemo.db.WordsDB;
import northern.captain.app.WordsMemo.logic.TaskDetail;
import northern.captain.app.WordsMemo.logic.Tasks;
import northern.captain.app.WordsMemo.logic.Words;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 13.08.14
 * Time: 12:07
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TaskFactory
{
    protected static TaskFactory singleton;

    public static void initialize() { singleton = new TaskFactory();}
    public static TaskFactory instance() { return singleton;}

    public Tasks newTask() { return new TasksDB();}
    public TaskDetail newTaskDetail() { return new TaskDetailDB();}

    public void add(Tasks task) { ((TasksDB)task).insert();}
    public void update(Tasks task) { ((TasksDB)task).update();}
    public void delete(Tasks task) { ((TasksDB)task).delete();}

    public void add(TaskDetail task) { ((TaskDetailDB)task).insert();}
    public void update(TaskDetail task) { ((TaskDetailDB)task).update();}
    public void delete(TaskDetail task) { ((TaskDetailDB)task).delete();}

    public List<TaskDetail> getTaskDetailListByTaskId(long taskId)
    {
        List<TaskDetail> result = null;

        SQLiteDatabase db = SQLManager.instance().dbr();

        String query;

        query = "SELECT * from " + TaskDetailDB.TBL_TASK_DETAIL + " where " + TaskDetailDB.DBF_TASK_ID + " = " + taskId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            result = new ArrayList<TaskDetail>();
            do
            {
                TaskDetailDB tdet = new TaskDetailDB();
                tdet.read(cursor);
                result.add(tdet);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return result;
    }

    public List<Words> getWordsListByTaskIdStatus(long taskId, int status)
    {
        List<Words> result = new ArrayList<Words>();

        SQLiteDatabase db = SQLManager.instance().dbr();

        String query;

        query = "SELECT words.* from words join task_detail on task_detail.word_id = words.id where " + TaskDetailDB.DBF_TASK_ID + " = " + taskId
        + " AND task_detail." + TaskDetailDB.DBF_STATUS + " = " + status + " order by words.name";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                WordsDB wordsDB = new WordsDB();
                wordsDB.read(cursor);
                result.add(wordsDB);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return result;
    }

    public List<Tasks> getTasksByStatus(int status, String sortByField)
    {
        List<Tasks> result = new ArrayList<Tasks>();

        SQLiteDatabase db = SQLManager.instance().dbr();

        String query;

        query = "SELECT * from " + TasksDB.TBL_TASKS + " where " + TasksDB.DBF_TASK_TYPE + " = " + Tasks.TYPE_TEST
           + " and " + TasksDB.DBF_STATUS + " = " + status + " order by " + sortByField + " desc";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                TasksDB tasksDB = new TasksDB();
                tasksDB.read(cursor);
                result.add(tasksDB);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return result;
    }
}
