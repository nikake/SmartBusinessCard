package niklaskerlund.smartbusinesscard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.adapters.ConferenceAdapter;
import niklaskerlund.smartbusinesscard.util.Conference;

/**
 * Created by Niklas on 2015-12-21.
 */
public class ConferencesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";
    private View rootView;
    private ListView listView;
    private Firebase firebase;
    private ArrayList<Conference> conferences = new ArrayList<>();
    private ConferenceAdapter adapter;

    public ConferencesFragment() {
        firebase = new Firebase(FIREBASE_URL);
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
        rootView = inflater.inflate(R.layout.fragment_conferences, container, false);

        listView = (ListView) rootView.findViewById(R.id.conference_list);
        updateList();

        return rootView;
    }

    private void updateList() {
        firebase.child("conferences").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Conference conference = dataSnapshot.getValue(Conference.class);
                if(!conferences.contains(conference))
                    conferences.add(conference);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Conference conference = dataSnapshot.getValue(Conference.class);
                if(conferences.contains(conference))
                    conferences.remove(conference);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        adapter = new ConferenceAdapter(this.getContext(), R.layout.item_conference, conferences);
        listView.setAdapter(adapter);
    }
}
