package niklaskerlund.smartbusinesscard.activities;


import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.adapters.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity.class";
    private static final String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";
    private static final int
            CODE_LOGIN_SUCCESS = 1,
            EDIT_PROFILE_SUCCESS = 2;


    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private AuthData activeUser = null;
    Firebase firebase;
    Firebase activeUserRef;
    Firebase.AuthStateListener firebaseAuthStateListener;
    ValueEventListener valueEventListener = new ValueEventListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase = new Firebase(FIREBASE_URL);
        firebase.addValueEventListener(valueEventListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(activeUser == null){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, CODE_LOGIN_SUCCESS);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.container);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == CODE_LOGIN_SUCCESS) {
                    activeUser = firebase.getAuth();
                    activeUserRef = firebase.child(activeUser.getUid());
                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.user_profile:
                if (viewPager.getCurrentItem() != 0 )
                    viewPager.setCurrentItem(0);
                break;
            case R.id.edit_profile:
                startActivity(new Intent(this, UpdateProfileActivity.class));
                break;
            case R.id.contacts:
                if (viewPager.getCurrentItem() != 1 )
                    viewPager.setCurrentItem(1);
                break;
            case R.id.new_contacts:
                Toast.makeText(this, "New Contacts", Toast.LENGTH_SHORT).show();
                break;
            case R.id.conferences:
                if (viewPager.getCurrentItem() != 2 )
                    viewPager.setCurrentItem(2);
                break;
            case R.id.new_conferences:
                startActivity(new Intent(this, CreateConferenceActivity.class));
                break;
            case R.id.conference_schedule:
                Toast.makeText(this, "Conference Schedule", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private boolean userAuthenticated;

        @Override
        public void onAuthenticated(AuthData authData) {
            userAuthenticated = true;
            Log.i(TAG, "Authentication successful.");
            activeUser = authData;
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            userAuthenticated = false;
            Log.d(TAG, "Firebase Error: " + firebaseError.getCode() + "\n     " + firebaseError.getMessage());
            showErrorDialog(firebaseError.toString());
        }

        public boolean isUserAuthenticated(){
            return userAuthenticated;
        }
    }

    private class ValueEventListener implements com.firebase.client.ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(TAG, "Data changed successfully: " + dataSnapshot.getValue());

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.d(TAG, "Firebase Error: " + firebaseError.getMessage());
        }
    }

    /*


//    1. Check if user is Authenticated.
//        a. User is Authenticated. [Continue with 2]
//        b. User is not Authenticated. [Send user to LoginActivity.class with 'startActivityForResult', resultCode 1]
    2. Start MainActivity.
        * Instantiate Firebase
        * Add AuthStateListener to Firebase reference. (???)
    3. Check if user account has a user profile.
        a) User has a user profile. [Continue with 4]
        b) User doesn't have a user profile. [Send user to UpdateProfileActivity.class with 'startActivityForResult', resultCode 2]
    4. Fetch data from Firebase belonging to Authenticated User.
        * Update fragment_profile
            - Profile picture
            - Profile description
            - Profile interests/tags
        * Update contacts list
        * Update conference list


     */
}
