package niklaskerlund.smartbusinesscard.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
    TextView name, description;
    private double longitude, latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_view);

        Intent intent = getIntent();
        firebase = new Firebase(FIREBASE_URL);
        confRef = firebase.child("conferences").child(intent.getStringExtra("cid"));
        name = (TextView) findViewById(R.id.view_conference_name);
        description = (TextView) findViewById(R.id.view_conference_description);

        setConferenceName();
        setConferenceDescription();
        setConferenceLocation();
    }

    private void setConferenceName() {
        Log.d(TAG, "setConferenceName: " + confRef);
        confRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                if (text != null) {
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

    private void setConferenceDescription() {
        Log.d(TAG, "setConferenceDescription: " + confRef);
        confRef.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                if (text != null) {
                    Log.d(TAG, "Setting text to: " + text);
                    description.setText(text);
                } else {
                    Log.d(TAG, "Unable to set conference description.");
                    description.setText("Unabled");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                description.setText("No description");
                Log.d(TAG, "Cancelled");
            }
        });
    }

    private void setConferenceLocation() {
        Log.d(TAG, "setConferenceLongitude: " + confRef);
        confRef.child("longitude").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                longitude = dataSnapshot.getValue(double.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Log.d(TAG, "setConferenceLatitude: " + confRef);
        confRef.child("latitude").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latitude = dataSnapshot.getValue(double.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void checkIn(View view) {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){}

        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){}

        if(!gps_enabled || !network_enabled){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("GPS and/or network connection not enabled, please enable to continue");
            dialog.setPositiveButton("Location settings", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int parameter){
                    Intent inte = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(inte);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int parameter){

                }
            });
            dialog.show();
        }else{
            Intent intent = new Intent(this, CheckInConference.class);
            intent.putExtra("cid", confRef.toString());
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivity(intent);
        }




    }
}



