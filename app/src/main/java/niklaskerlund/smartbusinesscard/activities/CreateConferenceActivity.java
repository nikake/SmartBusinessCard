package niklaskerlund.smartbusinesscard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.util.Conference;

import com.firebase.client.Firebase;

/**
 * Created by Niklas on 2015-12-30.
 */
public class CreateConferenceActivity extends AppCompatActivity {

    private static final String TAG = "CreateConferenceActivity";
    private static final int COORDINATES_SUCCESS_CODE = 10;

    private Firebase firebase;
    TextView name, desc, date, time;
    double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conference);

        name = (TextView) findViewById(R.id.new_conference_name);
        desc = (TextView) findViewById(R.id.new_conference_description);
        date = (TextView) findViewById(R.id.new_conference_date);
        time = (TextView) findViewById(R.id.new_conference_time);

        firebase = new Firebase("https://smartbusinesscard.firebaseio.com/");
    }

    public void googleMap(View view) {
        Intent intent = new Intent(this, ConferenceMapActivity.class);
        startActivityForResult(intent, COORDINATES_SUCCESS_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case COORDINATES_SUCCESS_CODE:
                TextView textView = (TextView) findViewById(R.id.create_conference_google_maps_boolean);
                textView.setText("Position chosen.");
                latitude = data.getDoubleExtra("lat", 0);
                longitude = data.getDoubleExtra("lon", 0);
                break;
        }

    }

    public void createConference(View view){

        Firebase newConference = firebase.child("conferences").push();
        Conference conference = new Conference(name.getText().toString(),
                desc.getText().toString(), date.getText().toString(), time.getText().toString(), latitude, longitude, firebase.getAuth().getUid(), newConference.getKey());

        newConference.setValue(conference);

        finish();
    }
}
