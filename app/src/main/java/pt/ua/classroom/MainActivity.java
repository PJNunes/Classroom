package pt.ua.classroom;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private GoogleApiClient mGoogleApiClient;
    private String mPhotoUrl;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        Log.d(TAG, "anon");
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            Log.d(TAG, "null");
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        } else {
            mUsername = mUser.getDisplayName();
            Log.d(TAG, mUsername);
            Log.d(TAG, mUser.getEmail());
            if (mUser.getPhotoUrl() != null) {
                mPhotoUrl = mUser.getPhotoUrl().toString();
            }
        }

        Log.d(TAG, "google");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        Log.d(TAG, "google2");
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
