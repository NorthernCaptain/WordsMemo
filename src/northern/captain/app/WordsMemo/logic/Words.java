package northern.captain.app.WordsMemo.logic;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 28.10.13
 * Time: 23:33
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class Words
{
    protected long id;
    protected String name;
    protected String thesaurus;
    protected String translation;
    protected int    usedTimes = 0;
    protected int    status = 0;
    protected String lang;
    protected String transLang;

    public static final int FLAG_TRANSLATION_IN_HTML = 1;
    public static final int FLAG_THESAURUS_IN_HTML = 2;
    protected int    flags = 0;


    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getTranslation()
    {
        return translation;
    }

    public void setTranslation(String translation)
    {
        this.translation = translation;
    }

    public int getUsedTimes()
    {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes)
    {
        this.usedTimes = usedTimes;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getThesaurus()
    {
        return thesaurus;
    }

    public void setThesaurus(String thesaurus)
    {
        this.thesaurus = thesaurus;
    }

    public int incrementUsage()
    {
        usedTimes++;
        return usedTimes;
    }

    public boolean isFlagSet(int flagBit)
    {
        return (flags & flagBit) == flagBit;
    }

    public void setFlagBit(int flagBit)
    {
        flags |= flagBit;
    }

    public void resetFlagBit(int flagBit)
    {
        flags &= ~flagBit;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getTransLang()
    {
        return transLang;
    }

    public void setTransLang(String transLang)
    {
        this.transLang = transLang;
    }
}
