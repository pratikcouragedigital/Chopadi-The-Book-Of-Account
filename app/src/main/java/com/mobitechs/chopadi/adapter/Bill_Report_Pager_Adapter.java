package com.mobitechs.chopadi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mobitechs.chopadi.Bill_Report_Tab_Daily;
import com.mobitechs.chopadi.Bill_Report_Tab_Monthly;

public class Bill_Report_Pager_Adapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public Bill_Report_Pager_Adapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Bill_Report_Tab_Daily tab1 = new Bill_Report_Tab_Daily();
                return tab1;
            case 1:
                Bill_Report_Tab_Monthly tab3 = new Bill_Report_Tab_Monthly();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
