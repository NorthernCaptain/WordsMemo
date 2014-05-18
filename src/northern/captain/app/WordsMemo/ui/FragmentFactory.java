package northern.captain.app.WordsMemo.ui;

import northern.captain.app.WordsMemo.logic.Tags;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: NorthernCaptain
 * Date: 29.10.13
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */
public class FragmentFactory
{
    protected FragmentFactory()
    {

    }

    protected static FragmentFactory singleton;
    public static FragmentFactory instance()
    {
        return singleton;
    }

    public static void initialize()
    {
        if(singleton == null)
            singleton = new FragmentFactory();
    }

    public TagsFragment newTagsFragment()
    {
        return new TagsFragment();
    }

    public IntroFragment newIntroFragment()
    {
        return new IntroFragment();
    }

    public WordCatalogFragment newWordCatFragment()
    {
        return new WordCatalogFragment();
    }

    public WordEditFragment newWordEditFragment(WordEditFragment.onOKListener listener)
    {
        return new WordEditFragment(listener);
    }

    public TagsSelectorDialogFragment newTagSelectorFragment(Set<Tags> selected, TagsSelectorDialogFragment.IOKCallback callback) { return new TagsSelectorDialogFragment(selected, callback);}
}
