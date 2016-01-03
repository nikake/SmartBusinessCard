package niklaskerlund.smartbusinesscard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import niklaskerlund.smartbusinesscard.R;

/**
 * Created by Niklas on 2016-01-03.
 */
public class MapPane extends FragmentActivity implements OnMapReadyCallback {

    private static final int COORDINATES_SUCCESS_CODE = 10;

    private double latitude, longitude;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        LatLng tullinge = new LatLng(59.205, 17.907);
        marker = map.addMarker(new MarkerOptions().position(tullinge).title("Marker in Tullinge").draggable(true));
        map.moveCamera(CameraUpdateFactory.newLatLng(tullinge));
    }

    public void saveCoordinates(View view) {
        Intent intent = new Intent();
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        intent.putExtra("lat", latitude);
        intent.putExtra("lon", longitude);

        setResult(COORDINATES_SUCCESS_CODE, intent);
        finish();
    }
}
