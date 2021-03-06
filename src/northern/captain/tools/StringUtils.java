package northern.captain.tools;

import android.text.Html;
import android.text.Spanned;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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

    private static final String HTML_PRE = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<body>\n" +
            "<div align=\"center\">\n";
    private static final String HTML_POST =
            "</div>\n" +
            "</body>\n" +
            "</html>";

    public static String toHtml(String text)
    {
        return HTML_PRE + text + HTML_POST;
    }

    public static String toHtmlH2(String text)
    {
        return HTML_PRE + "<h2>" + text + "</h2>" + HTML_POST;
    }

    public static String toHtmlH1(String text)
    {
        return HTML_PRE + "<h1>" + text + "</h1>" + HTML_POST;
    }

    public static Spanned toTextHtmlH1(String text) { return Html.fromHtml("<h1>" + text + "</h1>");};
    public static Spanned toTextHtmlH2(String text) { return Html.fromHtml("<h2>" + text + "</h2>");};
    public static Spanned toTextHtmlH3(String text) { return Html.fromHtml("<h3>" + text + "</h3>");};
    public static Spanned toTextHtml(String text) { return Html.fromHtml(text);};

    private static SimpleDateFormat dateTime = new SimpleDateFormat("dd MMM yyyy kk:mm");

    public static String formatDateTime(Date date)
    {
        return dateTime.format(date);
    }

    public static String formatTimeSpent(long sec)
    {

        return String.format("%02d:%02d:%02d", sec/3600, (sec%3600)/60, sec % 60);
    }
}
