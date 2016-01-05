package niklaskerlund.smartbusinesscard.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.firebase.client.Firebase;

import niklaskerlund.smartbusinesscard.R;

/**
 * Created by Niklas on 2015-12-30.
 */
public class ViewContactActivity extends AppCompatActivity {

    private final static String TAG = "ViewContactActivity";
    private final static String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";
    Firebase firebase, contactRef;
    TextView name, desription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);
    }
}
