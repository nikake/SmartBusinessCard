package niklaskerlund.smartbusinesscard.activities;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Niklas on 2015-12-30.
 */
public class SmartBusinessCardApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
