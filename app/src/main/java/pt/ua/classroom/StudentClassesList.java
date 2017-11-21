package pt.ua.classroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class StudentClassesList extends AppCompatActivity implements  StudentClassesListFragment.OnItemSelectedListener{
    private static final String TAG = "StudentClassesList";
    private StudentClassesList activity=this;
    private String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_classes_list);
        classId="";

        StudentClassesListFragment fragmentItemsList = (StudentClassesListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStudentClassesList);
        fragmentItemsList.setActivateOnItemClick(true);

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
                break;

            case R.id.swap_role:
                Database.getTeachingClasses(this);
                break;

            case R.id.default_role:
                Database.setRole("student");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(StudentClasse i) {
        classId=i.getId();
    }
}