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
import northern.captain.tools.IProgressUpdate;
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


    private static final int COL_NAME = 1;
    private static final int COL_TRANSLATION = 2;
    private static final int COL_THESAURUS = 3;
    private static final int COL_TAGS = 0;

    public static final int TYPE_XLS = 0;

    public int doExport(int type, File toFile, IProgressUpdate progressUpdate)
    {
        int ret = -1;
        switch (type)
        {
            case TYPE_XLS:
                ret = doExportXLS(toFile, progressUpdate);
                break;
        }

        return ret;
    }

    protected int doExportXLS(File toFile, IProgressUpdate progressUpdate)
    {
        WritableWorkbook workbook;

        int wordsWritten = 0;

        try
        {
            workbook = Workbook.createWorkbook(toFile);
            progressUpdate.updateProgress(1,0);
        } catch (IOException e)
        {
            e.printStackTrace();
            return -2;
        }

        WritableSheet sheet = workbook.createSheet("Words", 0);
        progressUpdate.updateProgress(2,0);

        try
        {
            sheet.addCell(new Label(COL_NAME,0, "Word (EN)"));
            sheet.addCell(new Label(COL_TRANSLATION,0, "Translation"));
            sheet.addCell(new Label(COL_THESAURUS,0, "Thesaurus (EN)"));
            sheet.addCell(new Label(COL_TAGS,0, "Tags"));

            progressUpdate.updateProgress(3,0);
            List<Words> allWords = WordFactory.instance().getWords();
            progressUpdate.updateProgress(4,0);
            for(Words word : allWords)
            {
                wordsWritten++;
                sheet.addCell(new Label(COL_NAME,wordsWritten, word.getName()));
                sheet.addCell(new Label(COL_TRANSLATION,wordsWritten, word.getTranslation()));
                sheet.addCell(new Label(COL_THESAURUS,wordsWritten, word.getThesaurus()));
                sheet.addCell(new Label(COL_TAGS,wordsWritten, word.getTags().toString()));
                progressUpdate.updateProgress(5 + wordsWritten,0);
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

    public int doImport(int type, File fromFile, IProgressUpdate progressUpdate)
    {
        int ret = 0;
        switch(type)
        {
            case TYPE_XLS:
                ret = doImportXLS(fromFile, progressUpdate);
                break;
        }

        return ret;
    }

    protected int doImportXLS(File fromFile, IProgressUpdate progressUpdate)
    {
        Workbook workbook;
        int row = 1;

        try
        {
            workbook = Workbook.getWorkbook(fromFile);

            Sheet sheet = workbook.getSheet(0);

            Map<String, Tags> allTags = TagFactory.instance().getTagsMap();

            int totalRows = sheet.getRows();

            progressUpdate.updateProgress(0, totalRows);

            while(row < totalRows)
            {
                String name = sheet.getCell(COL_NAME, row).getContents();
                if(StringUtils.isNullOrEmpty(name))
                {
                    break;
                }
                String translation = sheet.getCell(COL_TRANSLATION, row).getContents().trim();
                String thesaurus = sheet.getCell(COL_THESAURUS, row).getContents().trim();
                String tags = sheet.getCell(COL_TAGS, row).getContents().trim();

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
                    word.setLang(Words.LANG_EN);
                    word.setTransLang(Words.LANG_RU);
                    if(!StringUtils.isNullOrEmpty(translation) && translation.charAt(0) == '<')
                        word.setFlagBit(Words.FLAG_TRANSLATION_IN_HTML);
                    if(!StringUtils.isNullOrEmpty(thesaurus) && thesaurus.charAt(0) == '<')
                        word.setFlagBit(Words.FLAG_THESAURUS_IN_HTML);

                    WordFactory.instance().add(word);
                }

                row++;
                progressUpdate.updateProgress(row, totalRows);
            }

            workbook.close();

            progressUpdate.updateProgress(totalRows, totalRows);

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
