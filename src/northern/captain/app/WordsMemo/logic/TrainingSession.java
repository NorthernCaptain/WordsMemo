package northern.captain.app.WordsMemo.logic;

import northern.captain.app.WordsMemo.factory.WordFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by NorthernCaptain on 21.05.2014.
 */
public class TrainingSession extends TrainingParams
{
    public TrainingSession(String name)
    {
        super(name);
    }

    List<Long> wordIdsFull = new ArrayList<Long>();
    List<Long> currentIds  = new LinkedList<Long>();
    List<Long> usedIds     = new ArrayList<Long>();

    Words currentWord;

    Random rnd;

    public void init()
    {
        wordIdsFull = WordFactory.instance().getIdsByTags(this.tags);
        currentIds.clear();
        currentIds.addAll(wordIdsFull);
        rnd = new Random();
    }

    public Words nextWord()
    {
        int siz = currentIds.size();
        if(siz == 0)
            return null;

        int idx = rnd.nextInt(siz);

        Long id = currentIds.get(idx);
        currentIds.remove(idx);

        currentWord = WordFactory.instance().getById(id);

        return currentWord;
    }

    public Words getCurrentWord()
    {
        return currentWord;
    }
}
