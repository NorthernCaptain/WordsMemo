package northern.captain.app.WordsMemo.factory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import northern.captain.app.WordsMemo.logic.Tags;
import northern.captain.app.WordsMemo.logic.Words;
import northern.captain.tools.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 11.08.2014.
 */
public class ExportImportFactory
{
    public static ExportImportFactory singleton;

    public static void initialize() { singleton = new ExportImportFactory(); }

    public static ExportImportFactory instance() { return singleton; }


    public static final int TYPE_XLS = 0;

    public int doExport(int type, File toFile)
    {
        int ret = -1;
        switch (type)
        {
            case TYPE_XLS:
                ret = doExportXLS(toFile);
                break;
        }

        return ret;
    }

    protected int doExportXLS(File toFile)
    {
        WritableWorkbook workbook;

        int wordsWritten = 0;

        try
        {
           workbook = Workbook.createWorkbook(toFile);
        } catch (IOException e)
        {
            e.printStackTrace();
            return -2;
        }

        WritableSheet sheet = workbook.createSheet("Words", 0);

        try
        {
            sheet.addCell(new Label(0,0, "Word"));
            sheet.addCell(new Label(1,0, "Translation"));
            sheet.addCell(new Label(2,0, "Thesaurus"));
            sheet.addCell(new Label(3,0, "Tags"));

            List<Words> allWords = WordFactory.instance().getWords();
            for(Words word : allWords)
            {
                wordsWritten++;
                sheet.addCell(new Label(0,wordsWritten, word.getName()));
                sheet.addCell(new Label(1,wordsWritten, word.getTranslation()));
                sheet.addCell(new Label(2,wordsWritten, word.getThesaurus()));
                sheet.addCell(new Label(3,wordsWritten, word.getTags().toString()));
            }

        } catch (WriteException e)
        {
            e.printStackTrace();
            return -4;
        }

        try
        {
            workbook.write();
            workbook.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            return -3;
        } catch (WriteException e)
        {
            e.printStackTrace();
            return -1;
        }

        return wordsWritten;
    }

    protected int doImportXLS(File fromFile)
    {
        Workbook workbook;
        int row = 1;

        try
        {
            workbook = Workbook.getWorkbook(fromFile);

            Sheet sheet = workbook.getSheet(0);

            Map<String, Tags> allTags = TagFactory.instance().getTagsMap();

            while(true)
            {
                String name = sheet.getCell(0, row).getContents();
                if(StringUtils.isNullOrEmpty(name))
                    break;
                String translation = sheet.getCell(1, row).getContents();
                String thesaurus = sheet.getCell(2, row).getContents();
                String tags = sheet.getCell(3, row).getContents();

                Words word = WordFactory.instance().getByName(name);

                if(word != null)
                {
                    word.setTranslation(translation);
                    word.setThesaurus(thesaurus);
                    word.setTags(TagFactory.instance().getTagsFromStringNames(allTags, tags));
                    WordFactory.instance().update(word);

                } else
                {
                    word = WordFactory.instance().newWord();
                    word.setName(name);
                    word.setTranslation(translation);
                    word.setThesaurus(thesaurus);
                    word.setTags(TagFactory.instance().getTagsFromStringNames(allTags, tags));

                    WordFactory.instance().add(word);
                }

                row++;
            }

            workbook.close();

        } catch (IOException e)
        {
            e.printStackTrace();
            return -1;
        } catch (BiffException e)
        {
            e.printStackTrace();
            return -2;
        }

        return row - 1;
    }
}
