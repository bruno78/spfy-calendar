package com.brunogtavares.mycalendar.MobileFrontEnd;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.brunogtavares.mycalendar.R;

/**
 * Created by brunogtavares on 6/22/18.
 * CategoryAdapter is a FragmentPagerAdapter that can provide the layout for each list item.
 */

public class CategoryAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private static final int NUM_PAGES = 2;

    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new CalendarFragment();
        }
        else {
            return new EventListFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_calendar);
        }
        else {
            return mContext.getString(R.string.category_events);
        }
    }
}
