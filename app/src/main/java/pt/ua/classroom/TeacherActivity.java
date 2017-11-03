package pt.ua.classroom;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity implements ClassesListFragment.OnItemSelectedListener {
    private static final String TAG = "Teacher Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, String.valueOf(Classe.getClasses()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
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
                startActivity(new Intent(this, Login.class));
                finish();
            case R.id.swap_role:
                studentActivity();
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // Handles the event when the fragment list item is selected
    @Override
    public void onItemSelected(Classe classe) {
        Database.setClasseid(classe.getId());
        //startActivity(new Intent(this, ItemDetailActivity.class););
    }

    public void studentActivity(){
        startActivity(new Intent(this, StudentActivity.class));
    }

}
