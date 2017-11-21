package pt.ua.classroom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    private String mId="";
    private MainActivity classe;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private static FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set default username is anonymous.
        classe=this;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Database.setId(classe,mUser.getDisplayName(),mUser.getEmail(),mUser.getPhotoUrl());
                    if (mUser.getPhotoUrl() != null) {
                        setImage();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    // Not signed in, launch the Sign In activity
                    doLogin();
                    finish();
                }
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void doLogin(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void nextActivity(){

        String role= Database.getRole();
        if(role==null) {
            Log.d(TAG, "null");
            selectRole();
            finish();
        }
        else if(role.equals("teacher")){
            Log.d(TAG, role);
            Database.getTeachingClasses(this);
        }
        else if(role.equals("student")){
            Log.d(TAG, role);
            studentActivity();
        }
    }

    public void selectRole(){
        startActivity(new Intent(this,SelectRoleActivity.class));
    }

    public void studentActivity(){
        startActivity(new Intent(this, StudentActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void setImage(){
        ImageView i = (ImageView)findViewById(R.id.imageView);
        AsyncTask<String, Integer, Bitmap> bitmap = new Image().execute(String.valueOf(Database.getPhoto()));
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
