package northern.captain.app.WordsMemo.ui;

import northern.captain.app.WordsMemo.factory.TaskFactory;
import northern.captain.app.WordsMemo.logic.TaskDetail;
import northern.captain.app.WordsMemo.logic.Tasks;
import northern.captain.app.WordsMemo.logic.Words;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 13.08.14
 * Time: 18:23
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class WordErrorsForTaskFragment extends WordCatalogFragment
{
    protected Tasks task;

    public WordErrorsForTaskFragment(Tasks task)
    {
        this.task = task;
    }

    @Override
    protected void reloadWords()
    {
        items = TaskFactory.instance().getWordsListByTaskIdStatus(task.getId(), TaskDetail.STATUS_ERROR);
    }

    @Override
    protected void askForDelete(Words words)
    {
    }
}
