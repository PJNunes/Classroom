package pt.ua.classroom;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

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

    // Handles the event when the fragment list item is selected
    @Override
    public void onItemSelected(Classe classe) {
        Database.setClasseid(classe.getId());
        //startActivity(new Intent(this, ItemDetailActivity.class););
    }
}
