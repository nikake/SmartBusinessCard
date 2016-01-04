package niklaskerlund.smartbusinesscard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import niklaskerlund.smartbusinesscard.R;

/**
 * Created by Niklas on 2015-12-30.
 */
public class ViewConferenceActivity extends AppCompatActivity {

    private final static String TAG = "ViewConferenceActivity";
    private final static String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";
    Firebase firebase, confRef;
    TextView name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_view);

        Intent intent = getIntent();
        firebase = new Firebase(FIREBASE_URL);
        confRef = firebase.child("conferences").child(intent.getStringExtra("cid"));
        name = (TextView) findViewById(R.id.view_conference_name);
        setConferenceName();
    }

    private void setConferenceName(){
        Log.d(TAG, "setConferenceName: " + confRef);
        confRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                if(text != null){
                    Log.d(TAG, "Setting text to: " + text);
                    name.setText(text);
                } else {
                    Log.d(TAG, "Unable to set conference name.");
                    name.setText("Unabled");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                name.setText("No name");
                Log.d(TAG, "Cancelled");
            }
        });
    }
}
