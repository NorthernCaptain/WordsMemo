package northern.captain.app.WordsMemo.logic;

import android.app.SearchManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import northern.captain.app.WordsMemo.AndroidContext;

/**
 * Copyright 2013 by Northern Captain Software
 * User: leo
 * Date: 23.05.14
 * Time: 23:21
 * This code is a private property of its owner Northern Captain.
 * Any copying or redistribution of this source code is strictly prohibited.
 */
public class ThesaurusRetrieverLivio implements IThesaurusRetriever
{
    public static String AUTHORITY = "livio.pack.lang.en_US.DictionaryProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/dictionary");
    public static final Uri CONTENT_URI3 = Uri.parse("content://" + AUTHORITY + "/"+ SearchManager.SUGGEST_URI_PATH_QUERY);

    @Override
    public String getDefinition(String word)
    {
        String result = null;
        Cursor cursor = AndroidContext.current.app.getContentResolver().query(CONTENT_URI, null, null, new String[] {word}, null);
        if ((cursor != null) && cursor.moveToFirst())
        {
            int wIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
            int dIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);
            result = cursor.getString(dIndex);
            Log.d("wordm", "found word: " + cursor.getString(wIndex) + ", definition: " + result);
        } else
        {
            Log.d("wordm", "missing word for: " + word );
            return null;
        }

        return result;
    }
}
