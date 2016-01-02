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
public class ContactsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;

    public ContactsFragment(){

    }

    public static ContactsFragment newInstance(int sectionNumber) {
        ContactsFragment contactsFragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        contactsFragment.setArguments(args);
        return contactsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_contact, container, false);
        return rootView;
    }
}
