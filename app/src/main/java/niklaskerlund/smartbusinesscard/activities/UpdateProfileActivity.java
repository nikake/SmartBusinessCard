package niklaskerlund.smartbusinesscard.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.util.Tag;
import niklaskerlund.smartbusinesscard.adapters.TagAdapter;
import niklaskerlund.smartbusinesscard.util.ExpandableGridView;


/**
 * Created by Niklas on 2015-12-17.
 */
public class UpdateProfileActivity extends AppCompatActivity {

    private static final String TAG = "UpdateProfileActivity";

    private Firebase firebase;
    private Firebase userRef;
    private ExpandableGridView gridView;
    private Tag[] tags = new Tag[]{
            Tag.ANDROID,
            Tag.CPLUSPLUS,
            Tag.CSHARP,
            Tag.DEVELOPMENT,
            Tag.EDUCATION,
            Tag.INTERNET_OF_THINGS,
            Tag.IT,
            Tag.JAVA,
            Tag.PROGRAMMING,
            Tag.RECRIUTMENT,
            Tag.SOCIAL_NETWORKING
    };

    ArrayList<Tag> selectedTags;
    TagAdapter adapter;
    TextView name, description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = (TextView) findViewById(R.id.update_profile_name);
        description = (TextView) findViewById(R.id.update_profile_description);
        gridView = (ExpandableGridView) findViewById(R.id.update_profile_tags);

        selectedTags = new ArrayList<>();
        initGridView();

        firebase = new Firebase("https://smartbusinesscard.firebaseio.com/");
        userRef = firebase.child("users").child(firebase.getAuth().getUid());
        Log.d(TAG, userRef.toString());
        setNameText();
        setDescriptionText();

    }

    private void initGridView() {
        adapter = new TagAdapter(this, R.layout.item_tag, tags);
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tag tag = adapter.getItem(position);
                CheckedTextView ctv = (CheckedTextView) view;
                if (ctv.isChecked())
                    selectedTags.add(tag);
                else
                    selectedTags.remove(tag);
            }
        });
    }

    private void setNameText() {
        userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                if (text != null)
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

    private void setDescriptionText() {
        userRef.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                if (text != null)
                    description.setText(text);
                else
                    description.setText("");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                description.setText("");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void uploadPhoto(View view) {
        Toast.makeText(getBaseContext(), "Function not yet implemented.", Toast.LENGTH_SHORT).show();
    }

    public void updateProfile(View view) {
        String uid = firebase.getAuth().getUid();

        if (uid.isEmpty()) {
            Toast.makeText(getBaseContext(), "Couldn't find your user-ID.", Toast.LENGTH_LONG).show();
            return;
        } else {
            Firebase userRef = firebase.child("users").child(uid);

            // Create map for simple user data.
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name.getText().toString());
            userData.put("description", description.getText().toString());
            userRef.updateChildren(userData);

            // Update child for selected tags with Map<String, Object> selectedTags.
            userRef.child("tags").setValue(selectedTags);

            finish();
        }

    }

    public String name() {
        return name.getText().toString();
    }

    public String description() {
        return description.getText().toString();
    }

    /*


    1. Create Activity
        * Instantiate Firebase
    2. User clicks button 'Save'.
        a) Check name.
        b) Check description or autofill it.
        c) Check profile picture or choose default.
        d) Check interests/tags.
    3. Update user profile.
    4. Finish activity. [User is sent back to MainActivity, resultCode = 1]


     */
}
