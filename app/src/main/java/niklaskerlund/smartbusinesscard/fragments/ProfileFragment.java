package niklaskerlund.smartbusinesscard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.adapters.ProfileTagAdapter;
import niklaskerlund.smartbusinesscard.data.Tag;
import niklaskerlund.smartbusinesscard.util.ExpandableGridView;

/**
 * Created by Niklas on 2015-12-21.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";

    private ArrayList<Tag> tags = new ArrayList<>();
    private Firebase firebase, userRef;
    private View rootView;
    private TextView name, description;
    private ExpandableGridView gridview;
    private ProfileTagAdapter adapter;


    public ProfileFragment(){
        firebase = new Firebase(FIREBASE_URL);
        userRef = firebase.child("users").child(firebase.getAuth().getUid());
    }

    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        name = (TextView) rootView.findViewById(R.id.profile_name);
        setProfileName();

        description = (TextView) rootView.findViewById(R.id.profile_description);
        setProfileDescription();

        gridview = (ExpandableGridView) rootView.findViewById(R.id.profile_tags);
        setProfileTags();

        return rootView;
    }

    public void setProfileName(){
        userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                if(text != null)
                    name.setText(text);
                else
                    name.setText("");
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                name.setText("");
            }
        });
    }

    public void setProfileDescription(){
        userRef.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                if(text != null)
                    description.setText(text);
                else
                    description.setText("");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                description.setText("No description has been written yet.");
            }
        });
    }

    public void setProfileTags() {
        userRef.child("tags").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Tag tag = dataSnapshot.getValue(Tag.class);
                if(!tags.contains(tag))
                    tags.add(tag);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Tag tag = dataSnapshot.getValue(Tag.class);
                if(tags.contains(tag))
                    tags.remove(tag);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        adapter = new ProfileTagAdapter(this.getContext(), R.layout.item_tag_profile, tags);
        gridview.setAdapter(adapter);
        gridview.setExpanded(true);
    }
}
