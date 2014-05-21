package northern.captain.app.WordsMemo.logic;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 20.05.14
 * Time: 23:24
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class TrainingParams
{
    protected String name;

    protected TagSet tags;
    protected boolean needTalk = false;
    protected String  mainLang;

    protected boolean showThesaurus = true;

    public boolean isShowThesaurus()
    {
        return showThesaurus;
    }

    public void setShowThesaurus(boolean showThesaurus)
    {
        this.showThesaurus = showThesaurus;
    }

    public TrainingParams(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public TagSet getTags()
    {
        return tags;
    }

    public void setTags(TagSet tags)
    {
        this.tags = tags;
    }

    public boolean isNeedTalk()
    {
        return needTalk;
    }

    public void setNeedTalk(boolean needTalk)
    {
        this.needTalk = needTalk;
    }

    public String getMainLang()
    {
        return mainLang;
    }

    public void setMainLang(String mainLang)
    {
        this.mainLang = mainLang;
    }
}
