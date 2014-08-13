package northern.captain.app.WordsMemo.logic;

import java.util.List;

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

    protected TagSet tagSet;
    protected boolean needTalk = false;
    protected String  mainLang;

    public TrainingParams()
    {
        tagSet = new TagSet();
    }

    protected List<Long> initialWordIdList;

    protected boolean showThesaurus = false;

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

    public TagSet getTagSet()
    {
        return tagSet;
    }

    public void setTagSet(TagSet tags)
    {
        this.tagSet = tags;
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
        return mainLang == null ? Words.LANG_EN : mainLang;
    }

    public void setMainLang(String mainLang)
    {
        this.mainLang = mainLang;
    }

    public List<Long> getInitialWordIdList()
    {
        return initialWordIdList;
    }

    public void setInitialWordIdList(List<Long> initialWordIdList)
    {
        this.initialWordIdList = initialWordIdList;
    }
}
