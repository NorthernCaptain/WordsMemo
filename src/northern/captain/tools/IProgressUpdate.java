package northern.captain.tools;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 11.08.14
 * Time: 23:02
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public interface IProgressUpdate
{
    public void updateProgress(int currentValue, int maxValue);
}
