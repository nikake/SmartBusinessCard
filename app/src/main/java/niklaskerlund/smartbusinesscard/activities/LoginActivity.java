package niklaskerlund.smartbusinesscard.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import niklaskerlund.smartbusinesscard.R;

/**
 * Created by Niklas on 2015-12-16.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity.class";
    private static final int REQUEST_SIGNUP = 0;
    private static final String MY_PREFERENCES = "MyPrefs";

    private SharedPreferences sharedPreferences;
    private EditText etEmail, etPassword;
    private Button buttonSignIn;
    private Firebase firebase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Creating context..");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        buttonSignIn = (Button) findViewById(R.id.button_login);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Starting context..");
        super.onStart();
        firebase = new Firebase("https://smartbusinesscard.firebaseio.com");
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("Username", "");
        if(!savedEmail.isEmpty()){
            etEmail.setText(savedEmail);
        }
    }

    public void login(View view){
        Log.d(TAG, "Login");

        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Toast.makeText(getBaseContext(), "Login success!", Toast.LENGTH_LONG).show();

                // Save current users authentication to Firebase.
                Map<String, Object> map = new HashMap<>();
                map.put("provider", authData.getProvider());
                map.put("token", authData.getToken());
                firebase.child("users").child(authData.getUid()).updateChildren(map);

                // Save current user credentials to a shared preferences file.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username", etEmail.getText().toString());
                editor.putString("Uid", authData.getUid());
                editor.commit();

                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError error) {
                switch(error.getCode()){
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        toastLong("User does not exist");
                    case FirebaseError.INVALID_PASSWORD:
                        toastLong("Wrong password");
                    default:
                        toastLong("Something went wrong");
                        error.toException().printStackTrace();
                }
                Toast.makeText(getBaseContext(), "Error, try again", Toast.LENGTH_SHORT).show();
            }
        };

        String userEmail = etEmail.getText().toString();
        String userPassword = etPassword.getText().toString();

        firebase.authWithPassword(userEmail, userPassword, authResultHandler);
    }

    private void toastLong(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void createProfile(View view){
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String username){
        Toast.makeText(getBaseContext(), "Sign in success!", Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent();
        loginIntent.putExtra("result", username);
        setResult(Activity.RESULT_OK, loginIntent);
        finish();
    }

    public void onLoginFailed(){
        Toast.makeText(getBaseContext(), "Sign in failed", Toast.LENGTH_LONG).show();
    }


    /*


    1. Create activity.
        * Instantiate Firebase, create a reference.
    2. User decides to sign in.
        a) Check if e-mail is an e-mail.
        b) Check if password is at least 1 digit.
        c) Check if user exists.
        d) Check if password is correct.
        e) Sign in user.
        f) Save AuthToken (firebase.getAuth()) and finish acticity. [Send user to MainActivity]
    3. User decides to create new profile. [Send user to UpdateProfileActivity]


     */
}
