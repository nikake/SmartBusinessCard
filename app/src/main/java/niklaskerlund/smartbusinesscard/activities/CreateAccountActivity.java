package niklaskerlund.smartbusinesscard.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import niklaskerlund.smartbusinesscard.R;

/**
 * Created by Niklas on 2015-12-22.
 */
public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";

    private Firebase firebase;
    private TextView email, password;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);
        email = (TextView) findViewById(R.id.new_user_email);
        password = (TextView) findViewById(R.id.new_user_password);
        firebase = new Firebase("https://smartbusinesscard.firebaseio.com/");
    }

    public void createAccount(View view){

        String newUserEmail = email.getText().toString();
        String newUserPassword = password.getText().toString();

        if (validEmail(newUserEmail) && validPassword(newUserPassword)){
            firebase.createUser(newUserEmail, newUserPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {

                @Override
                public void onSuccess(Map<String, Object> result) {
                    Toast.makeText(getBaseContext(), "Successfully created user account with uid: " + result.get("uid"), Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Toast.makeText(getBaseContext(), "Error: " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, firebaseError.getMessage() + "\n     " + firebaseError.getCode());
                }
            });
        }

    }

    private boolean validEmail(String email){

        return true;
    }

    private boolean validPassword(String password) {
        if(password.isEmpty())
            return false;
        else
            return true;
    }

    /*


    1. Create Activity
    * Instantiate Firebase
    2. User clicks button 'create new account'.
    a) Check if e-mail is a valid e-mail.
            b) Check if username is available.
            c) Check if password is a valid password.
    3. Create new user account.
            4. Finish activity. [User is sent back to LoginActivity, resultCode = 1]


     */
}
