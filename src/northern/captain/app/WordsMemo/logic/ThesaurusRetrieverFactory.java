package northern.captain.app.WordsMemo.logic;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 23.05.14
 * Time: 23:27
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class ThesaurusRetrieverFactory
{
    protected static ThesaurusRetrieverFactory singleton = new ThesaurusRetrieverFactory();


    protected IThesaurusRetriever current = new ThesaurusRetrieverLivio();

    public static ThesaurusRetrieverFactory instance()
    {
        return singleton;
    }

    public IThesaurusRetriever getThesaurus()
    {
        return current;
    }
}
