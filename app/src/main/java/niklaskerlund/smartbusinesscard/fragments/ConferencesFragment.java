package niklaskerlund.smartbusinesscard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.activities.ViewConferenceActivity;
import niklaskerlund.smartbusinesscard.adapters.ConferenceAdapter;
import niklaskerlund.smartbusinesscard.util.Conference;

/**
 * Created by Niklas on 2015-12-21.
 */
public class ConferencesFragment extends Fragment {

    private static final String TAG = ConferencesFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/conferences";
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ViewConferenceActivity.class);
                Conference conference = adapter.getItem(position);
                intent.putExtra("cid",conference.getCid());
                startActivity(intent);
            }
        });
        updateList();

        return rootView;
    }

    /*
    *   Fill the ArrayList with data.
    *   Everytime a new Conference is added, it will add it to the list.
     */

    private void updateList() {
        Query sortByDate = firebase.orderByChild("date");
        // Add a listener for new children to the conference branch in the database.

        sortByDate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot conferenceSnapshot : dataSnapshot.getChildren()) {
                    Conference conference = conferenceSnapshot.getValue(Conference.class);
                    if (!conferences.contains(conference)) {
                        conferences.add(conference);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Log.d(TAG, "Initiating adapter and ListView for ConferenceFragment");
        // Initiate adapter and add it to the ListView.
        adapter = new ConferenceAdapter(this.getContext(), R.layout.item_conference, conferences);
        listView.setAdapter(adapter);
    }
}
