package niklaskerlund.smartbusinesscard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import niklaskerlund.smartbusinesscard.R;

/**
 * Created by Thomas on 2016-01-05.
 */
public class CheckInConference extends FragmentActivity implements OnMapReadyCallback {

    private double longitude,latitude;
    private Marker marker;
    private final static String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";
    Firebase firebase;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_conference);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        firebase = new Firebase(intent.getStringExtra("cid"));


    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng conference = new LatLng(latitude, longitude);
        marker = map.addMarker(new MarkerOptions().position(conference).title("Conference location").draggable(false));
        map.moveCamera(CameraUpdateFactory.newLatLng(conference));
    }

    public void getCoordinates(){

    }
}
