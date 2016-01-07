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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.activities.ViewContactActivity;
import niklaskerlund.smartbusinesscard.adapters.ContactsAdapter;
import niklaskerlund.smartbusinesscard.util.User;

/**
 * Created by Niklas on 2015-12-21.
 */
public class ContactsFragment extends Fragment {

    private static final String TAG = ContactsFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/users";
    private View rootView;
    private ListView listView;
    private Firebase firebase, userRef;
    private ArrayList<String> contactsUid = new ArrayList<>();
    private ArrayList<User> contacts = new ArrayList<>();
    private ContactsAdapter adapter;

    public ContactsFragment() {
        firebase = new Firebase(FIREBASE_URL);
        userRef = firebase.child(firebase.getAuth().getUid()).child("contacts");
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
        rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        listView = (ListView) rootView.findViewById(R.id.contact_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ViewContactActivity.class);
//                User contact = adapter.getItem(position);
//                intent.putExtra("cid", contactUid);
                startActivity(intent);
            }
        });
        updateList();

        return rootView;
    }

    private void updateList() {
        Log.d(TAG, "Updating contactsUid list.");
        // Get strings from Users/UserID/Contacts

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    String contactUid = contactSnapshot.getValue(String.class);
                    if(!contactsUid.contains(contactUid)){
                        contactsUid.add(contactUid);
                        Log.d(TAG, "Added user to contactUid list: " + contactUid);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Query sortByName = firebase.orderByChild("name");
        Log.d(TAG, "Updating contacts list.");
        // Get Users from Users that match String in ArrayList contactsUid
        sortByName.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()){
                    User contact = contactSnapshot.getValue(User.class);
                    if(contactsUid.contains(contactSnapshot.getKey())) {
                        if(!contacts.contains(contact)){
                            contacts.add(contact);
                            Log.d(TAG, "Added user to contacts list: " + contact.getName());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Log.d(TAG, "Initiating adapter and ListView.");
        // Initiate adapter and add it to the ListView.
        adapter = new ContactsAdapter(this.getContext(), R.layout.item_contact, contacts);
        listView.setAdapter(adapter);
    }
}
