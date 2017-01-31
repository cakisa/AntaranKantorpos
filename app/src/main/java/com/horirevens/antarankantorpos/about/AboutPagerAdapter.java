package com.horirevens.antarankantorpos.about;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.horirevens.antarankantorpos.fragment.About1;
import com.horirevens.antarankantorpos.fragment.About2;
import com.horirevens.antarankantorpos.fragment.About3;

/**
 * Created by horirevens on 1/23/17.
 */
public class AboutPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public AboutPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                About1 about1 = new About1();
                return about1;
            case 1:
                About2 about2 = new About2();
                return about2;
            case 2:
                About3 about3 = new About3();
                return about3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
