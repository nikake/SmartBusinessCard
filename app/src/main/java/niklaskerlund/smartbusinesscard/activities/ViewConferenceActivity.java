package niklaskerlund.smartbusinesscard.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.util.Conference;
import niklaskerlund.smartbusinesscard.util.Interest;
import niklaskerlund.smartbusinesscard.util.User;

/**
 * Created by Niklas on 2015-12-30.
 */
public class ViewConferenceActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static String TAG = ViewConferenceActivity.class.getSimpleName();
    private final static String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private final static String LOCATION_KEY = "location-key";
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    Context context;
    Firebase firebase, confRef, userRef;
    TextView name, description, date, time, feedback;
    Button checkInButton;
    private double userLatitude, userLongitude, confLatitude, confLongitude;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private boolean requestingLocationUpdates, userCheckedIn;
    private int currentapiVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_view);

        Intent intent = getIntent();
        firebase = new Firebase(FIREBASE_URL);
        userRef = new Firebase(FIREBASE_URL).child("users").child(firebase.getAuth().getUid());
        confRef = firebase.child("conferences").child(intent.getStringExtra("cid"));

        name = (TextView) findViewById(R.id.view_conference_name);
        description = (TextView) findViewById(R.id.view_conference_description);
        date = (TextView) findViewById(R.id.view_conference_date);
        time = (TextView) findViewById(R.id.view_conference_time);
        feedback = (TextView) findViewById(R.id.view_conference_feedback);
        checkInButton = (Button) findViewById(R.id.view_conference_button);

        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        context = getBaseContext();
        requestingLocationUpdates = false;

        updateValuesFromBundle(savedInstanceState);
    }

    @Override
    public void onStart() {
        updateInfo();
        isUserCheckedIn();
        super.onStart();
    }

    private void updateInfo() {
        confRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Conference conference = dataSnapshot.getValue(Conference.class);
                name.setText(conference.getName());
                description.setText(conference.getDescription());
                date.setText(conference.getDate());
                time.setText(conference.getStartTime());

                confLatitude = conference.getLatitude();
                confLongitude = conference.getLongitude();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void isUserCheckedIn() {
        Log.d(TAG, "isUserCheckedIn()");
        Query query = confRef.child("users").child(firebase.getAuth().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getValue(String.class);
                Log.d(TAG, "UserID: " + uid);
                if (uid != null) {
                    if (uid.equals(firebase.getAuth().getUid())) {
                        Log.d(TAG, "User checked in.");
                        giveFeedback();
                        deactivateButton();
                    } else {
                        Log.d(TAG, "User not checked in.");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "Firebase Error: " + firebaseError.getCode() + "\n" + firebaseError.getMessage());
            }
        });
    }

    public void checkIn(View view) {
        boolean gps_enabled = false;
        boolean network_enabled = false;

        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled || !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("GPS and/or network connection not enabled, please enable to continue");
            dialog.setPositiveButton("Location settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int parameter) {
                    Intent inte = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(inte);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int parameter) {

                }
            });
            dialog.show();
        } else {
            if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                } else {
                    initGPS();
                    finishTracking();
                }
            } else {
                initGPS();
                finishTracking();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initGPS();
                    finishTracking();

                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Location services connected.");

        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        } catch (SecurityException e) {
            Log.d(TAG, e.getMessage());
        }

        if (lastLocation != null) {
            handleNewLocation(lastLocation);
        } else {
            Log.d(TAG, "Last known location equals null.");
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } catch (SecurityException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, "New location: " + location.toString());
        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();
        Log.d(TAG, "Lat & Long updated. Lat: " + userLatitude + " Long: " + userLongitude);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Location services failed connecting. Please try again.");

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void initGPS() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000)
                    .setFastestInterval(5000);
        }
        googleApiClient.connect();
        if (userAtConference()) {
            Log.d(TAG, "User checked in.");
            registerUserToConference();
            giveFeedback();
            deactivateButton();
            Log.d(TAG, "Pairing users..");
            pairUsers();
        } else
            Log.d(TAG, "User not in range of Conference. User is not checked in.");
    }

    private void registerUserToConference() {
        HashMap<String, Object> attendingUser = new HashMap<>();
        attendingUser.put(firebase.getAuth().getUid(), firebase.getAuth().getUid());
        confRef.child("users").updateChildren(attendingUser);
    }

    private void giveFeedback() {
        feedback.setText(R.string.conference_view_feedback_true);
        feedback.setTextColor(getResources().getColor(R.color.green));
    }

    private void deactivateButton() {
        checkInButton.setEnabled(false);
        checkInButton.setClickable(false);
    }

    private boolean userAtConference() {
        boolean userAtConference = false;

        double latDiff = userLatitude - confLatitude;
        double lonDiff = userLongitude - confLongitude;

        Log.d(TAG, "Difference, Lat: " + latDiff + " Lon: " + lonDiff);
        if ((latDiff < 0.01 && latDiff > -0.01) && (lonDiff < 0.01 && lonDiff > -0.01))
            userAtConference = true;
        return userAtConference;
    }

    private ArrayList<Interest> userInterests;
    private ArrayList<String> foundContacts;
    private HashMap<String, ArrayList<Interest>> contacts;

    private void pairUsers() {
        userInterests = new ArrayList<>();
        foundContacts = new ArrayList<>();
        contacts = new HashMap<>();

        new PairUser().execute("");
    }

    private void addContact(String contact) {
        String user = firebase.getAuth().getUid();
        HashMap<String, Object> contacts = new HashMap<>();
        contacts.put(user, user);
        firebase.child("users").child(contact).child("contacts").updateChildren(contacts);

        contacts = new HashMap<>();
        contacts.put(contact, contact);
        firebase.child("users").child(user).child("contacts").updateChildren(contacts);
    }

    private void finishTracking() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                requestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                lastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, lastLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    private class PairUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Get user interests.
            Log.d(TAG, "Getting user interests.");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    userInterests = user.getInterests();
                    Log.d(TAG, "Added interests: " + userInterests);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d(TAG, firebaseError.getMessage());
                }
            });
            Log.d(TAG, "User interests: " + userInterests);

            // Find userID's to compare.
            Log.d(TAG, "Finding other users to compare");
            confRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String contactUid = userSnapshot.getValue(String.class);
                        if (!userSnapshot.getValue().equals(firebase.getAuth().getUid())) {
                            if (!foundContacts.contains(contactUid))
                                foundContacts.add(contactUid);
                        }
                    }
                    Log.d(TAG, "Added users: " + foundContacts);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d(TAG, firebaseError.getMessage());
                }
            });
            Log.d(TAG, "Other users: " + foundContacts);

            Log.d(TAG, "Get users and interests.");
            firebase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (!userSnapshot.getKey().equals(firebase.getAuth().getUid())) {
                            if (foundContacts.contains(userSnapshot.getKey()))
                                contacts.put(userSnapshot.getKey(), user.getInterests());
                        }
                    }
                    Log.d(TAG, "Added users and their interests to map: " + contacts);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d(TAG, firebaseError.getMessage());
                }
            });
            Log.d(TAG, "Hash Map with users and interests: " + contacts);

            return "executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d(TAG, "Comparing interests");
            for (Map.Entry<String, ArrayList<Interest>> contact : contacts.entrySet()) {
                for (Interest interest : contact.getValue()) {
                    if (userInterests.contains(interest)) ;
                    {
                        addContact(contact.getKey());
                        Log.d(TAG, "Match found! User: " + contact.getKey() + " Interest: " + interest);
                    }
                }
            }
            Log.d(TAG, "pairUsers() finished.");
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}



