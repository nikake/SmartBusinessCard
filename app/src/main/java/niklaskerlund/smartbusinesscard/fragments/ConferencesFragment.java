package niklaskerlund.smartbusinesscard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import niklaskerlund.smartbusinesscard.R;

/**
 * Created by Niklas on 2015-12-21.
 */
public class ConferencesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;

    public ConferencesFragment(){

    }

    public static ConferencesFragment newInstance(int sectionNumber) {
        ConferencesFragment conferencesFragment = new ConferencesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        conferencesFragment.setArguments(args);
        return conferencesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_conference, container, false);
        return rootView;
    }
}
