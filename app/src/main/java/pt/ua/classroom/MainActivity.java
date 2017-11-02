package pt.ua.classroom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private GoogleApiClient mGoogleApiClient;
    private String mPhotoUrl;
    private String mId="";
    private String role="";
    DatabaseReference database;

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
        database = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mUsername = mUser.getEmail();
                    if (mUser.getPhotoUrl() != null) {
                        mPhotoUrl = mUser.getPhotoUrl().toString();
                        setImage();
                    }
                    nextActivity();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    // Not signed in, launch the Sign In activity
                    doLogin();
                    finish();
                }
                // ...
            }
        };
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


    }

    public void doLogin(){
        startActivity(new Intent(this, Login.class));
    }

    public void nextActivity(){
        //startActivity(new Intent(this, SelectRole.class));
        Log.d(TAG, String.valueOf(database));
        DatabaseReference ref= database.child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String temp = ds.getKey();
                    Log.d(TAG, ds.child("e-mail").getValue(String.class));
                    Log.d(TAG, mUsername);
                    if(ds.child("e-mail").getValue(String.class).equals(mUsername)) {
                        mId=temp;
                        role =ds.child("role").getValue(String.class);
                        Log.d(TAG, role);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(eventListener);
        Log.d(TAG, role);
        if (role==null)
            selectRole();
        else if(role.equals("teacher"))
            startTeacher();
        else
            startStudent();
    }

    public void selectRole(){
        startActivity(new Intent(this,SelectRole.class));
    }

    public void startTeacher(){
        startActivity(new Intent(this,TeacherActivity.class));
    }

    public void startStudent(){
        Log.d(TAG, "here");
        startActivity(new Intent(this,StudentActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void setImage(){
        ImageView i = (ImageView)findViewById(R.id.imageView);
        AsyncTask<String, Integer, Bitmap> bitmap = new Image().execute(mPhotoUrl);
        try {
            i.setImageBitmap(bitmap.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
