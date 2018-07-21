package popularmovie.co.larry.popularmovie.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import popularmovie.co.larry.popularmovie.Fragments.TabFragment1;
import popularmovie.co.larry.popularmovie.Fragments.TabFragment2;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        mNumOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TabFragment1();
            case 1:
                return new TabFragment2();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
