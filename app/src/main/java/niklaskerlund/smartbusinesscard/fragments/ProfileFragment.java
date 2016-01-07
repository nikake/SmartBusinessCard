package niklaskerlund.smartbusinesscard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.adapters.ProfileInterestAdapter;
import niklaskerlund.smartbusinesscard.util.Interest;
import niklaskerlund.smartbusinesscard.util.ExpandableGridView;
import niklaskerlund.smartbusinesscard.util.User;

/**
 * Created by Niklas on 2015-12-21.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";

    private ArrayList<Interest> interests = new ArrayList<>();
    private Firebase firebase, userRef;
    private View rootView;
    private TextView name, description;
    private ExpandableGridView gridview;
    private ProfileInterestAdapter adapter;


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
        description = (TextView) rootView.findViewById(R.id.profile_description);
        gridview = (ExpandableGridView) rootView.findViewById(R.id.profile_tags);
//        setProfileName();
//        setProfileDescription();
//        setProfileTags();

        updateInfo();

        return rootView;
    }

    public void updateInfo() {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                description.setText(user.getDescription());
                interests = user.getInterests();
                Log.d(TAG, "Userinfo updated. Name: " + user.getName() + " Description: " + user.getDescription() + "Interests: " + user.getInterests());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Log.d(TAG, "Initiating adapter.");
        adapter = new ProfileInterestAdapter(this.getContext(), R.layout.item_tag_profile, interests);
        gridview.setAdapter(adapter);
        gridview.setExpanded(true);
        Log.d(TAG, "Initialization finished.");
    }

//    public void setProfileName(){
//        userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String text = dataSnapshot.getValue(String.class);
//                if(text != null)
//                    name.setText(text);
//                else
//                    name.setText("");
//            }
//
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                name.setText("");
//            }
//        });
//    }
//
//    public void setProfileDescription(){
//        userRef.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String text = dataSnapshot.getValue(String.class);
//                if(text != null)
//                    description.setText(text);
//                else
//                    description.setText("");
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                description.setText("No description has been written yet.");
//            }
//        });
//    }
//
//    public void setProfileTags() {
//        userRef.child("interests").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Interest interest = dataSnapshot.getValue(Interest.class);
//                if(!interests.contains(interest))
//                    interests.add(interest);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Interest interest = dataSnapshot.getValue(Interest.class);
//                if(interests.contains(interest))
//                    interests.remove(interest);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//        adapter = new ProfileInterestAdapter(this.getContext(), R.layout.item_tag_profile, interests);
//        gridview.setAdapter(adapter);
//        gridview.setExpanded(true);
//    }
}
