package northern.captain.app.WordsMemo.logic;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 28.10.13
 * Time: 23:33
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public interface Users
{
    public long getId();

    public void setId(long id);

    public String getName();

    public void setName(String name);

    public String getComments();

    public void setComments(String comments);
}
