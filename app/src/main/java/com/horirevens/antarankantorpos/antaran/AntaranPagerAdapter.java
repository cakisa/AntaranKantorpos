package com.horirevens.antarankantorpos.antaran;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.horirevens.antarankantorpos.fragment.AntaranTab1;
import com.horirevens.antarankantorpos.fragment.AntaranTab2;
import com.horirevens.antarankantorpos.fragment.AntaranTab3;

/**
 * Created by horirevens on 1/18/17.
 */
public class AntaranPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    public String anippos;

    public AntaranPagerAdapter(FragmentManager fm, int numOfTabs, String anippos) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.anippos = anippos;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("anippos", anippos);

        switch (position) {
            case 0:
                AntaranTab1 antaranTab1 = new AntaranTab1();
                antaranTab1.setArguments(bundle);
                return antaranTab1;
            case 1:
                AntaranTab2 antaranTab2 = new AntaranTab2();
                antaranTab2.setArguments(bundle);
                return antaranTab2;
            case 2:
                AntaranTab3 antaranTab3 = new AntaranTab3();
                antaranTab3.setArguments(bundle);
                return antaranTab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
