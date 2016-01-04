package niklaskerlund.smartbusinesscard.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import niklaskerlund.smartbusinesscard.fragments.ConferencesFragment;
import niklaskerlund.smartbusinesscard.fragments.ContactsFragment;
import niklaskerlund.smartbusinesscard.fragments.ProfileFragment;


/**
 * Created by Niklas on 2015-12-21.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final static int NUM_OF_FRAGMENTS = 3;
    private final static String
            PROFILE_TITLE = "Profile",
            CONTACTS_TITLE = "Contacts",
            CONFERENCES_TITLE = "Conferences";
    private Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return ProfileFragment.newInstance(position);
            case 1:
                return ContactsFragment.newInstance(position);
            case 2:
                return ConferencesFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_OF_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return PROFILE_TITLE;
            case 1:
                return CONTACTS_TITLE;
            case 2:
                return CONFERENCES_TITLE;
            default:
                return null;
        }
    }
}
