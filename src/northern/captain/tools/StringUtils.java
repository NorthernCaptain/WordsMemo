package northern.captain.tools;

import java.util.Collection;
import java.util.Set;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 19.08.13
 * Time: 22:15
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class StringUtils
{
    static public boolean isNullOrEmpty(String value)
    {
        return value == null || value.length() == 0;
    }

    static public String shorten(String value, int toLen)
    {
        if(value.length() >= toLen)
        {
            return value.substring(0, toLen - 3) + "...";
        }
        return value;
    }

    /**
     * Converts collection of objects into string like '12,14,66,34'
     * @param objects
     * @return
     */
    static public String collectionToString(Collection<?> objects)
    {
        return collectionToString(objects, ",");
    }

    static public String collectionToString(Collection<?> objects, String separator)
    {
        StringBuilder buf = new StringBuilder();
        int total = objects.size();

        if(total == 0)
        {
            return "";
        }

        int i = 0;
        for(Object obj : objects)
        {
            buf.append(obj.toString());
            i++;
            if(i<total)
            {
                buf.append(separator);
            }
        }

        return buf.toString();
    }
}
