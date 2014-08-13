package northern.captain.app.WordsMemo.logic;

import northern.captain.app.WordsMemo.factory.TaskFactory;
import northern.captain.app.WordsMemo.factory.UserFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 13.08.14
 * Time: 11:38
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class Tasks extends TrainingParams
{
    /* id integer primary key, user_id integer references users(id)), name text(64), "
                +"start_date datetime, finish_date datetime, time_spent_sec integer default 0, "
                +"words_total integer, words_passed integer default 0, words_error integer default 0, "
                +"status integer default 0, tags text, task_type integer default 0, score integer default 0 */
    protected long id;
    protected long userId;
    protected String name;
    protected long startDate;
    protected long finishDate;
    protected int timeSpentSec;
    protected int wordsTotal;
    protected int wordsPassed;
    protected int wordsError;

    public static final int STATUS_PROCESSING = 0;
    public static final int STATUS_DONE = 1;
    protected int status;

    public static final int TYPE_TEST = 0;
    public static final int TYPE_TRAINING = 1;
    protected int taskType;

    protected int score;

    protected List<TaskDetail> taskDetails;

    public Tasks()
    {
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getStartDate()
    {
        return startDate;
    }

    public Date getStartDateDate()
    {
        return new Date(startDate*1000L);
    }

    public void setStartDate(long startDate)
    {
        this.startDate = startDate;
    }

    public long getFinishDate()
    {
        return finishDate;
    }

    public Date getFinishDateDate()
    {
        return new Date(finishDate*1000L);
    }

    public void setFinishDate(long finishDate)
    {
        this.finishDate = finishDate;
    }

    public int getTimeSpentSec()
    {
        return timeSpentSec;
    }

    public void setTimeSpentSec(int timeSpentSec)
    {
        this.timeSpentSec = timeSpentSec;
    }

    public int getWordsTotal()
    {
        return wordsTotal;
    }

    public void setWordsTotal(int wordsTotal)
    {
        this.wordsTotal = wordsTotal;
    }

    public int getWordsPassed()
    {
        return wordsPassed;
    }

    public void setWordsPassed(int wordsPassed)
    {
        this.wordsPassed = wordsPassed;
    }

    public int getWordsError()
    {
        return wordsError;
    }

    public String getWordsErrorString()
    {
        StringBuilder buf = new StringBuilder();

        buf.append(wordsError);
        buf.append(" (");
        buf.append(wordsPassed == 0 ? 0 : wordsError*100/wordsPassed);
        buf.append("%)");

        return buf.toString();
    }

    public void setWordsError(int wordsError)
    {
        this.wordsError = wordsError;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getTaskType()
    {
        return taskType;
    }

    public void setTaskType(int taskType)
    {
        this.taskType = taskType;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public long getUserId()
    {
        return userId;
    }

    public Users getUser()
    {
        return UserFactory.instance().getById(userId);
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<TaskDetail> getTaskDetails()
    {
        if(taskDetails == null && id > 0)
        {
            taskDetails = TaskFactory.instance().getTaskDetailListByTaskId(id);
        }
        return taskDetails;
    }

    public List<TaskDetail> getUnprocessedTaskDetails()
    {
        getTaskDetails();

        List<TaskDetail> list = new ArrayList<TaskDetail>();
        if(taskDetails == null)
            return list;

        for(TaskDetail taskDetail : taskDetails)
        {
            if(taskDetail.getStatus() == TaskDetail.STATUS_UNUSED)
            {
                list.add(taskDetail);
            }
        }

        return list;
    }

    public Users initUser(String name)
    {
        Users user = UserFactory.instance().getByNameOrCreate(name);
        userId = user.getId();
        return user;
    }


    public void start()
    {
        if(id == 0)
        {
            init();
        }

        unusedWords = getUnprocessedTaskDetails();
        nextWord();
    }

    public void init()
    {
        wordsTotal = initialWordIdList.size();
        wordsPassed = 0;
        wordsError = 0;

        TaskFactory.instance().add(this);

        this.taskDetails = new ArrayList<TaskDetail>();
        for(Long wordId : initialWordIdList)
        {
            TaskDetail taskDetail = TaskFactory.instance().newTaskDetail();
            taskDetail.setStatus(TaskDetail.STATUS_UNUSED);
            taskDetail.setTaskId(id);
            taskDetail.setWordId(wordId);

            TaskFactory.instance().add(taskDetail);
            taskDetails.add(taskDetail);
        }
    }

    protected long startWordTime;
    protected List<TaskDetail> unusedWords;
    protected int currentWordIdx;
    protected Words currentWord;
    public static Random rnd = new Random();

    public Words nextWord()
    {
        startWordTime = System.currentTimeMillis();

        int len = unusedWords.size();
        if(len == 0)
            return null;

        int idx = currentWordIdx = len == 1 ? 0 : rnd.nextInt(len);

        TaskDetail taskDetail = unusedWords.get(idx);

        currentWord = taskDetail.getWord();
        return currentWord;
    }

    /**
     * Mark current word as passed with given status
     * @param status - STATUS_PASSED or STATUS_ERROR
     * @return true if no more words left in this task
     */
    public boolean markWordPassed(int status, int score)
    {
        if(currentWordIdx < 0)
            return false;

        boolean failed = status == TaskDetail.STATUS_ERROR;
        TaskDetail taskDetail = unusedWords.get(currentWordIdx);
        taskDetail.setStatus(status);
        wordsPassed++;
        if(failed)
            wordsError++;
        long curTime = System.currentTimeMillis();

        long spentSec = (curTime - startWordTime) / 1000L;
        timeSpentSec += spentSec;
        this.score += score + (failed ? 0 : (13 - Math.min(spentSec, 13)));

        unusedWords.remove(currentWordIdx);
        currentWordIdx = -1;
        currentWord = null;

        boolean ret = unusedWords.isEmpty();

        if(ret)
        {
            setStatus(STATUS_DONE);
        }

        TaskFactory.instance().update(this);
        TaskFactory.instance().update(taskDetail);

        return ret;
    }

    public Words getCurrentWord()
    {
        return currentWord;
    }

    public Words getAnyWord(Words except)
    {
        List<TaskDetail> allWords = getTaskDetails();
        if(allWords == null)
            return null;

        if(allWords.size() == 1)
            return allWords.get(0).getWord();

        TaskDetail taskDetail = allWords.get(rnd.nextInt(allWords.size()));
        while(taskDetail.getWordId() == except.getId())
        {
            taskDetail = allWords.get(rnd.nextInt(allWords.size()));
        }

        return taskDetail.getWord();
    }
}
