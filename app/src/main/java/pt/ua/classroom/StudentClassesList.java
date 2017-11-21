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

public class StudentClassesList extends AppCompatActivity implements  StudentClassesListFragment.OnItemSelectedListener, View.OnClickListener{
    private static final String TAG = "StudentClassesList";
    private StudentClassesList activity=this;
    private String classId,className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_classes_list);
        classId="";

        //Buttons
        Button buttonTeacher = (Button) findViewById(R.id.delete_attending_classe);
        buttonTeacher.setOnClickListener(this);

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
        className=i.getTitle();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.delete_attending_classe:
                deleteClasse();
            default:
        }
    }

    private void deleteClasse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (classId.equals("")){
            builder.setTitle("You need to select a classe first!");

            // Set up the buttons
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        else {
            builder.setTitle("Are you sure you want to delete this classe: "+className);

            // Set up the buttons
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Database.deleteAttendingClasse(activity, classId);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        builder.show();
    }
}