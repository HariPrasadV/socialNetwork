package db.socialnetwork;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jeyasoorya on 11/10/17.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter{

    private final String titles[] = new String[] {"Feed","Post","Search","Settings"};

    public SimpleFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0 : return new SeePosts();
            case 1 : return new AddPost();
            case 2 : return new Search();
            case 3 : return new Settings();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
